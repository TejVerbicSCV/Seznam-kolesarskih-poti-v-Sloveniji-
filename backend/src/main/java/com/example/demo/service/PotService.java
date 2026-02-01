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
        String krajiIdsStr = null;
        if (potDTO.krajiIds != null && !potDTO.krajiIds.isEmpty()) {
            krajiIdsStr = "{" + String.join(",", potDTO.krajiIds.stream().map(String::valueOf).toArray(String[]::new)) + "}";
        }

        String znamenitostiIdsStr = null;
        if (potDTO.znamenitostiIds != null && !potDTO.znamenitostiIds.isEmpty()) {
            znamenitostiIdsStr = "{" + String.join(",", potDTO.znamenitostiIds.stream().map(String::valueOf).toArray(String[]::new)) + "}";
        }

        potRepository.vnosPoti(
            potDTO.ime,
            potDTO.dolzinaKm,
            potDTO.tezavnost,
            potDTO.priporocenCas,
            potDTO.opis,
            potDTO.uporabnikId,
            krajiIdsStr,
            znamenitostiIdsStr
        );
    }

    // B) Urejanje poti
    public void urediPot(Long id, PotDTO potDTO) {
        String krajiIdsStr = null;
        if (potDTO.krajiIds != null && !potDTO.krajiIds.isEmpty()) {
            krajiIdsStr = "{" + String.join(",", potDTO.krajiIds.stream().map(String::valueOf).toArray(String[]::new)) + "}";
        }

        String znamenitostiIdsStr = null;
        if (potDTO.znamenitostiIds != null && !potDTO.znamenitostiIds.isEmpty()) {
            znamenitostiIdsStr = "{" + String.join(",", potDTO.znamenitostiIds.stream().map(String::valueOf).toArray(String[]::new)) + "}";
        }

        System.out.println("Service urediPot - krajiIdsStr: " + krajiIdsStr);
        System.out.println("Service urediPot - znamenitostiIdsStr: " + znamenitostiIdsStr);

        potRepository.urediPot(
            id,
            potDTO.ime,
            potDTO.dolzinaKm,
            potDTO.tezavnost,
            potDTO.priporocenCas,
            potDTO.opis,
            krajiIdsStr,
            znamenitostiIdsStr
        );
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
        return potRepository.pridobiVsePoti();
    }

    // Pridobi pot po ID
    public Optional<KolesarskaPot> pridobiPot(Long id) {
        return Optional.ofNullable(potRepository.pridobiPotPoId(id));
    }
}
