package com.example.Inventrix.service;

import com.example.Inventrix.model.Barang;
import com.example.Inventrix.repository.BarangRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BarangService {

    private final BarangRepository barangRepository;

    public BarangService(BarangRepository barangRepository) {
        this.barangRepository = barangRepository;
    }

    // 🔹 Tambah barang baru
    public Barang tambahBarang(Barang barang) {
        if (barangRepository.existsByKodeBarang(barang.getKodeBarang())) {
            throw new RuntimeException("Kode barang sudah digunakan!");
        }
        return barangRepository.save(barang);
    }

    // 🔹 Cari barang berdasarkan ID
    public Barang findById(Long id) {
        return barangRepository.findById(id).orElse(null);
    }

    // 🔹 Simpan perubahan (untuk edit barang)
    public Barang simpanBarang(Barang barang) {
        return barangRepository.save(barang);
    }

    // 🔹 Ambil semua barang (search + filter merekId + pagination)
    public Page<Barang> getListBarang(String search, Long merekId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return barangRepository.findFiltered(search, merekId, pageable);
    }
}
