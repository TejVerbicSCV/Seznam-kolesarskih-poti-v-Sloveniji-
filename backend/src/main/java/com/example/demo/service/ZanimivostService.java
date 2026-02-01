package com.example.demo.service;

import com.example.demo.model.Zanimivost;
import com.example.demo.repository.ZanimivostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ZanimivostService {

    @Autowired
    private ZanimivostRepository znamenitostRepository;

    // L) Pridobi vse znamenitosti (attractions)
    public List<Zanimivost> probiVseZanimivosti() {
        return znamenitostRepository.probiVseZanimivosti();
    }

    // Pridobi po ID
    public Optional<Zanimivost> probiZanimivost(Long id) {
        return Optional.ofNullable(znamenitostRepository.pridobiZanimivostPoId(id));
    }
}
