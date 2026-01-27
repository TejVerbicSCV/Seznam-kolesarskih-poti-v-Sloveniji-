package com.example.demo.service;

import com.example.demo.model.KolesarskaPot;
import com.example.demo.repository.PotRepository;
import com.example.demo.dto.PotDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PotService {

    @Autowired
    private PotRepository potRepository;

    // A) Vnos nove poti s krajem (osnovni)
    public void vnosPotiSKrajem(String ime, Float dolzina, String tezavnost,
                                 String cas, String opis, Long uporabnikId, Long krajId) {
        potRepository.vnosPotiSKrajem(ime, dolzina, tezavnost, cas, opis, uporabnikId, krajId);
    }

    // I) Kompleksen vnos - s krajem in znamenitostmi
    public void vnosPoti(PotDTO potDTO) {
        long[] krajiIds = potDTO.krajiIds != null ? 
            potDTO.krajiIds.stream().mapToLong(Long::longValue).toArray() : null;
        long[] znamenitostiIds = potDTO.znamenitostiIds != null ? 
            potDTO.znamenitostiIds.stream().mapToLong(Long::longValue).toArray() : null;

        potRepository.vnosPoti(
            potDTO.ime,
            potDTO.dolzinaKm,
            potDTO.tezavnost,
            potDTO.priporocenCas,
            potDTO.opis,
            potDTO.uporabnikId,
            krajiIds,
            znamenitostiIds
        );
    }

    // B) Urejanje poti
    public void urediPot(Long id, String ime, Float dolzina, String tezavnost,
                         String cas, String opis) {
        potRepository.urediPot(id, ime, dolzina, tezavnost, cas, opis);
    }

    // C) Varno brisanje poti (le ADMIN)
    public void izbrisiPotVarna(Long potId, Long izvajalecId) {
        potRepository.izbrisiPotVarna(potId, izvajalecId);
    }

    // G) Filtriranje po tezavnosti
    public List<KolesarskaPot> filtrirajPoTezavnosti(String tezavnost) {
        return potRepository.filtrirajPoTezavnosti(tezavnost);
    }

    // Pridobi vse poti
    public List<KolesarskaPot> pridobiVsePoti() {
        return potRepository.findAll();
    }

    // Pridobi pot po ID
    public Optional<KolesarskaPot> pridobiPot(Long id) {
        return potRepository.findById(id);
    }
}
