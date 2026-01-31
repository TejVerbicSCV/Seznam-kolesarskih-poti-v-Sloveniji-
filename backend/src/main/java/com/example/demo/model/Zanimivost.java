package com.example.demo.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Set;

@Entity
@Table(name = "zanimivosti")
public class Zanimivost {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "ime", nullable = false, length = 100)
    private String ime;
    
    @Column(name = "opis", columnDefinition = "TEXT")
    private String opis;
    
    @JsonIgnore
    @ManyToMany(mappedBy = "zanimivosti")
    private Set<KolesarskaPot> kolesarskePoti;
    
    // Constructors
    public Zanimivost() {
    }
    
    public Zanimivost(String ime, String opis) {
        this.ime = ime;
        this.opis = opis;
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
    
    public String getOpis() {
        return opis;
    }
    
    public void setOpis(String opis) {
        this.opis = opis;
    }
    
    public Set<KolesarskaPot> getKolesarskePoti() {
        return kolesarskePoti;
    }
    
    public void setKolesarskePoti(Set<KolesarskaPot> kolesarskePoti) {
        this.kolesarskePoti = kolesarskePoti;
    }
}
