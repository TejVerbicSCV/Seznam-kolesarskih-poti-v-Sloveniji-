/*
Seznam kolesarskih poti v Sloveniji - Database Initialization Script
Consolidated DDL, Functions, and Procedures
*/

-- ============================================================================
-- 1. DROP EXISTING TABLES AND FUNCTIONS (Clean Slate)
-- ============================================================================
DROP TABLE IF EXISTS "kolesarske_poti_znamenitosti" CASCADE;
DROP TABLE IF EXISTS "kolesarske_poti_kraji" CASCADE;
DROP TABLE IF EXISTS "log_sprememb" CASCADE;
DROP TABLE IF EXISTS "kolesarske_poti" CASCADE;
DROP TABLE IF EXISTS "zanimivosti" CASCADE;
DROP TABLE IF EXISTS "uporabniki" CASCADE;
DROP TABLE IF EXISTS "kraji" CASCADE;
DROP TABLE IF EXISTS "nastavitve" CASCADE;

-- ============================================================================
-- 2. CREATE TABLES
-- ============================================================================

CREATE TABLE "kraji" (
  "id" Serial NOT NULL,
  "ime" Character varying(50) NOT NULL,
  "regija" Character varying(50) NOT NULL,
  "postna_stavilka" Integer NOT NULL,
  CONSTRAINT "PK_kraji" PRIMARY KEY ("id")
);

CREATE TABLE "uporabniki" (
  "id" Serial NOT NULL,
  "uporabnisko_ime" Character varying(100) NOT NULL,
  "geslo" Text NOT NULL,
  "vloga" Character varying(20) DEFAULT 'USER' NOT NULL,
  CONSTRAINT "PK_uporabniki" PRIMARY KEY ("id"),
  CONSTRAINT "uporabnisko_ime" UNIQUE ("uporabnisko_ime")
);

CREATE TABLE "kolesarske_poti" (
  "id" Serial NOT NULL,
  "ime" Character varying(100) NOT NULL,
  "dolzina_km" Real NOT NULL,
  "tezavnost" Character varying(50) NOT NULL,
  "priporocen_cas" Character varying(50) NOT NULL,
  "opis" Text,
  "uporabnik_id" Integer REFERENCES "uporabniki" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT "PK_kolesarske_poti" PRIMARY KEY ("id")
);

CREATE INDEX "IX_Relationship6" ON "kolesarske_poti" ("uporabnik_id");

CREATE TABLE "zanimivosti" (
  "id" Serial NOT NULL,
  "ime" Character varying(100) NOT NULL,
  "opis" Text,
  CONSTRAINT "PK_zanimivosti" PRIMARY KEY ("id")
);

CREATE TABLE "log_sprememb" (
  "id" Serial NOT NULL,
  "tabela" Character varying(50) NOT NULL,
  "operacija" Character varying(20) NOT NULL,
  "spremenjen_id" Integer NOT NULL,
  "cas_spremembe" Timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "staro_stanje" Jsonb,
  "novo_stanje" Jsonb,
  "uporabnik_id" Integer REFERENCES "uporabniki" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT "PK_log_sprememb" PRIMARY KEY ("id")
);

CREATE INDEX "IX_Relationship5" ON "log_sprememb" ("uporabnik_id");

CREATE TABLE "nastavitve" (
  "nastavitve_key" Character varying(50) NOT NULL,
  "nastavitve_atribut" Character varying(100) NOT NULL,
  CONSTRAINT "PK_nastavitve" PRIMARY KEY ("nastavitve_key")
);

CREATE TABLE "kolesarske_poti_kraji" (
  "id" Serial NOT NULL,
  "kraj_id" Integer REFERENCES "kraji" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION,
  "kolesarska_pot_id" Integer REFERENCES "kolesarske_poti" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT "PK_kolesarske_poti_kraji" PRIMARY KEY ("id")
);

CREATE INDEX "IX_Relationship1" ON "kolesarske_poti_kraji" ("kraj_id");
CREATE INDEX "IX_Relationship2" ON "kolesarske_poti_kraji" ("kolesarska_pot_id");

CREATE TABLE "kolesarske_poti_znamenitosti" (
  "id" Serial NOT NULL,
  "kolesarska_pot_id" Integer REFERENCES "kolesarske_poti" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION,
  "znamenitost_id" Integer REFERENCES "zanimivosti" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT "PK_kolesarske_poti_znamenitosti" PRIMARY KEY ("id")
);

CREATE INDEX "IX_Relationship3" ON "kolesarske_poti_znamenitosti" ("kolesarska_pot_id");
CREATE INDEX "IX_Relationship4" ON "kolesarske_poti_znamenitosti" ("znamenitost_id");

-- ============================================================================
-- 3. LOAD INITIAL DATA (OPTIONAL)
-- ============================================================================
INSERT INTO "nastavitve" ("nastavitve_key", "nastavitve_atribut") VALUES ('app_version', '1.0.0'), ('maintenance_mode', 'false');

-- ============================================================================
-- 4. CREATE FUNCTIONS AND PROCEDURES
-- ============================================================================

CREATE OR REPLACE FUNCTION vnesi_kolesarsko_pot_s_krajem_fn(
    p_ime TEXT, 
    p_dolzina REAL, 
    p_tezavnost TEXT,
    p_cas TEXT, 
    p_opis TEXT, 
    p_uporabnik_id BIGINT, 
    p_kraj_id BIGINT
) RETURNS TEXT AS $$
DECLARE 
    v_nova_pot_id BIGINT;
BEGIN
    INSERT INTO "kolesarske_poti" ("ime", "dolzina_km", "tezavnost", "priporocen_cas", "opis", "uporabnik_id")
    VALUES (p_ime, p_dolzina, p_tezavnost, p_cas, p_opis, p_uporabnik_id)
    RETURNING id INTO v_nova_pot_id;

    INSERT INTO "kolesarske_poti_kraji" ("kraj_id", "kolesarska_pot_id")
    VALUES (p_kraj_id, v_nova_pot_id);

    RETURN 'OK'::TEXT;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION uredi_kolesarsko_pot_fn(
    p_id BIGINT, 
    p_ime TEXT, 
    p_dolzina REAL,
    p_tezavnost TEXT, 
    p_cas TEXT, 
    p_opis TEXT,
    p_kraji_ids BIGINT[],
    p_znamenitosti_ids BIGINT[]
) RETURNS TEXT AS $$
BEGIN
    UPDATE "kolesarske_poti"
    SET "ime" = p_ime, "dolzina_km" = p_dolzina, "tezavnost" = p_tezavnost,
        "priporocen_cas" = p_cas, "opis" = p_opis
    WHERE "id" = p_id;

    DELETE FROM "kolesarske_poti_kraji" WHERE "kolesarska_pot_id" = p_id;
    IF p_kraji_ids IS NOT NULL THEN
        INSERT INTO "kolesarske_poti_kraji" ("kraj_id", "kolesarska_pot_id")
        SELECT unnest(p_kraji_ids), p_id;
    END IF;

    DELETE FROM "kolesarske_poti_znamenitosti" WHERE "kolesarska_pot_id" = p_id;
    IF p_znamenitosti_ids IS NOT NULL THEN
        INSERT INTO "kolesarske_poti_znamenitosti" ("kolesarska_pot_id", "znamenitost_id")
        SELECT p_id, unnest(p_znamenitosti_ids);
    END IF;

    RETURN 'OK'::TEXT;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION izbrisi_kolesarsko_pot_varna_fn(
    p_pot_id BIGINT, 
    p_izvajalec_id BIGINT
) RETURNS TEXT AS $$
DECLARE 
    v_vloga TEXT;
    v_owner_id BIGINT;
BEGIN
    SELECT vloga::TEXT INTO v_vloga FROM uporabniki WHERE id = p_izvajalec_id;
    SELECT uporabnik_id INTO v_owner_id FROM kolesarske_poti WHERE id = p_pot_id;

    IF v_vloga = 'ADMIN' OR v_vloga = 'admin' OR v_owner_id = p_izvajalec_id THEN
        DELETE FROM "kolesarske_poti_kraji" WHERE "kolesarska_pot_id" = p_pot_id;
        DELETE FROM "kolesarske_poti_znamenitosti" WHERE "kolesarska_pot_id" = p_pot_id;
        DELETE FROM "kolesarske_poti" WHERE "id" = p_pot_id;
        RETURN 'OK'::TEXT;
    ELSE
        RAISE EXCEPTION 'Dostop zavrnjen: Nimate pravic za brisanje.';
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION registriraj_uporabnika_fn(
    p_user TEXT, 
    p_pass TEXT
) RETURNS TEXT AS $$
BEGIN
    INSERT INTO "uporabniki" ("uporabnisko_ime", "geslo", "vloga")
    VALUES (p_user, p_pass, 'USER');
    RETURN 'OK'::TEXT;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION prijava_uporabnika(p_user TEXT)
RETURNS TABLE(u_id BIGINT, u_ime TEXT, u_vloga TEXT, u_geslo TEXT) AS $$
BEGIN
    RETURN QUERY
    SELECT id::BIGINT, uporabnisko_ime::TEXT, vloga::TEXT, geslo::TEXT
    FROM uporabniki
    WHERE uporabnisko_ime = p_user;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION zamenjaj_geslo_fn(p_u_id BIGINT, p_novo_geslo TEXT)
RETURNS TEXT AS $$
BEGIN
    UPDATE "uporabniki" SET "geslo" = p_novo_geslo WHERE "id" = p_u_id;
    RETURN 'OK'::TEXT;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION vnesi_kolesarsko_pot_fn(
    p_ime TEXT,
    p_dolzina REAL,
    p_tezavnost TEXT,
    p_cas TEXT,
    p_opis TEXT,
    p_uporabnik_id BIGINT,
    p_kraji_ids BIGINT[],
    p_znamenitosti_ids BIGINT[]
) RETURNS TEXT AS $$
DECLARE
    v_nova_pot_id BIGINT;
BEGIN
    INSERT INTO "kolesarske_poti" ("ime", "dolzina_km", "tezavnost", "priporocen_cas", "opis", "uporabnik_id")
    VALUES (p_ime, p_dolzina, p_tezavnost, p_cas, p_opis, p_uporabnik_id)
    RETURNING id INTO v_nova_pot_id;

    IF p_kraji_ids IS NOT NULL THEN
        INSERT INTO "kolesarske_poti_kraji" ("kraj_id", "kolesarska_pot_id")
        SELECT unnest(p_kraji_ids), v_nova_pot_id;
    END IF;

    IF p_znamenitosti_ids IS NOT NULL THEN
        INSERT INTO "kolesarske_poti_znamenitosti" ("kolesarska_pot_id", "znamenitost_id")
        SELECT v_nova_pot_id, unnest(p_znamenitosti_ids);
    END IF;
    
    RETURN 'OK'::TEXT;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION filtriraj_poti_po_tezavnosti(p_tez TEXT)
RETURNS SETOF kolesarske_poti AS $$
BEGIN
    RETURN QUERY SELECT * FROM kolesarske_poti WHERE tezavnost = p_tez;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION pridobi_vse_kraje()
RETURNS SETOF kraji AS $$
BEGIN
    RETURN QUERY SELECT * FROM kraji ORDER BY ime ASC;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION pridobi_vse_znamenitosti()
RETURNS TABLE (id BIGINT, ime TEXT, opis TEXT) AS $$
BEGIN
    RETURN QUERY SELECT id::BIGINT, ime::TEXT, opis::TEXT FROM "zanimivosti" ORDER BY ime ASC;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION pridobi_zanimivost_po_id(p_id BIGINT)
RETURNS TABLE (id BIGINT, ime TEXT, opis TEXT) AS $$
BEGIN
    RETURN QUERY SELECT z.id::BIGINT, z.ime::TEXT, z.opis::TEXT FROM "zanimivosti" z WHERE z.id = p_id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION pridobi_vse_poti()
RETURNS SETOF kolesarske_poti AS $$
BEGIN
    RETURN QUERY SELECT * FROM kolesarske_poti ORDER BY ime ASC;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION pridobi_pot_po_id(p_id BIGINT)
RETURNS SETOF kolesarske_poti AS $$
BEGIN
    RETURN QUERY SELECT * FROM kolesarske_poti WHERE id = p_id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION pridobi_nastavitev(p_key VARCHAR)
RETURNS VARCHAR AS $$
DECLARE
    v_vrednost VARCHAR;
BEGIN
    SELECT nastavitve_atribut INTO v_vrednost
    FROM nastavitve
    WHERE nastavitve_key = p_key;
    
    RETURN v_vrednost;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION pridobi_vse_nastavitve()
RETURNS SETOF nastavitve AS $$
BEGIN
    RETURN QUERY SELECT * FROM nastavitve;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION pridobi_uporabnika_po_id(p_id BIGINT)
RETURNS SETOF uporabniki AS $$
BEGIN
    RETURN QUERY SELECT * FROM uporabniki WHERE id = p_id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION pridobi_uporabnika_po_imenu(p_username TEXT)
RETURNS SETOF uporabniki AS $$
BEGIN
    RETURN QUERY SELECT * FROM uporabniki WHERE uporabnisko_ime = p_username;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION pridobi_nastavitev_po_kljucu_fn(p_key TEXT)
RETURNS SETOF nastavitve AS $$
BEGIN
    RETURN QUERY 
    SELECT * 
    FROM "nastavitve" 
    WHERE "nastavitve_key" = p_key;
END;
$$ LANGUAGE plpgsql;

-- ============================================================================
-- 5. TRIGGERS
-- ============================================================================

CREATE OR REPLACE FUNCTION log_kolesarske_poti_changes_fn()
RETURNS TRIGGER AS $$
DECLARE
    v_id BIGINT;
    v_user_id BIGINT;
    v_old_json JSONB;
    v_new_json JSONB;
BEGIN
    IF (TG_OP = 'INSERT') THEN
        v_id := (NEW).id;
        v_user_id := (NEW).uporabnik_id;
        v_new_json := row_to_json(NEW)::jsonb;
        INSERT INTO "log_sprememb" ("tabela", "operacija", "spremenjen_id", "cas_spremembe", "novo_stanje", "uporabnik_id")
        VALUES ('kolesarske_poti', 'INSERT', v_id, CURRENT_TIMESTAMP, v_new_json, v_user_id);
        RETURN NEW;
    ELSIF (TG_OP = 'UPDATE') THEN
        v_id := (NEW).id;
        v_user_id := (NEW).uporabnik_id;
        v_old_json := row_to_json(OLD)::jsonb;
        v_new_json := row_to_json(NEW)::jsonb;
        INSERT INTO "log_sprememb" ("tabela", "operacija", "spremenjen_id", "cas_spremembe", "staro_stanje", "novo_stanje", "uporabnik_id")
        VALUES ('kolesarske_poti', 'UPDATE', v_id, CURRENT_TIMESTAMP, v_old_json, v_new_json, v_user_id);
        RETURN NEW;
    ELSIF (TG_OP = 'DELETE') THEN
        v_id := (OLD).id;
        v_user_id := (OLD).uporabnik_id;
        v_old_json := row_to_json(OLD)::jsonb;
        INSERT INTO "log_sprememb" ("tabela", "operacija", "spremenjen_id", "cas_spremembe", "staro_stanje", "uporabnik_id")
        VALUES ('kolesarske_poti', 'DELETE', v_id, CURRENT_TIMESTAMP, v_old_json, v_user_id);
        RETURN OLD;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER audit_kolesarske_poti_trg
AFTER INSERT OR UPDATE OR DELETE ON "kolesarske_poti"
FOR EACH ROW EXECUTE FUNCTION log_kolesarske_poti_changes_fn();

CREATE OR REPLACE FUNCTION normalize_pot_fn()
RETURNS TRIGGER AS $$
DECLARE
    v_ime TEXT;
    v_km REAL;
BEGIN
    v_ime := (NEW).ime;
    v_km := (NEW).dolzina_km;
    IF v_ime IS NOT NULL THEN
        NEW.ime := TRIM(v_ime);
    END IF;
    IF v_km <= 0 THEN
        RAISE EXCEPTION 'Dolžina poti mora biti večja od 0 km.';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION normalize_kraj_fn()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.ime IS NOT NULL THEN
        NEW.ime := TRIM(NEW.ime);
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_normalize_pot
BEFORE INSERT OR UPDATE ON "kolesarske_poti"
FOR EACH ROW EXECUTE FUNCTION normalize_pot_fn();

CREATE TRIGGER trg_normalize_kraj
BEFORE INSERT OR UPDATE ON "kraji"
FOR EACH ROW EXECUTE FUNCTION normalize_kraj_fn();

CREATE OR REPLACE FUNCTION log_uporabniki_changes_fn()
RETURNS TRIGGER AS $$
DECLARE
    v_id BIGINT;
    v_name TEXT;
    v_role TEXT;
    v_old_name TEXT;
    v_old_role TEXT;
BEGIN
    IF (TG_OP = 'INSERT') THEN
        v_id := (NEW).id;
        v_name := (NEW).uporabnisko_ime;
        v_role := (NEW).vloga;
        INSERT INTO "log_sprememb" ("tabela", "operacija", "spremenjen_id", "cas_spremembe", "novo_stanje")
        VALUES ('uporabniki', 'INSERT', v_id, CURRENT_TIMESTAMP, jsonb_build_object('username', v_name, 'vloga', v_role));
        RETURN NEW;
    ELSIF (TG_OP = 'UPDATE') THEN
        v_id := (NEW).id;
        v_name := (NEW).uporabnisko_ime;
        v_role := (NEW).vloga;
        v_old_name := (OLD).uporabnisko_ime;
        v_old_role := (OLD).vloga;
        INSERT INTO "log_sprememb" ("tabela", "operacija", "spremenjen_id", "cas_spremembe", "staro_stanje", "novo_stanje")
        VALUES ('uporabniki', 'UPDATE', v_id, CURRENT_TIMESTAMP, 
                jsonb_build_object('username', v_old_name, 'vloga', v_old_role),
                jsonb_build_object('username', v_name, 'vloga', v_role));
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER audit_uporabniki_trg
AFTER INSERT OR UPDATE ON "uporabniki"
FOR EACH ROW EXECUTE FUNCTION log_uporabniki_changes_fn();
