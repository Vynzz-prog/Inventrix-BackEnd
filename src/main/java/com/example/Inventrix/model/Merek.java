package com.example.Inventrix.model;

import jakarta.persistence.*;

@Entity
@Table(name = "merek")
public class Merek {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String namaMerek;

    public Merek() {
    }

    public Merek(String namaMerek) {
        this.namaMerek = namaMerek;
    }

    public Long getId() {
        return id;
    }

    public String getNamaMerek() {
        return namaMerek;
    }

    public void setNamaMerek(String namaMerek) {
        this.namaMerek = namaMerek;
    }
}
