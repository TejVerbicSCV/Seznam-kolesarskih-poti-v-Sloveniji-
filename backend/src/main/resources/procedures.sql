-- Database Stored Procedures for Kolesarske Poti Application
-- This file contains all stored procedures used by the application

-- ============================================================================
-- KRAJI (Locations) Procedures
-- ============================================================================

-- K) Pridobi vse kraje (Get all locations)
CREATE OR REPLACE FUNCTION pridobi_vse_kraje()
RETURNS TABLE (
    id BIGINT,
    ime VARCHAR(50),
    regija VARCHAR(50),
    postna_stavilka BIGINT
) AS $$
BEGIN
    RETURN QUERY
    SELECT k."id", k."ime", k."regija", k."postna_stavilka"
    FROM "kraji" k
    ORDER BY k."ime" ASC;
END;
$$ LANGUAGE plpgsql;

-- M) Pridobi podrobnosti poti (kraji za pot)
CREATE OR REPLACE FUNCTION pridobi_podrobnosti_poti(pot_id_param BIGINT)
RETURNS TABLE (
    ime VARCHAR(50),
    regija VARCHAR(50)
) AS $$
BEGIN
    RETURN QUERY
    SELECT k."ime", k."regija"
    FROM "kraji" k
    JOIN "kolesarske_poti_kraji" kpk ON k."id" = kpk."kraj_id"
    WHERE kpk."kolesarska_pot_id" = pot_id_param;
END;
$$ LANGUAGE plpgsql;

-- ============================================================================
-- ZANIMIVOSTI (Attractions) Procedures
-- ============================================================================

-- L) Pridobi vse znamenitosti (Get all attractions)
CREATE OR REPLACE FUNCTION pridobi_vse_znamenitosti()
RETURNS TABLE (
    id BIGINT,
    ime VARCHAR(100),
    opis TEXT
) AS $$
BEGIN
    RETURN QUERY
    SELECT z."id", z."ime", z."opis"
    FROM "zanimivosti" z
    ORDER BY z."ime" ASC;
END;
$$ LANGUAGE plpgsql;

-- ============================================================================
-- NASTAVITVE (Settings) Procedures
-- ============================================================================

-- I) Pridobi nastavitev (Already exists as function, keeping as is)
-- SELECT pridobi_nastavitev(:key)

-- ============================================================================
-- Note: Remaining procedures already exist:
-- - vnesi_kolesarsko_pot_s_krajem
-- - uredi_kolesarsko_pot
-- - izbrisi_kolesarsko_pot_varna
-- - vnesi_kolesarsko_pot
-- - filtriraj_poti_po_tezavnosti
-- - isci_poti_v_kraju
-- - registriraj_uporabnika
-- - prijava_uporabnika (function)
-- - zamenjaj_geslo
-- ============================================================================
