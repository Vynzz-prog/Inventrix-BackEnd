package com.example.Inventrix.repository;

import com.example.Inventrix.model.Merek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerekRepository extends JpaRepository<Merek, Long> {
    boolean existsByNamaMerekIgnoreCase(String namaMerek);
}
