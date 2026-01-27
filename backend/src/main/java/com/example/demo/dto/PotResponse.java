package com.example.demo.dto;

import java.util.List;

public class PotResponse {
    public Long id;
    public String ime;
    public Float dolzinaKm;
    public String tezavnost;
    public String priporocenCas;
    public String opis;
    public Long uporabnikId;
    public List<KrajInfo> kraji;
    public List<ZanimivostInfo> zanimivosti;
    
    public static class KrajInfo {
        public String ime;
        public String regija;
    }
    
    public static class ZanimivostInfo {
        public Long id;
        public String ime;
        public String opis;
    }
}

