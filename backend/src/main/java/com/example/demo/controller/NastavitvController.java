package com.example.demo.controller;

import com.example.demo.service.NastavitvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nastavitve")
@CrossOrigin(origins = "*")
public class NastavitvController {

    @Autowired
    private NastavitvService nastavitvService;

    // H) Pridobi nastavitev po ključu (get setting by key)
    @GetMapping("/{key}")
    public ResponseEntity<?> probiNastavitev(@PathVariable String key) {
        try {
            String vrednost = nastavitvService.probiNastavitev(key);
            if (vrednost != null) {
                return ResponseEntity.ok(vrednost);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Nastavitev ni najdena");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Napaka pri pridobivanju nastavitve: " + e.getMessage());
        }
    }
}
