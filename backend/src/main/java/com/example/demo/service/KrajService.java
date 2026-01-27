package com.example.demo.service;

import com.example.demo.model.Kraj;
import com.example.demo.repository.KrajRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KrajService {

    @Autowired
    private KrajRepository krajRepository;

    public List<Kraj> pridobiVseKraje() {
        return krajRepository.pridobiVseKraje();
    }

    public List<Object[]> podrobnostiPoti(Long  potId) {
        return krajRepository.podrobnostiPoti(potId);
    }
}

