package com.example.Inventrix.repository;

import com.example.Inventrix.model.Barang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BarangRepository extends JpaRepository<Barang, Long> {

    boolean existsByKodeBarang(String kodeBarang);

    // 🔍 Cari berdasarkan nama/kode barang (tanpa filter merek)
    @Query("""
        SELECT b FROM Barang b
        WHERE (:search IS NULL OR LOWER(b.namaBarang) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(b.kodeBarang) LIKE LOWER(CONCAT('%', :search, '%')))
    """)
    Page<Barang> findBySearch(@Param("search") String search, Pageable pageable);

    // 🔍 Filter hanya berdasarkan merek ID
    @Query("""
        SELECT b FROM Barang b
        WHERE (:merekId IS NULL OR b.merek.id = :merekId)
    """)
    Page<Barang> findByMerekId(@Param("merekId") Long merekId, Pageable pageable);

    // 🔍 Kombinasi search + filter merek ID
    @Query("""
        SELECT b FROM Barang b
        WHERE (:search IS NULL OR LOWER(b.namaBarang) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(b.kodeBarang) LIKE LOWER(CONCAT('%', :search, '%')))
          AND (:merekId IS NULL OR b.merek.id = :merekId)
    """)
    Page<Barang> findFiltered(@Param("search") String search,
                              @Param("merekId") Long merekId,
                              Pageable pageable);
}
