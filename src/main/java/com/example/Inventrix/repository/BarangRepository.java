package com.example.Inventrix.repository;

import com.example.Inventrix.model.Barang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BarangRepository extends JpaRepository<Barang, Long> {

    boolean existsByKodeBarang(String kodeBarang);

    // 🔍 Custom query untuk search + filter merek + pagination
    @Query("""
        SELECT b FROM Barang b
        WHERE (:search IS NULL OR LOWER(b.namaBarang) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(b.kodeBarang) LIKE LOWER(CONCAT('%', :search, '%')))
          AND (:merek IS NULL OR LOWER(b.merek) = LOWER(:merek))
    """)
    Page<Barang> findFiltered(String search, String merek, Pageable pageable);
}
