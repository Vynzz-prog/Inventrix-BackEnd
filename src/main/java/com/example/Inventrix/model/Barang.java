package com.example.Inventrix.model;

import jakarta.persistence.*;
import lombok.Data;
import java.text.DecimalFormat;

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

    // 🔹 Relasi ke tabel merek (ManyToOne)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "merek_id", nullable = false)
    private Merek merek;

    // 🔹 Harga beli wajib
    @Column(nullable = false)
    private Double hargaBeli = 0.0;

    // 🔹 Harga jual opsional
    private Double hargaJual = 0.0;

    // 🔹 Stok toko dan gudang
    @Column(nullable = false)
    private Integer stokToko = 0;

    @Column(nullable = false)
    private Integer stokGudang = 0;

    // 🔹 Deskripsi barang
    @Column(length = 500)
    private String deskripsi;

    // 🔹 URL gambar barang
    private String imageUrl;

    // ===============================
    // 🔹 Getter tambahan untuk format harga (3.000.000)
    // ===============================
    public String getHargaBeliFormatted() {
        return formatHarga(hargaBeli);
    }

    public String getHargaJualFormatted() {
        return formatHarga(hargaJual);
    }

    // 🔹 Utility untuk format angka ke bentuk "3.000.000"
    private String formatHarga(Double value) {
        if (value == null) return "0";
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(value).replace(",", ".");
    }
}
