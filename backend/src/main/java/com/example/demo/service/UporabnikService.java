package com.example.demo.service;

import com.example.demo.model.Uporabnik;
import com.example.demo.repository.UporabnikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UporabnikService {

    @Autowired
    private UporabnikRepository uporabnikRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // D) Registracija novega uporabnika
    public void registrirajUporabnika(String uporabnicoIme, String geslo) {
        String hashedPass = passwordEncoder.encode(geslo);
        uporabnikRepository.registrirajUporabnika(uporabnicoIme, hashedPass);
    }

    // E) Prijava uporabnika
    public Object[] prijaviUporabnika(String uporabnicoIme, String geslo) {
        List<Object[]> rezultat = uporabnikRepository.prijaviUporabnika(uporabnicoIme);
        if (rezultat != null && !rezultat.isEmpty()) {
            Object[] row = rezultat.get(0);
            String hashedPass = (String) row[3]; // Fourth column is u_geslo
            
            if (passwordEncoder.matches(geslo, hashedPass)) {
                // Return only the first three columns (id, name, role) to maintain original frontend compatibility
                return new Object[] { row[0], row[1], row[2] };
            }
        }
        return null;
    }

    // F) Sprememba gesla
    public void spremembaGesla(Long uporabnikId, String novoGeslo) {
        String hashedPass = passwordEncoder.encode(novoGeslo);
        uporabnikRepository.spremembaGesla(uporabnikId, hashedPass);
    }

    // Pridobi uporabnika po imenu
    public Optional<Uporabnik> pridobiUporabnikaPoImenu(String uporabnicoIme) {
        return Optional.ofNullable(uporabnikRepository.pridobiUporabnikaPoImenu(uporabnicoIme));
    }

    // Pridobi uporabnika po ID
    public Optional<Uporabnik> pridobiUporabnika(Long id) {
        return Optional.ofNullable(uporabnikRepository.pridobiUporabnikaPoId(id));
    }
}
