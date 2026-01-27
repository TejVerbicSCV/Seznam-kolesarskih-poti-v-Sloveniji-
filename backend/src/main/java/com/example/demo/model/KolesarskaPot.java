package com.example.demo.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Set;

@Entity
@Table(name = "kolesarske_poti")
public class KolesarskaPot {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "ime", nullable = false)
    private String ime;
    
    @Column(name = "dolzina_km", nullable = false)
    private Float dolzinaKm;
    
    @Column(name = "tezavnost", nullable = false)
    private String tezavnost;
    
    @Column(name = "priporocen_cas", nullable = false)
    private String priporocenCas;
    
    @Column(name = "opis", columnDefinition = "TEXT")
    private String opis;
    
    @Column(name = "uporabnik_id", nullable = false)
    private Long uporabnikId;
    
    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "kolesarske_poti_kraji",
        joinColumns = @JoinColumn(name = "kolesarska_pot_id"),
        inverseJoinColumns = @JoinColumn(name = "kraj_id")
    )
    private Set<Kraj> kraji;
    
    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "kolesarske_poti_znamenitosti",
        joinColumns = @JoinColumn(name = "kolesarska_pot_id"),
        inverseJoinColumns = @JoinColumn(name = "znamenitost_id")
    )
    private Set<Zanimivost> zanimivosti;
    
    // Constructors
    public KolesarskaPot() {
    }
    
    public KolesarskaPot(String ime, Float dolzinaKm, String tezavnost, 
                        String priporocenCas, String opis, Long uporabnikId) {
        this.ime = ime;
        this.dolzinaKm = dolzinaKm;
        this.tezavnost = tezavnost;
        this.priporocenCas = priporocenCas;
        this.opis = opis;
        this.uporabnikId = uporabnikId;
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
    
    public Float getDolzinaKm() {
        return dolzinaKm;
    }
    
    public void setDolzinaKm(Float dolzinaKm) {
        this.dolzinaKm = dolzinaKm;
    }
    
    public String getTezavnost() {
        return tezavnost;
    }
    
    public void setTezavnost(String tezavnost) {
        this.tezavnost = tezavnost;
    }
    
    public String getPriporocenCas() {
        return priporocenCas;
    }
    
    public void setPriporocenCas(String priporocenCas) {
        this.priporocenCas = priporocenCas;
    }
    
    public String getOpis() {
        return opis;
    }
    
    public void setOpis(String opis) {
        this.opis = opis;
    }
    
    public Long getUporabnikId() {
        return uporabnikId;
    }
    
    public void setUporabnikId(Long uporabnikId) {
        this.uporabnikId = uporabnikId;
    }
    
    public Set<Kraj> getKraji() {
        return kraji;
    }
    
    public void setKraji(Set<Kraj> kraji) {
        this.kraji = kraji;
    }
    
    public Set<Zanimivost> getZanimivosti() {
        return zanimivosti;
    }
    
    public void setZanimivosti(Set<Zanimivost> zanimivosti) {
        this.zanimivosti = zanimivosti;
    }
}
