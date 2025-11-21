package com.example.Inventrix.dto;

import lombok.Data;
import java.util.List;

@Data
public class ReqCreateLaporan {

    private String jenis; // "MASUK", "HILANG", "RUSAK"
    private String supplier;
    private String createdBy;

    private List<ReqLaporanItem> items;
}
