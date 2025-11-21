package com.example.Inventrix.controller;

import com.example.Inventrix.dto.ReqCreateLaporan;
import com.example.Inventrix.dto.ReqFilterLaporan;
import com.example.Inventrix.dto.ResLaporanDetail;
import com.example.Inventrix.dto.ResLaporanList;
import com.example.Inventrix.dto.ResPesan;
import com.example.Inventrix.service.LaporanService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventrix/laporan")
public class LaporanController {

    private final LaporanService laporanService;

    // =====================================================
    // OWNER BUAT LAPORAN MANUAL (MASUK / HILANG / RUSAK)
    // =====================================================
    @PreAuthorize("hasRole('OWNER')")
    @PostMapping("/create")
    public ResPesan create(@RequestBody ReqCreateLaporan req) {
        var laporan = laporanService.createLaporan(req);
        return new ResPesan("Laporan berhasil dibuat, ID = " + laporan.getId());
    }

    // =====================================================
    // OWNER MELIHAT LIST LAPORAN (FILTER)
    // return Page<ResLaporanList> (DTO RINGKAS)
    // =====================================================
    @PreAuthorize("hasRole('OWNER')")
    @PostMapping("/list")
    public Page<ResLaporanList> listLaporan(@RequestBody ReqFilterLaporan req) {
        return laporanService.filterLaporan(req);
    }

    // =====================================================
    // OWNER MELIHAT DETAIL LAPORAN BY ID
    // return DTO DETAIL aman
    // =====================================================
    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("/detail/{id}")
    public ResLaporanDetail detail(@PathVariable Long id) {
        return laporanService.getLaporanDetail(id);
    }
}
