package com.example.demo.repository;

import com.example.demo.model.Nastavitev;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NastavitvRepository extends JpaRepository<Nastavitev, String> {

    @Query(value = "SELECT pridobi_nastavitev(:p_key)", nativeQuery = true)
    String probiNastavitev(@Param("p_key") String key);

    @Query(value = "SELECT * FROM pridobi_vse_nastavitve()", nativeQuery = true)
    java.util.List<Nastavitev> pridobiVseNastavitve();

    @Query(value = "SELECT * FROM pridobi_nastavitev_po_kljucu_fn(CAST(:p_key AS TEXT))", nativeQuery = true)
    Nastavitev pridobiNastavitvEntitetoPoId(@Param("p_key") String key);
}
