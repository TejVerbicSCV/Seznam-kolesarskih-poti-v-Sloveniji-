import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ModalController, ToastController } from '@ionic/angular';
import { ApiService } from 'src/app/services/api.service';
import { AuthService } from 'src/app/services/auth.service';

@Component({
    selector: 'app-password-change',
    templateUrl: './password-change.component.html',
    standalone: false
})
export class PasswordChangeComponent implements OnInit {
    passwordForm: FormGroup;
    isSubmitting = false;

    constructor(
        private fb: FormBuilder,
        private modalController: ModalController,
        private apiService: ApiService,
        private authService: AuthService,
        private toastController: ToastController
    ) {
        this.passwordForm = this.fb.group({
            novoGeslo: ['', [Validators.required, Validators.minLength(6)]],
            confirmGeslo: ['', [Validators.required]]
        }, { validator: this.passwordMatchValidator });
    }

    ngOnInit() { }

    passwordMatchValidator(g: FormGroup) {
        return g.get('novoGeslo')?.value === g.get('confirmGeslo')?.value
            ? null : { 'mismatch': true };
    }

    onSubmit() {
        if (this.passwordForm.valid) {
            const currentUser = this.authService.currentUserValue;
            if (!currentUser) {
                this.presentToast('Uporabnik ni prijavljen', 'danger');
                return;
            }

            this.isSubmitting = true;
            this.apiService.spremembaGesla(currentUser.id, this.passwordForm.value.novoGeslo).subscribe({
                next: () => {
                    this.isSubmitting = false;
                    this.presentToast('Geslo uspešno spremenjeno', 'success');
                    this.modalController.dismiss('success');
                },
                error: (err) => {
                    this.isSubmitting = false;
                    const errorMsg = err.error?.message || 'Napaka pri spremembi gesla';
                    this.presentToast(errorMsg, 'danger');
                }
            });
        }
    }

    cancel() {
        this.modalController.dismiss();
    }

    async presentToast(message: string, color: string) {
        const toast = await this.toastController.create({
            message: message,
            duration: 2000,
            color: color,
            position: 'bottom'
        });
        toast.present();
    }
}
