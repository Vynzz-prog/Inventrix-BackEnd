package com.example.Inventrix.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "permintaan_toko")
public class PermintaanToko {

    public enum Status { DIPINTA, DIKIRIM, SELESAI, BATAL }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // barang yang diminta
    @ManyToOne
    @JoinColumn(name = "barang_id", nullable = false)
    private Barang barang;

    // jumlah yang diminta
    @Column(nullable = false)
    private Integer jumlah;

    // dapat diisi jika admin mau (opsional)
    private String keterangan;

    // status permintaan
    @Enumerated(EnumType.STRING)
    private Status status = Status.DIPINTA;

    // siapa membuat permintaan (admin)
    private String createdBy;

    // siapa memproses (gudang) saat klik KIRIM
    private String processedBy;

    // siapa mengkonfirmasi selesai (admin toko)
    private String completedBy;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime processedAt;
    private LocalDateTime completedAt;
}
