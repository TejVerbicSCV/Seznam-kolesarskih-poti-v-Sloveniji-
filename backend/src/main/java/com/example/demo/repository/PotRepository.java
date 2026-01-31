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

<<<<<<< Updated upstream
    // A) VNOS NOVE POTI - Osnovni (insert new path with location)
    @Modifying
    @Transactional
    @Query(value = "CALL vnesi_kolesarsko_pot_s_krajem(:p_ime, :p_dolzina, :p_tezavnost, :p_cas, :p_opis, :p_uporabnik_id, :p_kraj_id)", nativeQuery = true)
    void vnosPotiSKrajem(
=======
    @Transactional
    @Query(value = "SELECT vnesi_kolesarsko_pot_s_krajem_fn(CAST(:p_ime AS TEXT), :p_dolzina, CAST(:p_tezavnost AS TEXT), CAST(:p_cas AS TEXT), CAST(:p_opis AS TEXT), CAST(:p_uporabnik_id AS BIGINT), CAST(:p_kraj_id AS BIGINT))", nativeQuery = true)
    String vnosPotiSKrajem(
>>>>>>> Stashed changes
            @Param("p_ime") String ime,
            @Param("p_dolzina") Float dolzina,
            @Param("p_tezavnost") String tezavnost,
            @Param("p_cas") String cas,
            @Param("p_opis") String opis,
            @Param("p_uporabnik_id") Long uporabnikId,
            @Param("p_kraj_id") Long krajId
    );

<<<<<<< Updated upstream
    // B) UREJANJE POTI (update path)
    @Modifying
    @Transactional
    @Query(value = "CALL uredi_kolesarsko_pot(:p_id, :p_ime, :p_dolzina, :p_tezavnost, :p_cas, :p_opis)", nativeQuery = true)
    void urediPot(
=======
    @Transactional
    @Query(value = "SELECT uredi_kolesarsko_pot_fn(CAST(:p_id AS BIGINT), CAST(:p_ime AS TEXT), :p_dolzina, CAST(:p_tezavnost AS TEXT), CAST(:p_cas AS TEXT), CAST(:p_opis AS TEXT), CAST(:p_kraji_ids AS BIGINT[]), CAST(:p_znamenitosti_ids AS BIGINT[]))", nativeQuery = true)
    String urediPot(
>>>>>>> Stashed changes
            @Param("p_id") Long id,
            @Param("p_ime") String ime,
            @Param("p_dolzina") Float dolzina,
            @Param("p_tezavnost") String tezavnost,
            @Param("p_cas") String cas,
<<<<<<< Updated upstream
            @Param("p_opis") String opis
    );

    // C) VARNO BRISANJE (safe delete with admin check)
    @Modifying
    @Transactional
    @Query(value = "CALL izbrisi_kolesarsko_pot_varna(:p_pot_id, :p_izvajalec_id)", nativeQuery = true)
    void izbrisiPotVarna(
=======
            @Param("p_opis") String opis,
            @Param("p_kraji_ids") String krajiIds,
            @Param("p_znamenitosti_ids") String znamenitostiIds
    );

    @Transactional
    @Query(value = "SELECT izbrisi_kolesarsko_pot_varna_fn(CAST(:p_pot_id AS BIGINT), CAST(:p_izvajalec_id AS BIGINT))", nativeQuery = true)
    String izbrisiPotVarna(
>>>>>>> Stashed changes
            @Param("p_pot_id") Long potId,
            @Param("p_izvajalec_id") Long izvajalecId
    );

<<<<<<< Updated upstream
    // G) FILTRIRANJE PO TEZAVNOSTI (filter by difficulty)
    @Query(value = "SELECT * FROM filtriraj_poti_po_tezavnosti(:p_tez)", nativeQuery = true)
    List<KolesarskaPot> filtrirajPoTezavnosti(@Param("p_tez") String tezavnost);

    // I) KOMPLEKSEN VNOS (insert with arrays of locations and attractions)
    @Modifying
    @Transactional
    @Query(value = "CALL vnesi_kolesarsko_pot(:p_ime, :p_dolzina, :p_tezavnost, :p_cas, :p_opis, :p_uporabnik_id, :p_kraji_ids, :p_znamenitosti_ids)", nativeQuery = true)
    void vnosPoti(
=======
    @Query(value = "SELECT * FROM filtriraj_poti_po_tezavnosti(CAST(:p_tez AS TEXT))", nativeQuery = true)
    List<KolesarskaPot> filtrirajPoTezavnosti(@Param("p_tez") String tezavnost);

    @Transactional
    @Query(value = "SELECT vnesi_kolesarsko_pot_fn(CAST(:p_ime AS TEXT), :p_dolzina, CAST(:p_tezavnost AS TEXT), CAST(:p_cas AS TEXT), CAST(:p_opis AS TEXT), CAST(:p_uporabnik_id AS BIGINT), CAST(:p_kraji_ids AS BIGINT[]), CAST(:p_znamenitosti_ids AS BIGINT[]))", nativeQuery = true)
    String vnosPoti(
>>>>>>> Stashed changes
            @Param("p_ime") String ime,
            @Param("p_dolzina") Float dolzina,
            @Param("p_tezavnost") String tezavnost,
            @Param("p_cas") String cas,
            @Param("p_opis") String opis,
            @Param("p_uporabnik_id") Long uporabnikId,
<<<<<<< Updated upstream
            @Param("p_kraji_ids") long[] krajiIds,
            @Param("p_znamenitosti_ids") long[] znamenitostiIds
    );
=======
            @Param("p_kraji_ids") String krajiIds,
            @Param("p_znamenitosti_ids") String znamenitostiIds
    );

    @Query(value = "SELECT * FROM pridobi_vse_poti()", nativeQuery = true)
    List<KolesarskaPot> pridobiVsePoti();

    @Query(value = "SELECT * FROM pridobi_pot_po_id(CAST(:p_id AS BIGINT))", nativeQuery = true)
    KolesarskaPot pridobiPotPoId(@Param("p_id") Long id);
>>>>>>> Stashed changes
}