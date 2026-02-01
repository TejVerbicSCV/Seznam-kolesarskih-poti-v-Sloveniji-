# Seznam kolesarskih poti v Sloveniji

Moderna spletna aplikacija za upravljanje in raziskovanje kolesarskih poti po Sloveniji. Projekt vključuje zmogljiv **Spring Boot** zaledni del (backend) in **Ionic** čelni del (frontend), ki deluje na različnih platformah.

## 🚀 Ključne funkcije

- **Upravljanje poti**: Dodajanje in pregledovanje kolesarskih poti s podrobnimi opisi (razdalja, zahtevnost, čas).
- **Lokacijska povezanost**: Povezovanje poti s specifičnimi kraji in regijami v Sloveniji.
- **Znamenitosti**: Odkrivanje zanimivih točk in znamenitosti ob kolesarskih poteh.
- **Varna avtentikacija**: Registracija in prijava uporabnikov s šifriranimi gesli (BCrypt).
- **Podatkovne podprograme**: Visoko varna arhitektura, kjer se vsi podatkovni procesi izvajajo preko shranjenih procedur in funkcij.

## 🛠️ Tehnologije

- **Backend**: Java 17, Spring Boot, Spring Data JPA, Hibernate.
- **Frontend**: Ionic Framework, HTML/JS, Vanilla CSS.
- **Podatkovna baza**: PostgreSQL / CockroachDB.

---

## 🏁 Navodila za začetek

### 1. Nastavitev podatkovne baze
Preverite, ali imate nameščen in zagnan PostgreSQL ali CockroachDB.

1. Ustvarite podatkovno bazo z imenom `projektna_naloga`.
2. Zaženite SQL skripto, ki se nahaja v mapi `backend`:
   - Zaženite `init_database.sql` za popolno postavitev tabel, funkcij in sprožilcev.

### 2. Nastavitev backenda (Spring Boot)
1. Pomaknite se v mapo `backend`.
2. **Lokalna konfiguracija**:
   - Kopirajte `src/main/resources/application-local.properties.example` v `src/main/resources/application-local.properties` ali pa ustvarite novo datoteko z imenom `application-local.properties`.
   - Posodobite `spring.datasource.url`, `username` in `password` s svojimi podatki.
3. **Okoljske spremenljivke**:
   - Kopirajte `.env.example` iz korenske mape v `.env` ali pa ustvarite novo datoteko z imenom `.env`.
   - Vpišite podatke za dostop do baze.
4. Zaženite aplikacijo:
   ```bash
   ./mvnw spring-boot:run
   ```

### 3. Nastavitev frontenda (Ionic)
1. Pomaknite se v mapo `ionic-frontend`.
2. Namestite odvisnosti:
   ```bash
   npm install
   ```
3. Zaženite razvojni strežnik:
   ```bash
   ionic serve
   ```
   Aplikacija bo na voljo na `http://localhost:8100`.

---

## 🔒 Varnostno opozorilo
Projekt uporablja `.gitignore` za zaščito občutljivih podatkov. **Nikoli** ne objavljajte svojih `.env` ali `application-local.properties` datotek v sistem za nadzor različic (Git). Vedno uporabite priložene `.example` datoteke kot predloge.


