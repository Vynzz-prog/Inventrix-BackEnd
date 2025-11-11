package com.example.Inventrix.dto;

import lombok.Data;

@Data
public class BarangDetailDTO {
    private Long id;
    private String kodeBarang;
    private String namaBarang;
    private String merek;
    private Double hargaBeli;
    private Double hargaJual;
    private Integer stokToko;
    private Integer stokGudang;
    private String deskripsi;
    private String imageUrl;
}
