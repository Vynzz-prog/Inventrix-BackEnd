package com.example.Inventrix.dto;

import lombok.Data;

@Data
public class ReqLaporanItem {
    private Long barangId;
    private Integer jumlah;
    private String keterangan;
}
