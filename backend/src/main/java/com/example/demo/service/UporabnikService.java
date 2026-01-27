package com.example.demo.service;

import com.example.demo.model.Uporabnik;
import com.example.demo.repository.UporabnikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UporabnikService {

    @Autowired
    private UporabnikRepository uporabnikRepository;

    // D) Registracija novega uporabnika
    public void registrirajUporabnika(String uporabnicoIme, String geslo) {
        uporabnikRepository.registrirajUporabnika(uporabnicoIme, geslo);
    }

    // E) Prijava uporabnika
    public Object[] prijaviUporabnika(String uporabnicoIme, String geslo) {
        List<Object[]> rezultat = uporabnikRepository.prijaviUporabnika(uporabnicoIme, geslo);
        if (rezultat != null && !rezultat.isEmpty()) {
            return rezultat.get(0);
        }
        return null;
    }

    // F) Sprememba gesla
    public void spremembaGesla(Long uporabnikId, String novoGeslo) {
        uporabnikRepository.spremembaGesla(uporabnikId, novoGeslo);
    }

    // Pridobi uporabnika po imenu
    public Optional<Uporabnik> pridobiUporabnikaPoImenu(String uporabnicoIme) {
        return Optional.ofNullable(uporabnikRepository.findByUporabnikoIme(uporabnicoIme));
    }

    // Pridobi uporabnika po ID
    public Optional<Uporabnik> pridobiUporabnika(Long id) {
        return uporabnikRepository.findById(id);
    }
}
