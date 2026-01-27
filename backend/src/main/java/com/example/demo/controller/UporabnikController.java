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
@CrossOrigin(origins = "*")
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
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Uporabničko ime že obstaja");
            }

            uporabnikService.registrirajUporabnika(request.uporabnikoIme, request.geslo);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body("Uporabnik uspešno registriran");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Napaka pri registraciji: " + e.getMessage());
        }
    }

    // E) Prijava uporabnika
    @PostMapping("/prijava")
    public ResponseEntity<?> prijava(@RequestBody PrijavaRequest request) {
        try {
            Object[] rezultat = uporabnikService.prijaviUporabnika(request.uporabnikoIme, request.geslo);
            
            if (rezultat == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Napačno uporabničko ime ali geslo");
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Napaka pri prijavi: " + e.getMessage());
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
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Uporabnik ni najden");
            }

            uporabnikService.spremembaGesla(id, request.novoGeslo);
            return ResponseEntity.ok("Geslo uspešno spremenjeno");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Napaka pri spremembi gesla: " + e.getMessage());
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
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Uporabnik ni najden");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Napaka pri pridobivanju uporabnika: " + e.getMessage());
        }
    }
}
