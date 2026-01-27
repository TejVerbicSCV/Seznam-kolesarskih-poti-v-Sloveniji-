package com.example.demo.repository;

import com.example.demo.model.Kraj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KrajRepository extends JpaRepository<Kraj, Long> {

    // K) Pridobi vse kraje - using stored function
    @Query(value = "SELECT * FROM pridobi_vse_kraje()", nativeQuery = true)
    List<Kraj> pridobiVseKraje();

    // M) Podrobnosti poti (kraji za pot) - using stored function
    @Query(value = "SELECT * FROM pridobi_podrobnosti_poti(:pot_id_param)", nativeQuery = true)
    List<Object[]> podrobnostiPoti(@Param("pot_id_param") Long potId);
}

