package com.example.Inventrix.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ResLaporanList {
    private Long id;
    private String jenis;
    private String supplier;
    private String createdBy;
    private LocalDateTime tanggal;
    private Integer totalItem; // agar pemilik tahu jumlah barang
}
