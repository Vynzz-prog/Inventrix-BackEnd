package com.example.Inventrix.controller;

import com.example.Inventrix.dto.ReqPermintaan;
import com.example.Inventrix.dto.ResPesan;
import com.example.Inventrix.model.PermintaanToko;
import com.example.Inventrix.service.PermintaanService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventrix/permintaan")
public class PermintaanController {

    private final PermintaanService permintaanService;

    // =====================================================
    // OWNER - MEMBUAT PERMINTAAN
    // =====================================================
    @PreAuthorize("hasRole('OWNER')")
    @PostMapping("/create")
    public ResPesan create(@RequestBody ReqPermintaan req) {
        var p = permintaanService.buatPermintaan(req);
        return new ResPesan("Permintaan dibuat, ID = " + p.getId());
    }

    // =====================================================
    // OWNER - MELIHAT PERMINTAAN (STATUS DIPINTA)
    // =====================================================
    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("/owner/diminta")
    public List<PermintaanToko> listOwnerDiminta() {
        return permintaanService.listByStatus(PermintaanToko.Status.DIPINTA);
    }

    // =====================================================
    // WAREHOUSE - MELIHAT PERMINTAAN (STATUS DIPINTA)
    // =====================================================
    @PreAuthorize("hasRole('WAREHOUSE')")
    @GetMapping("/warehouse/diminta")
    public List<PermintaanToko> listGudangDiminta() {
        return permintaanService.listByStatus(PermintaanToko.Status.DIPINTA);
    }

    // =====================================================
    // WAREHOUSE - MENEKAN “KIRIM”
    // =====================================================
    @PreAuthorize("hasRole('WAREHOUSE')")
    @PostMapping("/kirim/{id}")
    public ResPesan kirim(@PathVariable Long id) {
        var p = permintaanService.kirimPermintaan(id);
        return new ResPesan("Permintaan ID " + p.getId() + " telah dikirim ke toko");
    }

    // =====================================================
    // OWNER & WAREHOUSE - MELIHAT PERMINTAAN DIKIRIM
    // =====================================================
    @PreAuthorize("hasAnyRole('OWNER','WAREHOUSE')")
    @GetMapping("/dikirim")
    public List<PermintaanToko> listDikirim() {
        return permintaanService.listByStatus(PermintaanToko.Status.DIKIRIM);
    }

    // =====================================================
    // OWNER - KONFIRMASI SELESAI
    // =====================================================
    @PreAuthorize("hasRole('OWNER')")
    @PostMapping("/selesai/{id}")
    public ResPesan selesai(@PathVariable Long id) {
        var p = permintaanService.selesaiPermintaan(id);
        return new ResPesan("Permintaan ID " + p.getId() + " selesai. Stok toko telah diperbarui.");
    }

    // =====================================================
    // OWNER - BATALKAN PERMINTAAN
    // =====================================================
    @PreAuthorize("hasRole('OWNER')")
    @PostMapping("/batal/{id}")
    public ResPesan batal(@PathVariable Long id) {
        var p = permintaanService.batalPermintaan(id);
        return new ResPesan("Permintaan ID " + p.getId() + " dibatalkan.");
    }
}
