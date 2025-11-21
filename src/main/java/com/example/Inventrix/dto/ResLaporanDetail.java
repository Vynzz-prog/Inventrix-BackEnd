package com.example.Inventrix.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ResLaporanDetail {

    private Long id;
    private String jenis;
    private String supplier;
    private String createdBy;
    private LocalDateTime tanggal;

    private List<Item> items;

    @Data
    public static class Item {
        private Long id;
        private Long barangId;
        private String namaBarang;
        private Integer jumlah;
        private String keterangan;
    }
}
