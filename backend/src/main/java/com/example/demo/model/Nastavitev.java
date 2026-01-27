package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "nastavitve")
public class Nastavitev {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "nastavitve_key", nullable = false, unique = true)
    private String key;
    
    @Column(name = "nastavitve_atribut")
    private String value;
    
    // Constructors
    public Nastavitev() {
    }
    
    public Nastavitev(String key, String value) {
        this.key = key;
        this.value = value;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getKey() {
        return key;
    }
    
    public void setKey(String key) {
        this.key = key;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
}
