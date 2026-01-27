package com.example.demo.repository;

import com.example.demo.model.KolesarskaPot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PotRepository extends JpaRepository<KolesarskaPot, Long> {

    // A) VNOS NOVE POTI - Osnovni (insert new path with location)
    @Modifying
    @Transactional
    @Query(value = "CALL vnesi_kolesarsko_pot_s_krajem(:p_ime, :p_dolzina, :p_tezavnost, :p_cas, :p_opis, :p_uporabnik_id, :p_kraj_id)", nativeQuery = true)
    void vnosPotiSKrajem(
            @Param("p_ime") String ime,
            @Param("p_dolzina") Float dolzina,
            @Param("p_tezavnost") String tezavnost,
            @Param("p_cas") String cas,
            @Param("p_opis") String opis,
            @Param("p_uporabnik_id") Long uporabnikId,
            @Param("p_kraj_id") Long krajId
    );

    // B) UREJANJE POTI (update path)
    @Modifying
    @Transactional
    @Query(value = "CALL uredi_kolesarsko_pot(:p_id, :p_ime, :p_dolzina, :p_tezavnost, :p_cas, :p_opis)", nativeQuery = true)
    void urediPot(
            @Param("p_id") Long id,
            @Param("p_ime") String ime,
            @Param("p_dolzina") Float dolzina,
            @Param("p_tezavnost") String tezavnost,
            @Param("p_cas") String cas,
            @Param("p_opis") String opis
    );

    // C) VARNO BRISANJE (safe delete with admin check)
    @Modifying
    @Transactional
    @Query(value = "CALL izbrisi_kolesarsko_pot_varna(:p_pot_id, :p_izvajalec_id)", nativeQuery = true)
    void izbrisiPotVarna(
            @Param("p_pot_id") Long potId,
            @Param("p_izvajalec_id") Long izvajalecId
    );

    // G) FILTRIRANJE PO TEZAVNOSTI (filter by difficulty)
    @Query(value = "SELECT * FROM filtriraj_poti_po_tezavnosti(:p_tez)", nativeQuery = true)
    List<KolesarskaPot> filtrirajPoTezavnosti(@Param("p_tez") String tezavnost);

    // I) KOMPLEKSEN VNOS (insert with arrays of locations and attractions)
    @Modifying
    @Transactional
    @Query(value = "CALL vnesi_kolesarsko_pot(:p_ime, :p_dolzina, :p_tezavnost, :p_cas, :p_opis, :p_uporabnik_id, :p_kraji_ids, :p_znamenitosti_ids)", nativeQuery = true)
    void vnosPoti(
            @Param("p_ime") String ime,
            @Param("p_dolzina") Float dolzina,
            @Param("p_tezavnost") String tezavnost,
            @Param("p_cas") String cas,
            @Param("p_opis") String opis,
            @Param("p_uporabnik_id") Long uporabnikId,
            @Param("p_kraji_ids") long[] krajiIds,
            @Param("p_znamenitosti_ids") long[] znamenitostiIds
    );
}