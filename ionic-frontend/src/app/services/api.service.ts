import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { KolesarskaPot, PotDTO, LoginResponse, Uporabnik, Kraj, Zanimivost, Nastavitev } from '../models/models';

@Injectable({
    providedIn: 'root'
})
export class ApiService {
    private apiUrl = environment.apiUrl;

    constructor(private http: HttpClient) { }

    // --- Uporabnik (Auth) ---

    registracija(uporabnikoIme: string, geslo: string): Observable<any> {
        return this.http.post(`${this.apiUrl}/uporabniki/registracija`, { uporabnikoIme, geslo });
    }

    prijava(uporabnikoIme: string, geslo: string): Observable<LoginResponse> {
        return this.http.post<LoginResponse>(`${this.apiUrl}/uporabniki/prijava`, { uporabnikoIme, geslo });
    }

    spremembaGesla(id: number, novoGeslo: string): Observable<any> {
        return this.http.put(`${this.apiUrl}/uporabniki/${id}/sprememba-gesla`, { novoGeslo });
    }

    // --- Kolesarske Poti ---

    pridobiVsePoti(): Observable<KolesarskaPot[]> {
        return this.http.get<KolesarskaPot[]>(`${this.apiUrl}/poti`);
    }

    pridobiPot(id: number | string): Observable<KolesarskaPot> {
        return this.http.get<KolesarskaPot>(`${this.apiUrl}/poti/${id}`);
    }

    vnosPoti(potDTO: PotDTO): Observable<any> {
        console.log('Sending PotDTO:', potDTO);
        return this.http.post(`${this.apiUrl}/poti/vnos`, potDTO);
    }

    urediPot(id: number | string, potDTO: PotDTO): Observable<any> {
        return this.http.put(`${this.apiUrl}/poti/${id}`, potDTO);
    }

    izbrisiPot(potId: number | string, izvajalecId: number): Observable<any> {
        let params = new HttpParams().set('izvajalecId', izvajalecId.toString());
        return this.http.delete(`${this.apiUrl}/poti/${potId}`, { params });
    }

    filtrirajPoTezavnosti(tezavnost: string): Observable<KolesarskaPot[]> {
        return this.http.get<KolesarskaPot[]>(`${this.apiUrl}/poti/filter/tezavnost/${tezavnost}`);
    }

    // --- Kraji & Zanimivosti ---

    pridobiVseKraje(): Observable<Kraj[]> {
        return this.http.get<Kraj[]>(`${this.apiUrl}/kraji`);
    }

    pridobiVseZanimivosti(): Observable<Zanimivost[]> {
        return this.http.get<Zanimivost[]>(`${this.apiUrl}/zanimivosti`);
    }

    getNastavitve(): Observable<Nastavitev[]> {
        return this.http.get<Nastavitev[]>(`${this.apiUrl}/nastavitve`);
    }
}
