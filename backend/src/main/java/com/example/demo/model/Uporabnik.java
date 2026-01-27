package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "uporabniki")
public class Uporabnik {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "uporabnisko_ime", nullable = false, unique = true, length = 100)
    private String uporabnikoIme;
    
    @Column(name = "geslo", nullable = false)
    private String geslo;
    
    @Column(name = "vloga", nullable = false, length = 20)
    private String vloga;
    
    // Constructors
    public Uporabnik() {
    }
    
    public Uporabnik(String uporabnikoIme, String geslo, String vloga) {
        this.uporabnikoIme = uporabnikoIme;
        this.geslo = geslo;
        this.vloga = vloga;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUporabnikoIme() {
        return uporabnikoIme;
    }
    
    public void setUporabnikoIme(String uporabnikoIme) {
        this.uporabnikoIme = uporabnikoIme;
    }
    
    public String getGeslo() {
        return geslo;
    }
    
    public void setGeslo(String geslo) {
        this.geslo = geslo;
    }
    
    public String getVloga() {
        return vloga;
    }
    
    public void setVloga(String vloga) {
        this.vloga = vloga;
    }
}
