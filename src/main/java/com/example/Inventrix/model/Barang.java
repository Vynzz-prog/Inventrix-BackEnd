package com.example.Inventrix.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "barang")
public class Barang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔹 Kode barang wajib & unik
    @Column(unique = true, nullable = false)
    private String kodeBarang;

    // 🔹 Nama barang wajib
    @Column(nullable = false)
    private String namaBarang;

    // 🔹 Boleh kosong
    private String merek;

    // 🔹 Harga beli wajib, tidak boleh null atau negatif
    @Column(nullable = false)
    private Double hargaBeli = 0.0;

    // 🔹 Harga jual opsional
    private Double hargaJual = 0.0;

    // 🔹 Stok wajib (default 0)
    @Column(nullable = false)
    private Integer stokToko = 0;

    @Column(nullable = false)
    private Integer stokGudang = 0;

    // 🔹 Deskripsi maksimal 500 karakter
    @Column(length = 500)
    private String deskripsi;

    // 🔹 Foto barang opsional
    private String imageUrl;
}
