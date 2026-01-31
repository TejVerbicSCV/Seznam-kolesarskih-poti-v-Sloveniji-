package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "nastavitve")
public class Nastavitev {
    
    @Id
<<<<<<< Updated upstream
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "nastavitve_key", nullable = false, unique = true)
    private String key;
    
    @Column(name = "nastavitve_atribut")
=======
    @Column(name = "nastavitve_key", nullable = false, length = 50)
    private String key;
    
    @Column(name = "nastavitve_atribut", length = 100)
>>>>>>> Stashed changes
    private String value;
    
    // Constructors
    public Nastavitev() {
    }
    
    public Nastavitev(String key, String value) {
        this.key = key;
        this.value = value;
    }
    
    // Getters and Setters
<<<<<<< Updated upstream
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
=======
>>>>>>> Stashed changes
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
