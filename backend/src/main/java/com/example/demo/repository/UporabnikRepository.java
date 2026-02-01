package com.example.demo.repository;

import com.example.demo.model.Uporabnik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Repository
public interface UporabnikRepository extends JpaRepository<Uporabnik, Long> {

    @Transactional
    @Query(value = "SELECT registriraj_uporabnika_fn(CAST(:p_user AS TEXT), CAST(:p_pass AS TEXT))", nativeQuery = true)
    String registrirajUporabnika(
            @Param("p_user") String uporabnicoIme,
            @Param("p_pass") String geslo
    );

    @Query(value = "SELECT * FROM prijava_uporabnika(CAST(:p_user AS TEXT))", nativeQuery = true)
    List<Object[]> prijaviUporabnika(
            @Param("p_user") String uporabnicoIme
    );

    @Transactional
    @Query(value = "SELECT zamenjaj_geslo_fn(CAST(:p_u_id AS BIGINT), CAST(:p_novo_geslo AS TEXT))", nativeQuery = true)
    String spremembaGesla(
            @Param("p_u_id") Long uporabnikId,
            @Param("p_novo_geslo") String novoGeslo
    );

    @Query(value = "SELECT * FROM pridobi_uporabnika_po_id(CAST(:p_id AS BIGINT))", nativeQuery = true)
    Uporabnik pridobiUporabnikaPoId(@Param("p_id") Long id);

    @Query(value = "SELECT * FROM pridobi_uporabnika_po_imenu(CAST(:p_username AS TEXT))", nativeQuery = true)
    Uporabnik pridobiUporabnikaPoImenu(@Param("p_username") String uporabnicoIme);
}
