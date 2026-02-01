package com.example.demo.controller;

import com.example.demo.model.KolesarskaPot;
import com.example.demo.service.PotService;
import com.example.demo.dto.PotDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/poti")

public class PotController {

    @Autowired
    private PotService potService;

    // A) Vnos nove poti s krajem (osnovni)
    @PostMapping("/vnos-s-krajem")
    public ResponseEntity<?> vnosPotiSKrajem(@RequestBody PotDTO potDTO) {
        try {
            potService.vnosPotiSKrajem(
                potDTO.ime,
                potDTO.dolzinaKm,
                potDTO.tezavnost,
                potDTO.priporocenCas,
                potDTO.opis,
                potDTO.uporabnikId,
                potDTO.krajiIds.get(0)
            );
            Map<String, String> response = new HashMap<>();
            response.put("message", "Pot uspešno vnesena");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("message", "Napaka pri vnosu poti: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // I) Kompleksen vnos poti (s krajem in znamenitostmi)
    @PostMapping("/vnos")
    public ResponseEntity<?> vnosPoti(@RequestBody PotDTO potDTO) {
        try {
            potService.vnosPoti(potDTO);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Pot uspešno vnesena");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("message", "Napaka pri vnosu poti: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // B) Urejanje poti
    @PutMapping("/{id}")
    public ResponseEntity<?> urediPot(@PathVariable Long id, @RequestBody PotDTO potDTO) {
        try {
            potService.urediPot(id, potDTO);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Pot uspešno urejena");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Napaka pri urejanju poti (ID: " + id + "): " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("message", "Napaka pri urejanju poti: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // C) Varno brisanje poti (le ADMIN)
    @DeleteMapping("/{potId}")
    public ResponseEntity<?> izbrisiPotVarna(@PathVariable Long potId,
                                             @RequestParam Long izvajalecId) {
        try {
            potService.izbrisiPotVarna(potId, izvajalecId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Pot uspešno izbrisana");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("message", "Napaka pri brisanju poti: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // G) Filtriranje po tezavnosti
    @GetMapping("/filter/tezavnost/{tezavnost}")
    public ResponseEntity<?> filtrirajPoTezavnosti(@PathVariable String tezavnost) {
        try {
            List<KolesarskaPot> poti = potService.filtrirajPoTezavnosti(tezavnost);
            return ResponseEntity.ok(poti);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("message", "Napaka pri filtriranju: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Pridobi vse poti
    @GetMapping
    public ResponseEntity<?> pridobiVsePoti() {
        try {
            List<KolesarskaPot> poti = potService.pridobiVsePoti();
            return ResponseEntity.ok(poti);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("message", "Napaka pri pridobivanju poti: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Pridobi pot po ID
    @GetMapping("/{id}")
    public ResponseEntity<?> pridobiPot(@PathVariable Long id) {
        try {
            Optional<KolesarskaPot> pot = potService.pridobiPot(id);
            if (pot.isPresent()) {
                return ResponseEntity.ok(pot.get());
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Pot ni najdena");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("message", "Napaka pri pridobivanju poti: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
