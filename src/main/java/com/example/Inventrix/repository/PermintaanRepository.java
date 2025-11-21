package com.example.Inventrix.repository;

import com.example.Inventrix.model.PermintaanToko;
import com.example.Inventrix.model.PermintaanToko.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermintaanRepository extends JpaRepository<PermintaanToko, Long> {
    List<PermintaanToko> findByStatus(Status status);
}
