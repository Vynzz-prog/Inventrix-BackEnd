package com.example.Inventrix.service;

import com.example.Inventrix.model.Merek;
import com.example.Inventrix.repository.MerekRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MerekService {

    private final MerekRepository merekRepository;

    public MerekService(MerekRepository merekRepository) {
        this.merekRepository = merekRepository;
    }

    public List<Merek> getAllMerek() {
        return merekRepository.findAll(Sort.by("namaMerek").ascending());
    }

    public Merek tambahMerek(String namaMerek) {
        if (merekRepository.existsByNamaMerekIgnoreCase(namaMerek)) {
            throw new RuntimeException("Merek sudah ada");
        }
        return merekRepository.save(new Merek(namaMerek));
    }

    public Merek editMerek(Long id, String namaBaru) {
        Optional<Merek> optional = merekRepository.findById(id);
        if (optional.isEmpty()) {
            throw new RuntimeException("Merek tidak ditemukan");
        }

        Merek merek = optional.get();
        merek.setNamaMerek(namaBaru);
        return merekRepository.save(merek);
    }

    public void hapusMerek(Long id) {
        if (!merekRepository.existsById(id)) {
            throw new RuntimeException("Merek tidak ditemukan");
        }
        merekRepository.deleteById(id);
    }

    // ✅ Tambahkan ini
    public Merek findById(Long id) {
        return merekRepository.findById(id).orElse(null);
    }
}
