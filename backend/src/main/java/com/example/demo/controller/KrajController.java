package com.example.demo.controller;

import com.example.demo.model.Kraj;
import com.example.demo.service.KrajService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/kraji")

public class KrajController {

    @Autowired
    private KrajService krajService;

    @GetMapping
    public ResponseEntity<?> pridobiVseKraje() {
        try {
            List<Kraj> kraji = krajService.pridobiVseKraje();
            return ResponseEntity.ok(kraji);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("message", "Napaka pri pridobivanju krajev: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/pot/{potId}")
    public ResponseEntity<?> podrobnostiPoti(@PathVariable Long potId) {
        try {
            List<Object[]> podrobnosti = krajService.podrobnostiPoti(potId);
            return ResponseEntity.ok(podrobnosti);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Napaka pri pridobivanju podrobnosti poti");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
