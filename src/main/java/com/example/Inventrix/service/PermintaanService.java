package com.example.Inventrix.service;

import com.example.Inventrix.dto.ReqPermintaan;
import com.example.Inventrix.model.Barang;
import com.example.Inventrix.model.PermintaanToko;
import com.example.Inventrix.model.PermintaanToko.Status;
import com.example.Inventrix.repository.PermintaanRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PermintaanService {

    private final PermintaanRepository permintaanRepo;
    private final BarangService barangService;
    private final LaporanService laporanService;  // <-- WAJIB ADA

    // =====================================================
    // OWNER membuat permintaan
    // =====================================================
    @Transactional
    public PermintaanToko buatPermintaan(ReqPermintaan req) {

        if (req.getJumlah() == null || req.getJumlah() <= 0) {
            throw new IllegalArgumentException("Jumlah permintaan harus lebih dari 0");
        }

        Barang barang = barangService.findById(req.getBarangId());
        if (barang == null) {
            throw new RuntimeException("Barang tidak ditemukan id=" + req.getBarangId());
        }

        // Validasi stok gudang
        if (barang.getStokGudang() < req.getJumlah()) {
            throw new RuntimeException("Stok gudang tidak mencukupi untuk membuat permintaan");
        }

        PermintaanToko p = new PermintaanToko();
        p.setBarang(barang);
        p.setJumlah(req.getJumlah());
        p.setKeterangan(req.getKeterangan());
        p.setStatus(Status.DIPINTA);

        // createdBy dari JWT
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        p.setCreatedBy(username);
        p.setCreatedAt(LocalDateTime.now());

        return permintaanRepo.save(p);
    }

    // =====================================================
    // List berdasarkan status
    // =====================================================
    public List<PermintaanToko> listByStatus(Status status) {
        return permintaanRepo.findByStatus(status);
    }

    // =====================================================
    // Cari berdasarkan ID
    // =====================================================
    public PermintaanToko findById(Long id) {
        return permintaanRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Permintaan tidak ditemukan id=" + id));
    }

    // =====================================================
    // WAREHOUSE menekan "Kirim"
    // =====================================================
    @Transactional
    public PermintaanToko kirimPermintaan(Long id) {

        PermintaanToko p = findById(id);

        if (p.getStatus() != Status.DIPINTA) {
            throw new RuntimeException("Permintaan tidak bisa dikirim, status saat ini: " + p.getStatus());
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        p.setProcessedBy(username);
        p.setProcessedAt(LocalDateTime.now());
        p.setStatus(Status.DIKIRIM);

        return permintaanRepo.save(p);
    }

    // =====================================================
    // OWNER menekan "Selesai" → stok update + buat laporan
    // =====================================================
    @Transactional
    public PermintaanToko selesaiPermintaan(Long id) {

        PermintaanToko p = findById(id);

        if (p.getStatus() != Status.DIKIRIM) {
            throw new RuntimeException("Hanya permintaan berstatus DIKIRIM yang dapat diselesaikan");
        }

        Barang barang = p.getBarang();
        int jumlah = p.getJumlah();

        // Validasi stok gudang
        if (barang.getStokGudang() < jumlah) {
            throw new RuntimeException("Stok gudang tidak mencukupi saat konfirmasi selesai");
        }

        // Update stok
        barang.setStokGudang(barang.getStokGudang() - jumlah);
        barang.setStokToko(barang.getStokToko() + jumlah);
        barangService.simpanBarang(barang);

        // Update permintaan
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        p.setCompletedBy(username);
        p.setCompletedAt(LocalDateTime.now());
        p.setStatus(Status.SELESAI);

        permintaanRepo.save(p);

        // =====================================================
        // BUAT LAPORAN MUTASI TOKO
        // =====================================================
        laporanService.buatLaporanMutasiToko(p);

        return p;
    }

    // =====================================================
    // OWNER membatalkan permintaan
    // =====================================================
    @Transactional
    public PermintaanToko batalPermintaan(Long id) {

        PermintaanToko p = findById(id);

        if (p.getStatus() == Status.SELESAI) {
            throw new RuntimeException("Tidak bisa membatalkan permintaan yang sudah selesai");
        }

        p.setStatus(Status.BATAL);
        return permintaanRepo.save(p);
    }
}
