package com.example.Inventrix.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "laporan")
public class Laporan {

    public enum Jenis { MASUK, HILANG, RUSAK }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Jenis jenis;

    private String supplier;

    private String createdBy;

    private LocalDateTime tanggal = LocalDateTime.now();

    @OneToMany(mappedBy = "laporan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LaporanItem> items = new ArrayList<>();

    public void addItem(LaporanItem item) {
        this.items.add(item);
        item.setLaporan(this);
    }
}
