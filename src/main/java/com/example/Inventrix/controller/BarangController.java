package com.example.Inventrix.controller;

import com.example.Inventrix.model.Barang;
import com.example.Inventrix.service.BarangService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.InetAddress;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inventrix/barang")
@CrossOrigin(origins = "*")
public class BarangController {

    private final BarangService barangService;

    public BarangController(BarangService barangService) {
        this.barangService = barangService;
    }

    // ✅ Tambah Barang + Upload Gambar (maksimal 5MB)
    @PreAuthorize("hasRole('OWNER')")
    @PostMapping(value = "/tambah", consumes = {"multipart/form-data"})
    public ResponseEntity<Map<String, Object>> tambahBarang(
            @RequestParam("kodeBarang") String kodeBarang,
            @RequestParam("namaBarang") String namaBarang,
            @RequestParam("merek") String merek,
            @RequestParam("hargaBeli") Double hargaBeli,
            @RequestParam("hargaJual") Double hargaJual,
            @RequestParam("deskripsi") String deskripsi,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (kodeBarang.trim().isEmpty() || namaBarang.trim().isEmpty()) {
                response.put("pesan", "Kode dan nama barang wajib diisi");
                return ResponseEntity.badRequest().body(response);
            }

            if (hargaBeli == null || hargaBeli <= 0) {
                response.put("pesan", "Harga beli wajib diisi dan harus lebih dari 0");
                return ResponseEntity.badRequest().body(response);
            }

            String imageUrl = null;
            if (image != null && !image.isEmpty()) {

                // 🔒 Validasi ukuran file (maks 5MB)
                long maxFileSize = 5 * 1024 * 1024; // 5MB dalam bytes
                if (image.getSize() > maxFileSize) {
                    response.put("pesan", "Ukuran file melebihi batas 5MB");
                    return ResponseEntity.badRequest().body(response);
                }

                String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();

                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                File destinationFile = new File(uploadDir + fileName);
                image.transferTo(destinationFile);

                // simpan hanya path relatif
                imageUrl = "/inventrix/barang/uploads/" + fileName;
            }

            Barang barang = new Barang();
            barang.setKodeBarang(kodeBarang);
            barang.setNamaBarang(namaBarang);
            barang.setMerek(merek);
            barang.setHargaBeli(hargaBeli);
            barang.setHargaJual(hargaJual);
            barang.setDeskripsi(deskripsi);
            barang.setImageUrl(imageUrl);
            barang.setStokToko(0);
            barang.setStokGudang(0);

            Barang saved = barangService.tambahBarang(barang);

            response.put("pesan", "Barang berhasil ditambahkan");
            response.put("data", saved);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("pesan", "Terjadi kesalahan: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // ✅ Endpoint untuk akses file gambar
    @GetMapping("/uploads/{filename}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename, HttpServletRequest request) {
        try {
            Path uploadPath = Paths.get(System.getProperty("user.dir"), "uploads", filename);
            Resource file = new UrlResource(uploadPath.toUri());

            if (!file.exists() || !file.isReadable()) {
                System.out.println("⚠️ File tidak ditemukan: " + uploadPath);
                return ResponseEntity.notFound().build();
            }

            String contentType = request.getServletContext().getMimeType(file.getFile().getAbsolutePath());
            if (contentType == null) contentType = "application/octet-stream";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                    .body(file);

        } catch (Exception e) {
            System.out.println("❌ ERROR membaca file: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // 🔒 Edit barang
    @PreAuthorize("hasRole('OWNER')")
    @PutMapping("/edit/{id}")
    public ResponseEntity<Map<String, Object>> editBarang(
            @PathVariable Long id,
            @RequestBody com.example.Inventrix.dto.BarangEditDTO dto) {

        Map<String, Object> response = new HashMap<>();

        try {
            Barang barang = barangService.findById(id);
            if (barang == null) {
                response.put("pesan", "Barang tidak ditemukan");
                return ResponseEntity.badRequest().body(response);
            }

            if (dto.getKodeBarang() != null && !dto.getKodeBarang().trim().isEmpty())
                barang.setKodeBarang(dto.getKodeBarang());
            if (dto.getNamaBarang() != null && !dto.getNamaBarang().trim().isEmpty())
                barang.setNamaBarang(dto.getNamaBarang());
            if (dto.getMerek() != null)
                barang.setMerek(dto.getMerek());
            if (dto.getHargaBeli() != null && dto.getHargaBeli() > 0)
                barang.setHargaBeli(dto.getHargaBeli());
            if (dto.getHargaJual() != null && dto.getHargaJual() > 0)
                barang.setHargaJual(dto.getHargaJual());
            if (dto.getDeskripsi() != null)
                barang.setDeskripsi(dto.getDeskripsi());
            if (dto.getImageUrl() != null)
                barang.setImageUrl(dto.getImageUrl());

            Barang saved = barangService.simpanBarang(barang);
            response.put("pesan", "Data barang berhasil diperbarui");
            response.put("data", saved);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("pesan", "Terjadi kesalahan: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 🔹 Detail barang (tambahkan base URL otomatis)
    @GetMapping("/detail/{id}")
    public ResponseEntity<Map<String, Object>> getBarangDetail(@PathVariable Long id, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {
            Barang barang = barangService.findById(id);
            if (barang == null) {
                response.put("pesan", "Barang tidak ditemukan");
                return ResponseEntity.badRequest().body(response);
            }

            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            String baseUrl = request.getScheme() + "://" + hostAddress + ":" + request.getServerPort();

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String role = auth.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");

            com.example.Inventrix.dto.BarangDetailDTO dto = new com.example.Inventrix.dto.BarangDetailDTO();
            dto.setId(barang.getId());
            dto.setNamaBarang(barang.getNamaBarang());
            dto.setMerek(barang.getMerek());
            dto.setDeskripsi(barang.getDeskripsi());

            if (barang.getImageUrl() != null)
                dto.setImageUrl(baseUrl + barang.getImageUrl());

            switch (role) {
                case "OWNER" -> {
                    dto.setKodeBarang(barang.getKodeBarang());
                    dto.setHargaBeli(barang.getHargaBeli());
                    dto.setHargaJual(barang.getHargaJual());
                    dto.setStokToko(barang.getStokToko());
                    dto.setStokGudang(barang.getStokGudang());
                }
                case "WAREHOUSE" -> dto.setStokGudang(barang.getStokGudang());
                case "KARYAWAN" -> {
                    dto.setHargaJual(barang.getHargaJual());
                    dto.setStokToko(barang.getStokToko());
                }
            }

            response.put("pesan", "Data barang ditemukan");
            response.put("data", dto);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("pesan", "Terjadi kesalahan: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // ✅ LIST BARANG — tambahkan base URL untuk image otomatis (IP lokal)
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getAllBarang(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String merek,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request
    ) {
        Map<String, Object> response = new HashMap<>();

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String role = auth.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");

            var barangPage = barangService.getListBarang(search, merek, page, size);
            if (barangPage.isEmpty()) {
                response.put("pesan", "Tidak ada data barang ditemukan");
                response.put("data", List.of());
                return ResponseEntity.ok(response);
            }

            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            String baseUrl = request.getScheme() + "://" + hostAddress + ":" + request.getServerPort();

            var dtoList = barangPage.getContent().stream().map(barang -> {
                Map<String, Object> dto = new HashMap<>();
                dto.put("id", barang.getId());
                dto.put("kodeBarang", barang.getKodeBarang());
                dto.put("namaBarang", barang.getNamaBarang());
                dto.put("merek", barang.getMerek());
                dto.put("imageUrl", barang.getImageUrl() != null ? baseUrl + barang.getImageUrl() : null);

                switch (role) {
                    case "OWNER" -> {
                        dto.put("hargaJual", barang.getHargaJual());
                        dto.put("stokToko", barang.getStokToko());
                        dto.put("stokGudang", barang.getStokGudang());
                    }
                    case "KARYAWAN" -> {
                        dto.put("hargaJual", barang.getHargaJual());
                        dto.put("stokToko", barang.getStokToko());
                    }
                    case "WAREHOUSE" -> dto.put("stokGudang", barang.getStokGudang());
                }

                return dto;
            }).toList();

            response.put("pesan", "Data barang berhasil diambil");
            response.put("data", dtoList);
            response.put("currentPage", barangPage.getNumber());
            response.put("totalPages", barangPage.getTotalPages());
            response.put("totalItems", barangPage.getTotalElements());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("pesan", "Terjadi kesalahan: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 🚨 Handler untuk file terlalu besar
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, Object>> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("pesan", "Ukuran file terlalu besar! Maksimal 5MB.");
        return ResponseEntity.badRequest().body(response);
    }
}
