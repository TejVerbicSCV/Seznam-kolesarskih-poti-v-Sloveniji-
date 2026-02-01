import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from 'src/app/services/api.service';
import { ToastController, LoadingController } from '@ionic/angular';

@Component({
  selector: 'app-register',
  templateUrl: './register.page.html',
  styleUrls: ['./register.page.scss'],
  standalone: false,
})
export class RegisterPage implements OnInit {
  registerForm: FormGroup;
  errorMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private apiService: ApiService,
    private router: Router,
    private toastController: ToastController,
    private loadingController: LoadingController
  ) {
    this.registerForm = this.fb.group({
      uporabnikoIme: ['', [Validators.required, Validators.minLength(3)]],
      geslo: ['', [Validators.required, Validators.minLength(6)]],
      potrdiGeslo: ['', Validators.required]
    }, { validators: this.passwordMatchValidator });
  }

  ngOnInit() {
  }

  passwordMatchValidator(g: FormGroup) {
    return g.get('geslo')?.value === g.get('potrdiGeslo')?.value
      ? null : { mismatch: true };
  }

  async onRegister() {
    if (this.registerForm.valid) {
      const loading = await this.loadingController.create({
        message: 'Registracija...',
      });
      await loading.present();

      const { uporabnikoIme, geslo } = this.registerForm.value;
      this.apiService.registracija(uporabnikoIme, geslo).subscribe({
        next: async () => {
          await loading.dismiss();
          const toast = await this.toastController.create({
            message: 'Registracija uspešna! Prosimo, prijavite se.',
            duration: 3000,
            color: 'success',
            position: 'bottom'
          });
          toast.present();
          this.router.navigate(['/login']);
        },
        error: async (err) => {
          await loading.dismiss();
          console.error('Registration error:', err);

          if (err.error && typeof err.error === 'object' && err.error.message) {
            this.errorMessage = err.error.message;
          } else if (typeof err.error === 'string') {
            this.errorMessage = err.error;
          } else {
            this.errorMessage = err.message || 'Registracija ni uspela. Poskusite z drugim uporabniškim imenom.';
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
      this.registerForm.markAllAsTouched();
    }
  }

  goToLogin() {
    this.router.navigate(['/login']);
  }
}
