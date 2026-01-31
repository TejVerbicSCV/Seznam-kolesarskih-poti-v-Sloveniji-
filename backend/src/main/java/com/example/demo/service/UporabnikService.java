package com.example.demo.service;

import com.example.demo.model.Uporabnik;
import com.example.demo.repository.UporabnikRepository;
import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< Updated upstream
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
=======
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
>>>>>>> Stashed changes
import java.util.Optional;

@Service
public class UporabnikService {

    @Autowired
    private UporabnikRepository uporabnikRepository;

<<<<<<< Updated upstream
    // D) Registracija novega uporabnika
    public void registrirajUporabnika(String uporabnicoIme, String geslo) {
        uporabnikRepository.registrirajUporabnika(uporabnicoIme, geslo);
=======
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // D) Registracija novega uporabnika
    public void registrirajUporabnika(String uporabnicoIme, String geslo) {
        String hashedPass = passwordEncoder.encode(geslo);
        uporabnikRepository.registrirajUporabnika(uporabnicoIme, hashedPass);
>>>>>>> Stashed changes
    }

    // E) Prijava uporabnika
    public Object[] prijaviUporabnika(String uporabnicoIme, String geslo) {
<<<<<<< Updated upstream
        List<Object[]> rezultat = uporabnikRepository.prijaviUporabnika(uporabnicoIme, geslo);
        if (rezultat != null && !rezultat.isEmpty()) {
            return rezultat.get(0);
=======
        List<Object[]> rezultat = uporabnikRepository.prijaviUporabnika(uporabnicoIme);
        if (rezultat != null && !rezultat.isEmpty()) {
            Object[] row = rezultat.get(0);
            String hashedPass = (String) row[3]; // Fourth column is u_geslo
            
            if (passwordEncoder.matches(geslo, hashedPass)) {
                // Return only the first three columns (id, name, role) to maintain original frontend compatibility
                return new Object[] { row[0], row[1], row[2] };
            }
>>>>>>> Stashed changes
        }
        return null;
    }

    // F) Sprememba gesla
    public void spremembaGesla(Long uporabnikId, String novoGeslo) {
<<<<<<< Updated upstream
        uporabnikRepository.spremembaGesla(uporabnikId, novoGeslo);
=======
        String hashedPass = passwordEncoder.encode(novoGeslo);
        uporabnikRepository.spremembaGesla(uporabnikId, hashedPass);
>>>>>>> Stashed changes
    }

    // Pridobi uporabnika po imenu
    public Optional<Uporabnik> pridobiUporabnikaPoImenu(String uporabnicoIme) {
<<<<<<< Updated upstream
        return Optional.ofNullable(uporabnikRepository.findByUporabnikoIme(uporabnicoIme));
=======
        return Optional.ofNullable(uporabnikRepository.pridobiUporabnikaPoImenu(uporabnicoIme));
>>>>>>> Stashed changes
    }

    // Pridobi uporabnika po ID
    public Optional<Uporabnik> pridobiUporabnika(Long id) {
<<<<<<< Updated upstream
        return uporabnikRepository.findById(id);
=======
        return Optional.ofNullable(uporabnikRepository.pridobiUporabnikaPoId(id));
>>>>>>> Stashed changes
    }
}
