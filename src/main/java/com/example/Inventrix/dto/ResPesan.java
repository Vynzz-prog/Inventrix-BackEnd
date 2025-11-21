package com.example.Inventrix.dto;

import lombok.Data;

@Data
public class ResPesan {
    private String pesan;

    public ResPesan(String pesan) {
        this.pesan = pesan;
    }
}
