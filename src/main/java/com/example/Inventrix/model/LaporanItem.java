package com.example.Inventrix.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "laporan_item")
public class LaporanItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "laporan_id")
    private Laporan laporan;

    @ManyToOne
    @JoinColumn(name = "barang_id", nullable = false)
    private Barang barang;

    @Column(nullable = false)
    private Integer jumlah;

    private String keterangan;
}
