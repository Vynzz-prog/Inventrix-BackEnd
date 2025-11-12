package com.example.Inventrix.controller;

import com.example.Inventrix.model.Barang;
import com.example.Inventrix.model.Merek;
import com.example.Inventrix.service.BarangService;
import com.example.Inventrix.service.MerekService;
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
    private final MerekService merekService;

    public BarangController(BarangService barangService, MerekService merekService) {
        this.barangService = barangService;
        this.merekService = merekService;
    }

    // ✅ Tambah Barang + Upload Gambar (maks 5MB) + relasi ke Merek
    @PreAuthorize("hasRole('OWNER')")
    @PostMapping(value = "/tambah", consumes = {"multipart/form-data"})
    public ResponseEntity<Map<String, Object>> tambahBarang(
            @RequestParam("kodeBarang") String kodeBarang,
            @RequestParam("namaBarang") String namaBarang,
            @RequestParam("merekId") Long merekId,
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

            // ✅ Validasi merek
            Merek merek = merekService.findById(merekId);
            if (merek == null) {
                response.put("pesan", "Merek dengan ID " + merekId + " tidak ditemukan");
                return ResponseEntity.badRequest().body(response);
            }

            // ✅ Upload gambar maksimal 5 MB
            String imageUrl = null;
            if (image != null && !image.isEmpty()) {
                long maxFileSize = 5 * 1024 * 1024;
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

    // ✅ Edit barang + Ganti Foto (OWNER Only)
    @PreAuthorize("hasRole('OWNER')")
    @PutMapping(value = "/edit/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<Map<String, Object>> editBarang(
            @PathVariable Long id,
            @RequestParam(value = "kodeBarang", required = false) String kodeBarang,
            @RequestParam(value = "namaBarang", required = false) String namaBarang,
            @RequestParam(value = "merekId", required = false) Long merekId,
            @RequestParam(value = "hargaBeli", required = false) Double hargaBeli,
            @RequestParam(value = "hargaJual", required = false) Double hargaJual,
            @RequestParam(value = "deskripsi", required = false) String deskripsi,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        Map<String, Object> response = new HashMap<>();

        try {
            Barang barang = barangService.findById(id);
            if (barang == null) {
                response.put("pesan", "Barang tidak ditemukan");
                return ResponseEntity.badRequest().body(response);
            }

            // ✅ Ubah data umum
            if (kodeBarang != null && !kodeBarang.trim().isEmpty())
                barang.setKodeBarang(kodeBarang);
            if (namaBarang != null && !namaBarang.trim().isEmpty())
                barang.setNamaBarang(namaBarang);
            if (deskripsi != null)
                barang.setDeskripsi(deskripsi);
            if (hargaBeli != null && hargaBeli > 0)
                barang.setHargaBeli(hargaBeli);
            if (hargaJual != null && hargaJual > 0)
                barang.setHargaJual(hargaJual);

            // ✅ Ubah merek (jika merekId dikirim)
            if (merekId != null) {
                Merek merekBaru = merekService.findById(merekId);
                if (merekBaru == null) {
                    response.put("pesan", "Merek dengan ID " + merekId + " tidak ditemukan");
                    return ResponseEntity.badRequest().body(response);
                }
                barang.setMerek(merekBaru);
            }

            // ✅ Upload foto baru (jika dikirim)
            if (image != null && !image.isEmpty()) {
                long maxFileSize = 5 * 1024 * 1024;
                if (image.getSize() > maxFileSize) {
                    response.put("pesan", "Ukuran file melebihi batas 5MB");
                    return ResponseEntity.badRequest().body(response);
                }

                // Hapus file lama (jika ada)
                if (barang.getImageUrl() != null) {
                    String oldFilePath = System.getProperty("user.dir") + barang.getImageUrl().replace("/inventrix/barang", "");
                    File oldFile = new File(oldFilePath);
                    if (oldFile.exists()) oldFile.delete();
                }

                // Simpan file baru
                String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();

                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                File destinationFile = new File(uploadDir + fileName);
                image.transferTo(destinationFile);

                barang.setImageUrl("/inventrix/barang/uploads/" + fileName);
            }

            // ✅ Simpan perubahan
            Barang updated = barangService.simpanBarang(barang);

            response.put("pesan", "Data barang berhasil diperbarui");
            response.put("data", updated);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("pesan", "Terjadi kesalahan: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }


    // ✅ Endpoint file upload (gambar)
    @GetMapping("/uploads/{filename}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename, HttpServletRequest request) {
        try {
            Path uploadPath = Paths.get(System.getProperty("user.dir"), "uploads", filename);
            Resource file = new UrlResource(uploadPath.toUri());
            if (!file.exists() || !file.isReadable()) return ResponseEntity.notFound().build();

            String contentType = request.getServletContext().getMimeType(file.getFile().getAbsolutePath());
            if (contentType == null) contentType = "application/octet-stream";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                    .body(file);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // 🔹 Detail barang
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

            Map<String, Object> dto = new HashMap<>();
            dto.put("id", barang.getId());
            dto.put("kodeBarang", barang.getKodeBarang());
            dto.put("namaBarang", barang.getNamaBarang());
            dto.put("merek", barang.getMerek().getNamaMerek());
            dto.put("deskripsi", barang.getDeskripsi());
            dto.put("imageUrl", barang.getImageUrl() != null ? baseUrl + barang.getImageUrl() : null);

            switch (role) {
                case "OWNER" -> {
                    dto.put("hargaBeli", barang.getHargaBeliFormatted());
                    dto.put("hargaJual", barang.getHargaJualFormatted());
                    dto.put("stokToko", barang.getStokToko());
                    dto.put("stokGudang", barang.getStokGudang());
                }
                case "WAREHOUSE" -> dto.put("stokGudang", barang.getStokGudang());
                case "KARYAWAN" -> {
                    dto.put("hargaJual", barang.getHargaJualFormatted());
                    dto.put("stokToko", barang.getStokToko());
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

    // ✅ List barang
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getAllBarang(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long merekId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request
    ) {
        Map<String, Object> response = new HashMap<>();

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String role = auth.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");

            var barangPage = barangService.getListBarang(search, merekId, page, size);
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
                dto.put("merek", barang.getMerek().getNamaMerek());
                dto.put("imageUrl", barang.getImageUrl() != null ? baseUrl + barang.getImageUrl() : null);

                switch (role) {
                    case "OWNER" -> {
                        dto.put("hargaJual", barang.getHargaJualFormatted());
                        dto.put("stokToko", barang.getStokToko());
                        dto.put("stokGudang", barang.getStokGudang());
                    }
                    case "KARYAWAN" -> {
                        dto.put("hargaJual", barang.getHargaJualFormatted());
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

    // 🚨 Handler file terlalu besar
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, Object>> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("pesan", "Ukuran file terlalu besar! Maksimal 5MB.");
        return ResponseEntity.badRequest().body(response);
    }
}
