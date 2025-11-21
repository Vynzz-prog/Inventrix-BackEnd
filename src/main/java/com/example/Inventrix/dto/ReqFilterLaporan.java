package com.example.Inventrix.dto;

import lombok.Data;

@Data
public class ReqFilterLaporan {

    private String jenis;           // MASUK / HILANG / RUSAK / MUTASI_TOKO
    private String tanggalMulai;    // yyyy-MM-dd
    private String tanggalSelesai;  // yyyy-MM-dd
    private String namaBarang;
    private String createdBy;
    private Integer page = 0;
    private Integer size = 10;
}
