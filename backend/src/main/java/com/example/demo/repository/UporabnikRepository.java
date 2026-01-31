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

<<<<<<< Updated upstream
    // D) REGISTRACIJA (register user)
    @Modifying
    @Transactional
    @Query(value = "CALL registriraj_uporabnika(:p_user, :p_pass)", nativeQuery = true)
    void registrirajUporabnika(
=======
    @Transactional
    @Query(value = "SELECT registriraj_uporabnika_fn(CAST(:p_user AS TEXT), CAST(:p_pass AS TEXT))", nativeQuery = true)
    String registrirajUporabnika(
>>>>>>> Stashed changes
            @Param("p_user") String uporabnicoIme,
            @Param("p_pass") String geslo
    );

<<<<<<< Updated upstream
    // E) PRIJAVA (login - returns user data)
    @Query(value = "SELECT * FROM prijava_uporabnika(:p_user, :p_pass)", nativeQuery = true)
    List<Object[]> prijaviUporabnika(
            @Param("p_user") String uporabnicoIme,
            @Param("p_pass") String geslo
    );

    // F) SPREMEMBA GESLA (change password)
    @Modifying
    @Transactional
    @Query(value = "CALL zamenjaj_geslo(:p_u_id, :p_novo_geslo)", nativeQuery = true)
    void spremembaGesla(
=======
    @Query(value = "SELECT * FROM prijava_uporabnika(CAST(:p_user AS TEXT))", nativeQuery = true)
    List<Object[]> prijaviUporabnika(
            @Param("p_user") String uporabnicoIme
    );

    @Transactional
    @Query(value = "SELECT zamenjaj_geslo_fn(CAST(:p_u_id AS BIGINT), CAST(:p_novo_geslo AS TEXT))", nativeQuery = true)
    String spremembaGesla(
>>>>>>> Stashed changes
            @Param("p_u_id") Long uporabnikId,
            @Param("p_novo_geslo") String novoGeslo
    );

<<<<<<< Updated upstream
    // Find user by username
    Uporabnik findByUporabnikoIme(String uporabnicoIme);
=======
    @Query(value = "SELECT * FROM pridobi_uporabnika_po_id(CAST(:p_id AS BIGINT))", nativeQuery = true)
    Uporabnik pridobiUporabnikaPoId(@Param("p_id") Long id);

    @Query(value = "SELECT * FROM pridobi_uporabnika_po_imenu(CAST(:p_username AS TEXT))", nativeQuery = true)
    Uporabnik pridobiUporabnikaPoImenu(@Param("p_username") String uporabnicoIme);
>>>>>>> Stashed changes
}
