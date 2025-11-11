package com.example.Inventrix.dto;

import lombok.Data;

@Data
public class BarangEditDTO {
    private String kodeBarang;
    private String namaBarang;
    private String merek;
    private Double hargaBeli;
    private Double hargaJual;
    private String deskripsi;
    private String imageUrl;
}
