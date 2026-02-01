export interface Uporabnik {
    id: number;
    uporabnikoIme: string;
    vloga: 'USER' | 'ADMIN';
}

export interface Kraj {
    id: number;
    ime: string;
    postnaStavilka: number;
}

export interface Zanimivost {
    id: number;
    ime: string;
    opis: string;
    kraj?: Kraj;
}

export interface KolesarskaPot {
    id: number; // Keeping number here as TS usually handles JSON strings loosely, but let's see. 
    // Actually, if backend sends "123", parsed JSON in JS is string "123".
    // If we define it as number in TS, we get runtime string.
    // It's safer to keep it as number if we want to use it as number, but we must treat it as string if it comes as string.
    // Let's define it as any or string | number for safety.
    ime: string;
    dolzinaKm: number;
    tezavnost: 'LAHKO' | 'SREDNJE' | 'TEZKO';
    priporocenCas: string;
    opis: string;
    kraji: Kraj[];
    zanimivosti: Zanimivost[];
    uporabnik?: Uporabnik;
}

// DTOs for forms
export interface PotDTO {
    ime: string;
    dolzinaKm: number;
    tezavnost: string;
    priporocenCas: string;
    opis: string;
    uporabnikId: number;
    krajiIds: number[];
    znamenitostiIds: number[];
}

export interface LoginResponse {
    id: number;
    uporabnikoIme: string;
    vloga: string;
    message: string;
    token?: string; // If you add JWT later
}

export interface Nastavitev {
    key: string;
    value: string;
}
