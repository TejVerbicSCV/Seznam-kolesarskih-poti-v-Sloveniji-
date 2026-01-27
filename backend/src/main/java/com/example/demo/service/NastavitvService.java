package com.example.demo.service;

import com.example.demo.model.Nastavitev;
import com.example.demo.repository.NastavitvRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class NastavitvService {

    @Autowired
    private NastavitvRepository nastavitvRepository;

    // H) Pridobi nastavitev po ključu (get setting by key)
    public String probiNastavitev(String key) {
        return nastavitvRepository.probiNastavitev(key);
    }

    // Pridobi nastavitev entiteto po ključu
    public Optional<Nastavitev> probiNastavitvEntiteto(String key) {
        return Optional.ofNullable(nastavitvRepository.findByKey(key));
    }
}
