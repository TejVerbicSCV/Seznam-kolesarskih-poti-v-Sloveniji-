package com.example.demo.controller;

import com.example.demo.model.Uporabnik;
import com.example.demo.service.UporabnikService;
import com.example.demo.dto.RegistracijaRequest;
import com.example.demo.dto.PrijavaRequest;
import com.example.demo.dto.SpremembaGeslaRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/uporabniki")

public class UporabnikController {

    @Autowired
    private UporabnikService uporabnikService;

    // D) Registracija novega uporabnika
    @PostMapping("/registracija")
    public ResponseEntity<?> registracija(@RequestBody RegistracijaRequest request) {
        try {
            // Check if user already exists
            Optional<Uporabnik> existing = uporabnikService.pridobiUporabnikaPoImenu(request.uporabnikoIme);
            if (existing.isPresent()) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Uporabniško ime že obstaja");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
            }

            uporabnikService.registrirajUporabnika(request.uporabnikoIme, request.geslo);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Uporabnik uspešno registriran");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("message", "Napaka pri registraciji: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // E) Prijava uporabnika
    @PostMapping("/prijava")
    public ResponseEntity<?> prijava(@RequestBody PrijavaRequest request) {
        try {
            Object[] rezultat = uporabnikService.prijaviUporabnika(request.uporabnikoIme, request.geslo);
            
            if (rezultat == null) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Napačno uporabničko ime ali geslo");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }

            // Format response with user data
            Map<String, Object> response = new HashMap<>();
            response.put("id", rezultat[0]);
            response.put("uporabnikoIme", rezultat[1]);
            response.put("vloga", rezultat[2]);
            response.put("message", "Prijava uspešna");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("message", "Napaka pri prijavi: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // F) Sprememba gesla
    @PutMapping("/{id}/sprememba-gesla")
    public ResponseEntity<?> spremembaGesla(@PathVariable Long id,
                                            @RequestBody SpremembaGeslaRequest request) {
        try {
            // Verify user exists
            Optional<Uporabnik> user = uporabnikService.pridobiUporabnika(id);
            if (!user.isPresent()) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Uporabnik ni najden");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

            uporabnikService.spremembaGesla(id, request.novoGeslo);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Geslo uspešno spremenjeno");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("message", "Napaka pri spremembi gesla: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Pridobi uporabnika po ID
    @GetMapping("/{id}")
    public ResponseEntity<?> pridobiUporabnika(@PathVariable Long id) {
        try {
            Optional<Uporabnik> user = uporabnikService.pridobiUporabnika(id);
            if (user.isPresent()) {
                return ResponseEntity.ok(user.get());
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Uporabnik ni najden");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("message", "Napaka pri pridobivanju uporabnika: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
