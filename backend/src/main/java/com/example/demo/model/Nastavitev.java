package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "nastavitve")
public class Nastavitev {
    
    @Id
    @Column(name = "nastavitve_key", nullable = false, length = 50)
    private String key;
    
    @Column(name = "nastavitve_atribut", length = 100)
    private String value;
    
    // Constructors
    public Nastavitev() {
    }
    
    public Nastavitev(String key, String value) {
        this.key = key;
        this.value = value;
    }
    
    // Getters and Setters
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
