package com.example.demo.controller;

import com.example.demo.model.Zanimivost;
import com.example.demo.service.ZanimivostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/zanimivosti")

public class ZanimivostController {

    @Autowired
    private ZanimivostService znamenitostService;

    // L) Pridobi vse znamenitosti (attractions)
    @GetMapping
    public ResponseEntity<?> probiVseZanimivosti() {
        try {
            List<Zanimivost> zanimivosti = znamenitostService.probiVseZanimivosti();
            return ResponseEntity.ok(zanimivosti);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("message", "Napaka pri pridobivanju znamenitosti: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Pridobi znamenitost po ID
    @GetMapping("/{id}")
    public ResponseEntity<?> probiZanimivost(@PathVariable Long id) {
        try {
            Optional<Zanimivost> zanimivost = znamenitostService.probiZanimivost(id);
            if (zanimivost.isPresent()) {
                return ResponseEntity.ok(zanimivost.get());
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Znamenitost ni najdena");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("message", "Napaka pri pridobivanju znamenitosti: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
