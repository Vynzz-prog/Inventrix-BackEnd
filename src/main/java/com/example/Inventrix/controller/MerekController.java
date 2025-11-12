package com.example.Inventrix.controller;

import com.example.Inventrix.model.Merek;
import com.example.Inventrix.service.MerekService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inventrix/merek")
@CrossOrigin(origins = "*")
public class MerekController {

    private final MerekService merekService;

    public MerekController(MerekService merekService) {
        this.merekService = merekService;
    }

    // ✅ Ambil semua merek
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getAllMerek() {
        Map<String, Object> response = new HashMap<>();
        List<Merek> data = merekService.getAllMerek();

        if (data.isEmpty()) {
            response.put("pesan", "Belum ada merek");
            response.put("data", List.of());
            return ResponseEntity.ok(response);
        }

        response.put("pesan", "Data merek berhasil diambil");
        response.put("data", data);
        return ResponseEntity.ok(response);
    }

    // ✅ Tambah merek (hanya owner)
    @PreAuthorize("hasRole('OWNER')")
    @PostMapping("/tambah")
    public ResponseEntity<Map<String, Object>> tambahMerek(@RequestParam String namaMerek) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (namaMerek == null || namaMerek.trim().isEmpty()) {
                response.put("pesan", "Nama merek tidak boleh kosong");
                return ResponseEntity.badRequest().body(response);
            }

            Merek saved = merekService.tambahMerek(namaMerek.trim());
            response.put("pesan", "Merek berhasil ditambahkan");
            response.put("data", saved);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("pesan", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // ✅ Edit merek (hanya owner)
    @PreAuthorize("hasRole('OWNER')")
    @PutMapping("/edit/{id}")
    public ResponseEntity<Map<String, Object>> editMerek(@PathVariable Long id, @RequestParam String namaBaru) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (namaBaru == null || namaBaru.trim().isEmpty()) {
                response.put("pesan", "Nama baru tidak boleh kosong");
                return ResponseEntity.badRequest().body(response);
            }

            Merek updated = merekService.editMerek(id, namaBaru.trim());
            response.put("pesan", "Merek berhasil diperbarui");
            response.put("data", updated);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("pesan", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // ✅ Hapus merek (hanya owner)
    @PreAuthorize("hasRole('OWNER')")
    @DeleteMapping("/hapus/{id}")
    public ResponseEntity<Map<String, Object>> hapusMerek(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            merekService.hapusMerek(id);
            response.put("pesan", "Merek berhasil dihapus");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("pesan", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
