import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { LoginResponse } from '../models/models';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private currentUserSubject = new BehaviorSubject<LoginResponse | null>(null);
    public currentUser$ = this.currentUserSubject.asObservable();

    constructor() {
        this.loadUserFromStorage();
    }

    private loadUserFromStorage() {
        const user = localStorage.getItem('currentUser');
        if (user) {
            this.currentUserSubject.next(JSON.parse(user));
        }
    }

    login(user: LoginResponse) {
        localStorage.setItem('currentUser', JSON.stringify(user));
        this.currentUserSubject.next(user);
    }

    logout() {
        localStorage.removeItem('currentUser');
        this.currentUserSubject.next(null);
    }

    get currentUserValue(): LoginResponse | null {
        return this.currentUserSubject.value;
    }

    isAuthenticated(): boolean {
        return !!this.currentUserSubject.value;
    }
}
