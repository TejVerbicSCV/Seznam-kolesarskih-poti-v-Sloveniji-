package com.example.demo.controller;

import com.example.demo.model.KolesarskaPot;
import com.example.demo.service.PotService;
import com.example.demo.dto.PotDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/poti")
@CrossOrigin(origins = "*")
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
            return ResponseEntity.status(HttpStatus.CREATED).body("Pot uspešno vnesena");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Napaka pri vnosu poti: " + e.getMessage());
        }
    }

    // I) Kompleksen vnos poti (s krajem in znamenitostmi)
    @PostMapping("/vnos")
    public ResponseEntity<?> vnosPoti(@RequestBody PotDTO potDTO) {
        try {
            potService.vnosPoti(potDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Pot uspešno vnesena");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Napaka pri vnosu poti: " + e.getMessage());
        }
    }

    // B) Urejanje poti
    @PutMapping("/{id}")
    public ResponseEntity<?> urediPot(@PathVariable Long id, @RequestBody PotDTO potDTO) {
        try {
            potService.urediPot(
                id,
                potDTO.ime,
                potDTO.dolzinaKm,
                potDTO.tezavnost,
                potDTO.priporocenCas,
                potDTO.opis
            );
            return ResponseEntity.ok("Pot uspešno urejena");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Napaka pri urejanju poti: " + e.getMessage());
        }
    }

    // C) Varno brisanje poti (le ADMIN)
    @DeleteMapping("/{potId}")
    public ResponseEntity<?> izbrisiPotVarna(@PathVariable Long potId,
                                             @RequestParam Long izvajalecId) {
        try {
            potService.izbrisiPotVarna(potId, izvajalecId);
            return ResponseEntity.ok("Pot uspešno izbrisana");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Napaka pri brisanju poti: " + e.getMessage());
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Napaka pri filtriranju: " + e.getMessage());
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Napaka pri pridobivanju poti: " + e.getMessage());
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
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Pot ni najdena");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Napaka pri pridobivanju poti: " + e.getMessage());
        }
    }
}
