package com.example.Inventrix.dto;

import lombok.Data;

@Data
public class ReqPermintaan {
    private Long barangId;
    private Integer jumlah;
    private String keterangan; // optional
}
