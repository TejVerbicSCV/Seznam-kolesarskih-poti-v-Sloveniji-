import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from 'src/app/services/api.service';
import { AuthService } from 'src/app/services/auth.service';
import { KolesarskaPot, LoginResponse } from 'src/app/models/models';
import { ModalController, AlertController, ToastController } from '@ionic/angular';
import { RouteEditorComponent } from 'src/app/components/route-editor/route-editor.component';
import { PasswordChangeComponent } from 'src/app/components/password-change/password-change.component';

@Component({
  selector: 'app-home',
  templateUrl: './home.page.html',
  styleUrls: ['./home.page.scss'],
  standalone: false
})
export class HomePage implements OnInit {
  poti: KolesarskaPot[] = [];
  filteredPoti: KolesarskaPot[] = [];
  currentUser: LoginResponse | null = null;
  currentFilter: string = 'VSE'; // VSE, LAHKO, SREDNJE, TEZKO

  stats = {
    count: 0,
    distance: 0,
    duration: 0
  };

  constructor(
    private apiService: ApiService,
    private authService: AuthService,
    private router: Router,
    private modalController: ModalController,
    private alertController: AlertController,
    private toastController: ToastController
  ) { }

  ngOnInit() {
    this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
    });
  }

  ionViewWillEnter() {
    this.loadPoti();
  }

  loadPoti() {
    this.apiService.pridobiVsePoti().subscribe({
      next: (data) => {
        this.poti = data;
        this.applyFilter();
        this.calculateStats();
      },
      error: (err) => {
        console.error('Error loading paths', err);
      }
    });
  }

  applyFilter() {
    if (this.currentFilter === 'VSE') {
      this.filteredPoti = this.poti;
    } else {
      this.filteredPoti = this.poti.filter(p => p.tezavnost === this.currentFilter);
    }
  }

  setFilter(filter: string) {
    this.currentFilter = filter;
    this.applyFilter();
  }

  calculateStats() {
    this.stats.count = this.poti.length;
    this.stats.distance = this.poti.reduce((acc, curr) => acc + (curr.dolzinaKm || 0), 0);

    if (this.poti.length > 0) {
      const totalHours = this.poti.reduce((acc, curr) => {
        const hours = parseFloat(curr.priporocenCas);
        return acc + (isNaN(hours) ? 0 : hours);
      }, 0);
      this.stats.duration = totalHours / this.poti.length;
    } else {
      this.stats.duration = 0;
    }
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  async openAddRouteModal() {
    if (!this.currentUser) {
      this.router.navigate(['/login']);
      return;
    }

    const modal = await this.modalController.create({
      component: RouteEditorComponent
    });

    modal.onDidDismiss().then((result) => {
      if (result.data === 'saved') {
        this.loadPoti();
        this.presentToast('Pot uspešno dodana', 'success');
      }
    });

    return await modal.present();
  }

  async openEditRouteModal(pot: KolesarskaPot, event: Event) {
    event.stopPropagation();
    if (!this.currentUser) return;

    const modal = await this.modalController.create({
      component: RouteEditorComponent,
      componentProps: { pot: pot }
    });

    modal.onDidDismiss().then((result) => {
      if (result.data === 'saved') {
        this.loadPoti();
        this.presentToast('Pot uspešno urejena', 'success');
      }
    });

    return await modal.present();
  }

  async openPasswordChangeModal() {
    if (!this.currentUser) return;

    const modal = await this.modalController.create({
      component: PasswordChangeComponent,
      cssClass: 'auto-height-modal'
    });

    return await modal.present();
  }

  goToDetail(id: number | string) {
    console.log('Navigating to detail for ID:', id);
    this.router.navigate(['/pot-detail', id]);
  }

  async confirmDelete(id: number | string, event: Event) {
    event.stopPropagation();
    const alert = await this.alertController.create({
      header: 'Izbris poti',
      message: 'Ali ste prepričani, da želite izbrisati to pot?',
      buttons: [
        {
          text: 'Prekliči',
          role: 'cancel'
        },
        {
          text: 'Izbriši',
          handler: () => {
            this.deleteRoute(id);
          }
        }
      ]
    });
    await alert.present();
  }

  deleteRoute(id: number | string) {
    if (!this.currentUser) return;
    this.apiService.izbrisiPot(id, this.currentUser.id).subscribe({
      next: () => {
        this.loadPoti();
        this.presentToast('Pot izbrisana', 'success');
      },
      error: (err) => {
        const errorMsg = err.error?.error || err.error?.message || err.message || 'Neznana napaka';
        this.presentToast('Napaka pri brisanju: ' + errorMsg, 'danger');
      }
    });
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

  getDifficultyColor(difficulty: string): string {
    switch (difficulty) {
      case 'LAHKO': return 'bg-green-100 text-green-800 border-green-200';
      case 'SREDNJE': return 'bg-yellow-100 text-yellow-800 border-yellow-200';
      case 'TEZKO': return 'bg-red-100 text-red-800 border-red-200';
      default: return 'bg-gray-100 text-gray-800';
    }
  }
}
