import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from 'src/app/services/api.service';
import { AuthService } from 'src/app/services/auth.service';
import { ToastController, LoadingController } from '@ionic/angular';

@Component({
  selector: 'app-login',
  templateUrl: './login.page.html',
  styleUrls: ['./login.page.scss'],
  standalone: false
})
export class LoginPage implements OnInit {
  loginForm: FormGroup;
  errorMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private apiService: ApiService,
    private authService: AuthService,
    private router: Router,
    private toastController: ToastController,
    private loadingController: LoadingController
  ) {
    this.loginForm = this.fb.group({
      uporabnikoIme: ['', Validators.required],
      geslo: ['', Validators.required]
    });
  }

  ngOnInit() {
  }

  async onLogin() {
    if (this.loginForm.valid) {
      const loading = await this.loadingController.create({
        message: 'Prijavljanje...',
      });
      await loading.present();

      const { uporabnikoIme, geslo } = this.loginForm.value;
      this.apiService.prijava(uporabnikoIme, geslo).subscribe({
        next: async (res) => {
          await loading.dismiss();
          this.authService.login(res);
          this.router.navigate(['/home']);
        },
        error: async (err) => {
          await loading.dismiss();
          console.error('Login error:', err);

          if (err.error && typeof err.error === 'object' && err.error.message) {
            this.errorMessage = err.error.message;
          } else if (typeof err.error === 'string') {
            this.errorMessage = err.error;
          } else {
            this.errorMessage = err.message || 'Prijava ni uspela. Preverite podatke.';
          }

          const toast = await this.toastController.create({
            message: this.errorMessage,
            duration: 3000,
            color: 'danger',
            position: 'bottom'
          });
          toast.present();
        }
      });
    } else {
      this.loginForm.markAllAsTouched();
    }
  }

  goToRegister() {
    this.router.navigate(['/register']);
  }
}
