package com.example.Inventrix.controller;

import com.example.Inventrix.dto.ReqCreateLaporan;
import com.example.Inventrix.dto.ResPesan;
import com.example.Inventrix.service.LaporanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventrix/laporan")
public class LaporanController {

    private final LaporanService laporanService;

    @PostMapping("/create")
    public ResPesan create(@RequestBody ReqCreateLaporan req) {
        var laporan = laporanService.createLaporan(req);
        return new ResPesan("Laporan berhasil dibuat, ID = " + laporan.getId());
    }
}
