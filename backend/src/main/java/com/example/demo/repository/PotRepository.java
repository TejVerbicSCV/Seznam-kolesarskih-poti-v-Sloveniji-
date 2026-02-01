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

    @Transactional
    @Query(value = "SELECT vnesi_kolesarsko_pot_s_krajem_fn(CAST(:p_ime AS TEXT), :p_dolzina, CAST(:p_tezavnost AS TEXT), CAST(:p_cas AS TEXT), CAST(:p_opis AS TEXT), CAST(:p_uporabnik_id AS BIGINT), CAST(:p_kraj_id AS BIGINT))", nativeQuery = true)
    String vnosPotiSKrajem(
            @Param("p_ime") String ime,
            @Param("p_dolzina") Float dolzina,
            @Param("p_tezavnost") String tezavnost,
            @Param("p_cas") String cas,
            @Param("p_opis") String opis,
            @Param("p_uporabnik_id") Long uporabnikId,
            @Param("p_kraj_id") Long krajId
    );

    @Transactional
    @Query(value = "SELECT uredi_kolesarsko_pot_fn(CAST(:p_id AS BIGINT), CAST(:p_ime AS TEXT), :p_dolzina, CAST(:p_tezavnost AS TEXT), CAST(:p_cas AS TEXT), CAST(:p_opis AS TEXT), CAST(:p_kraji_ids AS BIGINT[]), CAST(:p_znamenitosti_ids AS BIGINT[]))", nativeQuery = true)
    String urediPot(
            @Param("p_id") Long id,
            @Param("p_ime") String ime,
            @Param("p_dolzina") Float dolzina,
            @Param("p_tezavnost") String tezavnost,
            @Param("p_cas") String cas,
            @Param("p_opis") String opis,
            @Param("p_kraji_ids") String krajiIds,
            @Param("p_znamenitosti_ids") String znamenitostiIds
    );

    @Transactional
    @Query(value = "SELECT izbrisi_kolesarsko_pot_varna_fn(CAST(:p_pot_id AS BIGINT), CAST(:p_izvajalec_id AS BIGINT))", nativeQuery = true)
    String izbrisiPotVarna(
            @Param("p_pot_id") Long potId,
            @Param("p_izvajalec_id") Long izvajalecId
    );

    @Query(value = "SELECT * FROM filtriraj_poti_po_tezavnosti(CAST(:p_tez AS TEXT))", nativeQuery = true)
    List<KolesarskaPot> filtrirajPoTezavnosti(@Param("p_tez") String tezavnost);

    @Transactional
    @Query(value = "SELECT vnesi_kolesarsko_pot_fn(CAST(:p_ime AS TEXT), :p_dolzina, CAST(:p_tezavnost AS TEXT), CAST(:p_cas AS TEXT), CAST(:p_opis AS TEXT), CAST(:p_uporabnik_id AS BIGINT), CAST(:p_kraji_ids AS BIGINT[]), CAST(:p_znamenitosti_ids AS BIGINT[]))", nativeQuery = true)
    String vnosPoti(
            @Param("p_ime") String ime,
            @Param("p_dolzina") Float dolzina,
            @Param("p_tezavnost") String tezavnost,
            @Param("p_cas") String cas,
            @Param("p_opis") String opis,
            @Param("p_uporabnik_id") Long uporabnikId,
            @Param("p_kraji_ids") String krajiIds,
            @Param("p_znamenitosti_ids") String znamenitostiIds
    );

    @Query(value = "SELECT * FROM pridobi_vse_poti()", nativeQuery = true)
    List<KolesarskaPot> pridobiVsePoti();

    @Query(value = "SELECT * FROM pridobi_pot_po_id(CAST(:p_id AS BIGINT))", nativeQuery = true)
    KolesarskaPot pridobiPotPoId(@Param("p_id") Long id);
}