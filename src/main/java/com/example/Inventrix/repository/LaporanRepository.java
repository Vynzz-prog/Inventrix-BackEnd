package com.example.Inventrix.repository;

import com.example.Inventrix.model.Laporan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface LaporanRepository extends JpaRepository<Laporan, Long> {

    @Query("""
        SELECT DISTINCT l FROM Laporan l
        JOIN l.items i
        WHERE (:jenis IS NULL OR l.jenis = :jenis)
          AND (:createdBy IS NULL OR LOWER(l.createdBy) LIKE LOWER(CONCAT('%', :createdBy, '%')))
          AND (:namaBarang IS NULL OR LOWER(i.barang.namaBarang) LIKE LOWER(CONCAT('%', :namaBarang, '%')))
          AND (:tglMulai IS NULL OR l.tanggal >= :tglMulai)
          AND (:tglSelesai IS NULL OR l.tanggal <= :tglSelesai)
        ORDER BY l.tanggal DESC
        """)
    Page<Laporan> filterLaporan(
            @Param("jenis") Laporan.Jenis jenis,
            @Param("createdBy") String createdBy,
            @Param("namaBarang") String namaBarang,
            @Param("tglMulai") LocalDateTime tglMulai,
            @Param("tglSelesai") LocalDateTime tglSelesai,
            Pageable pageable
    );
}
