package com.example.demo.controller;

import com.example.demo.model.Zanimivost;
import com.example.demo.service.ZanimivostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/zanimivosti")
@CrossOrigin(origins = "*")
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Napaka pri pridobivanju znamenitosti: " + e.getMessage());
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
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Znamenitost ni najdena");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Napaka pri pridobivanju znamenitosti: " + e.getMessage());
        }
    }
}
