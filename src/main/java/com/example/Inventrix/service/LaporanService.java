package com.example.Inventrix.service;

import com.example.Inventrix.dto.ReqCreateLaporan;
import com.example.Inventrix.dto.ReqFilterLaporan;
import com.example.Inventrix.dto.ReqLaporanItem;
import com.example.Inventrix.dto.ResLaporanDetail;
import com.example.Inventrix.dto.ResLaporanList;
import com.example.Inventrix.model.Barang;
import com.example.Inventrix.model.Laporan;
import com.example.Inventrix.model.LaporanItem;
import com.example.Inventrix.model.PermintaanToko;
import com.example.Inventrix.repository.LaporanRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LaporanService {

    private final BarangService barangService;
    private final LaporanRepository laporanRepository;

    // ===========================================================
    // CREATE LAPORAN NORMAL (MASUK / RUSAK / HILANG)
    // ===========================================================
    @Transactional
    public Laporan createLaporan(ReqCreateLaporan req) {

        if (req.getItems() == null || req.getItems().isEmpty()) {
            throw new IllegalArgumentException("Daftar barang tidak boleh kosong");
        }

        Laporan.Jenis jenis = Laporan.Jenis.valueOf(req.getJenis().toUpperCase());

        Laporan laporan = new Laporan();
        laporan.setJenis(jenis);
        laporan.setSupplier(req.getSupplier());

        // Username dari JWT
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        laporan.setCreatedBy(username);

        for (ReqLaporanItem ri : req.getItems()) {

            Barang barang = barangService.findById(ri.getBarangId());
            if (barang == null) {
                throw new RuntimeException("Barang dengan ID " + ri.getBarangId() + " tidak ditemukan");
            }

            int jumlah = ri.getJumlah();
            if (jumlah <= 0) {
                throw new IllegalArgumentException("Jumlah harus lebih dari 0");
            }

            // Update stok (kecuali MUTASI)
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

                default:
                    break;
            }

            barangService.simpanBarang(barang);

            // Tambah item laporan
            LaporanItem item = new LaporanItem();
            item.setBarang(barang);
            item.setJumlah(jumlah);
            item.setKeterangan(ri.getKeterangan());

            laporan.addItem(item);
        }

        return laporanRepository.save(laporan);
    }

    // ===========================================================
    // LAPORAN OTOMATIS DARI PERMINTAAN (MUTASI TOKO)
    // ===========================================================
    @Transactional
    public Laporan buatLaporanMutasiToko(PermintaanToko p) {

        Laporan laporan = new Laporan();
        laporan.setJenis(Laporan.Jenis.MUTASI_TOKO);
        laporan.setSupplier(null);
        laporan.setCreatedBy(p.getCompletedBy());

        LaporanItem item = new LaporanItem();
        item.setBarang(p.getBarang());
        item.setJumlah(p.getJumlah());
        item.setKeterangan("Mutasi ke toko dari permintaan ID " + p.getId());

        laporan.addItem(item);

        return laporanRepository.save(laporan);
    }

    // ===========================================================
    // FILTER LAPORAN → RETURN DTO LIST
    // ===========================================================
    public Page<ResLaporanList> filterLaporan(ReqFilterLaporan req) {

        Pageable pageable = PageRequest.of(req.getPage(), req.getSize());

        Laporan.Jenis jenis = null;
        if (req.getJenis() != null && !req.getJenis().isEmpty()) {
            jenis = Laporan.Jenis.valueOf(req.getJenis().toUpperCase());
        }

        LocalDateTime tglMulai = null;
        LocalDateTime tglSelesai = null;

        if (req.getTanggalMulai() != null && !req.getTanggalMulai().isEmpty()) {
            tglMulai = LocalDateTime.parse(req.getTanggalMulai() + "T00:00:00");
        }

        if (req.getTanggalSelesai() != null && !req.getTanggalSelesai().isEmpty()) {
            tglSelesai = LocalDateTime.parse(req.getTanggalSelesai() + "T23:59:59");
        }

        Page<Laporan> result = laporanRepository.filterLaporan(
                jenis,
                req.getCreatedBy(),
                req.getNamaBarang(),
                tglMulai,
                tglSelesai,
                pageable
        );

        // Convert ke DTO aman
        return result.map(lap -> {
            ResLaporanList dto = new ResLaporanList();
            dto.setId(lap.getId());
            dto.setJenis(lap.getJenis().name());
            dto.setSupplier(lap.getSupplier());
            dto.setCreatedBy(lap.getCreatedBy());
            dto.setTanggal(lap.getTanggal());
            dto.setTotalItem(lap.getItems().size());
            return dto;
        });
    }

    // ===========================================================
    // FIND LAPORAN ENTITY BY ID
    // ===========================================================
    public Laporan findById(Long id) {
        return laporanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Laporan tidak ditemukan ID = " + id));
    }

    // ===========================================================
    // GET DETAIL LAPORAN → DTO DETAIL (AMAN)
    // ===========================================================
    public ResLaporanDetail getLaporanDetail(Long id) {

        Laporan lap = findById(id);

        ResLaporanDetail res = new ResLaporanDetail();
        res.setId(lap.getId());
        res.setJenis(lap.getJenis().name());
        res.setSupplier(lap.getSupplier());
        res.setCreatedBy(lap.getCreatedBy());
        res.setTanggal(lap.getTanggal());

        List<ResLaporanDetail.Item> items = lap.getItems().stream().map(i -> {
            ResLaporanDetail.Item dto = new ResLaporanDetail.Item();
            dto.setId(i.getId());
            dto.setBarangId(i.getBarang().getId());
            dto.setNamaBarang(i.getBarang().getNamaBarang());
            dto.setJumlah(i.getJumlah());
            dto.setKeterangan(i.getKeterangan());
            return dto;
        }).toList();

        res.setItems(items);

        return res;
    }
}
