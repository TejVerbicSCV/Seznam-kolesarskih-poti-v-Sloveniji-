package com.example.demo.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Set;

@Entity
@Table(name = "kraji")
public class Kraj {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "ime", nullable = false, length = 50)
    private String ime;
    
    @Column(name = "regija", nullable = false, length = 50)
    private String regija;
    
    @Column(name = "postna_stavilka", nullable = false)
    private Long postnaStavilka;
    
    @JsonIgnore
    @ManyToMany(mappedBy = "kraji")
    private Set<KolesarskaPot> kolesarskePoti;
    
    // Constructors
    public Kraj() {
    }
    
    public Kraj(String ime, String regija, Long postnaStavilka) {
        this.ime = ime;
        this.regija = regija;
        this.postnaStavilka = postnaStavilka;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getIme() {
        return ime;
    }
    
    public void setIme(String ime) {
        this.ime = ime;
    }
    
    public String getRegija() {
        return regija;
    }
    
    public void setRegija(String regija) {
        this.regija = regija;
    }
    
    public Long getPostnaStavilka() {
        return postnaStavilka;
    }
    
    public void setPostnaStavilka(Long postnaStavilka) {
        this.postnaStavilka = postnaStavilka;
    }
    
    public Set<KolesarskaPot> getKolesarskePoti() {
        return kolesarskePoti;
    }
    
    public void setKolesarskePoti(Set<KolesarskaPot> kolesarskePoti) {
        this.kolesarskePoti = kolesarskePoti;
    }
}

