package com.example.demo.controller;

import com.example.demo.model.Kraj;
import com.example.demo.service.KrajService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

<<<<<<< Updated upstream
import java.util.List;

@RestController
@RequestMapping("/api/kraji")
@CrossOrigin(origins = "*")
=======
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/kraji")

>>>>>>> Stashed changes
public class KrajController {

    @Autowired
    private KrajService krajService;

    @GetMapping
    public ResponseEntity<?> pridobiVseKraje() {
        try {
            List<Kraj> kraji = krajService.pridobiVseKraje();
            return ResponseEntity.ok(kraji);
        } catch (Exception e) {
<<<<<<< Updated upstream
            e.printStackTrace(); // Log error for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Napaka pri pridobivanju krajev: " + e.getMessage());
                
=======
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("message", "Napaka pri pridobivanju krajev: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
>>>>>>> Stashed changes
        }
    }

    @GetMapping("/pot/{potId}")
<<<<<<< Updated upstream
    public ResponseEntity<List<Object[]>> podrobnostiPoti(@PathVariable Long  potId) {
=======
    public ResponseEntity<?> podrobnostiPoti(@PathVariable Long potId) {
>>>>>>> Stashed changes
        try {
            List<Object[]> podrobnosti = krajService.podrobnostiPoti(potId);
            return ResponseEntity.ok(podrobnosti);
        } catch (Exception e) {
<<<<<<< Updated upstream
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            
        }
    }
}

=======
            Map<String, String> error = new HashMap<>();
            error.put("message", "Napaka pri pridobivanju podrobnosti poti");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
>>>>>>> Stashed changes
