package com.example.Inventrix.service;

import com.example.Inventrix.dto.ReqCreateLaporan;
import com.example.Inventrix.dto.ReqLaporanItem;
import com.example.Inventrix.model.Barang;
import com.example.Inventrix.model.Laporan;
import com.example.Inventrix.model.LaporanItem;
import com.example.Inventrix.repository.LaporanRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LaporanService {

    private final BarangService barangService;
    private final LaporanRepository laporanRepository;

    @Transactional
    public Laporan createLaporan(ReqCreateLaporan req) {

        if (req.getItems() == null || req.getItems().isEmpty()) {
            throw new IllegalArgumentException("Daftar barang tidak boleh kosong");
        }

        Laporan.Jenis jenis = Laporan.Jenis.valueOf(req.getJenis().toUpperCase());

        Laporan laporan = new Laporan();
        laporan.setJenis(jenis);
        laporan.setSupplier(req.getSupplier());

        // ===================================================
        // 🔥 AMBIL USERNAME YANG LOGIN DARI JWT SECARA OTOMATIS
        // ===================================================
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        laporan.setCreatedBy(username);

        // ===================================================
        // PROSES ITEM LAPORAN
        // ===================================================
        for (ReqLaporanItem ri : req.getItems()) {

            Barang barang = barangService.findById(ri.getBarangId());
            if (barang == null) {
                throw new RuntimeException("Barang dengan ID " + ri.getBarangId() + " tidak ditemukan");
            }

            int jumlah = ri.getJumlah();
            if (jumlah <= 0) {
                throw new IllegalArgumentException("Jumlah harus lebih dari 0");
            }

            // ===================================================
            // UPDATE STOK GUDANG
            // ===================================================
            switch (jenis) {
                case MASUK:
                    barang.setStokGudang(barang.getStokGudang() + jumlah);
                    break;

                case HILANG:
                case RUSAK:
                    if (barang.getStokGudang() < jumlah) {
                        throw new RuntimeException("Stok gudang tidak cukup untuk: " + barang.getNamaBarang());
                    }
                    barang.setStokGudang(barang.getStokGudang() - jumlah);
                    break;
            }

            // Simpan perubahan stok
            barangService.simpanBarang(barang);

            // ===================================================
            // BUAT ITEM LAPORAN
            // ===================================================
            LaporanItem item = new LaporanItem();
            item.setBarang(barang);
            item.setJumlah(jumlah);
            item.setKeterangan(ri.getKeterangan());

            laporan.addItem(item);
        }

        return laporanRepository.save(laporan);
    }
}
