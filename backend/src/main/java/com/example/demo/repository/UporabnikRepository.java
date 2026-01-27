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

    // D) REGISTRACIJA (register user)
    @Modifying
    @Transactional
    @Query(value = "CALL registriraj_uporabnika(:p_user, :p_pass)", nativeQuery = true)
    void registrirajUporabnika(
            @Param("p_user") String uporabnicoIme,
            @Param("p_pass") String geslo
    );

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
            @Param("p_u_id") Long uporabnikId,
            @Param("p_novo_geslo") String novoGeslo
    );

    // Find user by username
    Uporabnik findByUporabnikoIme(String uporabnicoIme);
}
