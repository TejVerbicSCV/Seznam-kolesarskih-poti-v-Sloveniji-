package com.example.demo.controller;

import com.example.demo.model.Kraj;
import com.example.demo.service.KrajService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kraji")
@CrossOrigin(origins = "*")
public class KrajController {

    @Autowired
    private KrajService krajService;

    @GetMapping
    public ResponseEntity<?> pridobiVseKraje() {
        try {
            List<Kraj> kraji = krajService.pridobiVseKraje();
            return ResponseEntity.ok(kraji);
        } catch (Exception e) {
            e.printStackTrace(); // Log error for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Napaka pri pridobivanju krajev: " + e.getMessage());
                
        }
    }

    @GetMapping("/pot/{potId}")
    public ResponseEntity<List<Object[]>> podrobnostiPoti(@PathVariable Long  potId) {
        try {
            List<Object[]> podrobnosti = krajService.podrobnostiPoti(potId);
            return ResponseEntity.ok(podrobnosti);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            
        }
    }
}

