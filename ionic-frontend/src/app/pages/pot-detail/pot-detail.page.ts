import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AlertController, ModalController, ToastController } from '@ionic/angular';
import { RouteEditorComponent } from 'src/app/components/route-editor/route-editor.component';
import { KolesarskaPot, LoginResponse } from 'src/app/models/models';
import { ApiService } from 'src/app/services/api.service';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-pot-detail',
  templateUrl: './pot-detail.page.html',
  styleUrls: ['./pot-detail.page.scss'],
  standalone: false
})
export class PotDetailPage implements OnInit {
  pot: KolesarskaPot | undefined;
  currentUser: LoginResponse | null = null;
  loading = true;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private apiService: ApiService,
    private authService: AuthService,
    private modalCtrl: ModalController,
    private alertCtrl: AlertController,
    private toastCtrl: ToastController
  ) { }

  ngOnInit() {
    this.authService.currentUser$.subscribe(user => this.currentUser = user);
    this.route.paramMap.subscribe(params => {
      console.log('Route params:', params);
      const id = params.get('id');
      console.log('Pot ID from route:', id);
      if (id) {
        this.loadPot(id); // Pass as string directly
      } else {
        console.error('No ID found in route');
      }
    });
  }

  loadPot(id: number | string) {
    console.log('Loading pot with ID:', id);
    this.loading = true;
    this.apiService.pridobiPot(id).subscribe({
      next: (data) => {
        this.pot = data;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.router.navigate(['/home']);
      }
    });
  }

  getDifficultyClass(difficulty: string): string {
    let normalized = difficulty ? difficulty.toUpperCase() : '';
    if (normalized.includes('TEZ') || normalized.includes('TEŽ')) normalized = 'TEZKO';
    else if (normalized.includes('SREDNJ')) normalized = 'SREDNJE';
    else if (normalized.includes('LAHK')) normalized = 'LAHKO';

    switch (normalized) {
      case 'LAHKO': return 'bg-green-50 border-green-200';
      case 'SREDNJE': return 'bg-yellow-50 border-yellow-200';
      case 'TEZKO': return 'bg-red-50 border-red-200';
      default: return 'bg-gray-50 border-gray-200';
    }
  }

  getDifficultyIconClass(difficulty: string): string {
    let normalized = difficulty ? difficulty.toUpperCase() : '';
    if (normalized.includes('TEZ') || normalized.includes('TEŽ')) normalized = 'TEZKO';
    else if (normalized.includes('SREDNJ')) normalized = 'SREDNJE';
    else if (normalized.includes('LAHK')) normalized = 'LAHKO';

    switch (normalized) {
      case 'LAHKO': return 'text-green-800';
      case 'SREDNJE': return 'text-yellow-800';
      case 'TEZKO': return 'text-red-800';
      default: return 'text-gray-800';
    }
  }

  async editPot() {
    if (!this.pot || !this.currentUser) return;

    const modal = await this.modalCtrl.create({
      component: RouteEditorComponent,
      componentProps: { pot: this.pot }
    });

    modal.onDidDismiss().then((result) => {
      if (result.data === 'saved') {
        if (this.pot) this.loadPot(this.pot.id);
        this.presentToast('Pot posodobljena');
      }
    });

    return await modal.present();
  }

  async deletePot() {
    if (!this.pot || !this.currentUser) return;

    const alert = await this.alertCtrl.create({
      header: 'Izbris poti',
      message: 'Ali ste prepričani?',
      buttons: [
        { text: 'Prekliči', role: 'cancel' },
        {
          text: 'Izbriši',
          handler: () => {
            if (this.pot && this.currentUser) {
              this.apiService.izbrisiPot(this.pot.id, this.currentUser.id).subscribe({
                next: () => {
                  this.presentToast('Pot izbrisana');
                  this.router.navigate(['/home']);
                },
                error: (err) => {
                  const errorMsg = err.error?.error || err.error?.message || err.message || 'Neznana napaka';
                  this.presentToast('Napaka: ' + errorMsg);
                }
              });
            }
          }
        }
      ]
    });
    await alert.present();
  }

  getDifficultyBarClass(barIndex: number): string {
    if (!this.pot) return 'bg-gray-200';

    const level = this.pot.tezavnost;
    // Console log only once per render check usually spammy, but useful for debugging now.
    if (barIndex === 0) console.log('Difficulty Level:', level);

    let active = false;

    // Normalizing case and mapping Slovenian values
    let upperLevel = level ? level.toUpperCase() : '';

    // Fuzzy match to handle "Težka", "Tezka", "Srednja", "Lahka" etc.
    if (upperLevel.includes('TEZ') || upperLevel.includes('TEŽ')) upperLevel = 'TEZKO';
    else if (upperLevel.includes('SREDNJ')) upperLevel = 'SREDNJE';
    else if (upperLevel.includes('LAHK')) upperLevel = 'LAHKO';

    if (barIndex === 0) active = true;
    if (barIndex === 1) active = upperLevel === 'SREDNJE' || upperLevel === 'TEZKO';
    if (barIndex === 2) active = upperLevel === 'TEZKO';

    if (active) {
      if (upperLevel === 'LAHKO') return 'bg-green-500';
      if (upperLevel === 'SREDNJE') return 'bg-yellow-500';
      if (upperLevel === 'TEZKO') return 'bg-red-500';
    }

    return 'bg-gray-200';
  }

  async presentToast(message: string) {
    const toast = await this.toastCtrl.create({
      message,
      duration: 2000,
      position: 'bottom'
    });
    toast.present();
  }
}
