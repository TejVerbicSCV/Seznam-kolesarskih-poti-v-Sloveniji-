package com.example.demo.repository;

import com.example.demo.model.Zanimivost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ZanimivostRepository extends JpaRepository<Zanimivost, Long> {

    @Query(value = "SELECT * FROM pridobi_vse_znamenitosti()", nativeQuery = true)
    List<Zanimivost> probiVseZanimivosti();

    @Query(value = "SELECT * FROM pridobi_zanimivost_po_id(CAST(:p_id AS BIGINT))", nativeQuery = true)
    Zanimivost pridobiZanimivostPoId(@Param("p_id") Long id);
}
