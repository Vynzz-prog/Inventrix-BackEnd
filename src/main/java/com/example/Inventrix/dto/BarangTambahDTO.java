package com.example.Inventrix.dto;

import lombok.Data;

@Data
public class BarangTambahDTO {
    private String kodeBarang;
    private String namaBarang;
    private String merek;
    private Double hargaBeli;
    private Double hargaJual;
    private Integer stok;
    private String deskripsi;
    private String imageUrl;
}
