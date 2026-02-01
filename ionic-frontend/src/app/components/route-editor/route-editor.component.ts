import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ModalController, ToastController } from '@ionic/angular';
import { KolesarskaPot, Kraj, Zanimivost } from 'src/app/models/models';
import { ApiService } from 'src/app/services/api.service';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-route-editor',
  templateUrl: './route-editor.component.html',
  styleUrls: ['./route-editor.component.scss'],
  standalone: false
})
export class RouteEditorComponent implements OnInit {
  @Input() pot: KolesarskaPot | undefined;

  routeForm: FormGroup;
  kraji: Kraj[] = [];
  zanimivosti: Zanimivost[] = [];

  selectedKraji: number[] = [];
  selectedZanimivosti: number[] = [];

  constructor(
    private modalCtrl: ModalController,
    private fb: FormBuilder,
    private apiService: ApiService,
    private authService: AuthService,
    private toastCtrl: ToastController
  ) {
    this.routeForm = this.fb.group({
      ime: ['', Validators.required],
      dolzinaKm: ['', [Validators.required, Validators.min(0)]],
      tezavnost: ['LAHKO', Validators.required],
      priporocenCas: ['', Validators.required],
      opis: ['']
    });
  }

  ngOnInit() {
    this.loadData();
    if (this.pot) {
      this.routeForm.patchValue({
        ime: this.pot.ime,
        dolzinaKm: this.pot.dolzinaKm,
        tezavnost: this.pot.tezavnost,
        priporocenCas: this.pot.priporocenCas, // Assuming string provided, need to match
        opis: this.pot.opis
      });

      this.selectedKraji = this.pot.kraji.map(k => k.id);
      this.selectedZanimivosti = this.pot.zanimivosti.map(z => z.id);
    }
  }

  loadData() {
    this.apiService.pridobiVseKraje().subscribe(data => this.kraji = data);
    this.apiService.pridobiVseZanimivosti().subscribe(data => this.zanimivosti = data);
  }

  toggleKraj(id: number) {
    const index = this.selectedKraji.indexOf(id);
    if (index > -1) {
      this.selectedKraji.splice(index, 1);
    } else {
      this.selectedKraji.push(id);
    }
  }

  toggleZanimivost(id: number) {
    const index = this.selectedZanimivosti.indexOf(id);
    if (index > -1) {
      this.selectedZanimivosti.splice(index, 1);
    } else {
      this.selectedZanimivosti.push(id);
    }
  }

  isKrajSelected(id: number): boolean {
    return this.selectedKraji.includes(id);
  }

  isZanimivostSelected(id: number): boolean {
    return this.selectedZanimivosti.includes(id);
  }

  cancel() {
    return this.modalCtrl.dismiss(null, 'cancel');
  }

  save() {
    if (this.routeForm.valid) {
      const user = this.authService.currentUserValue;
      if (!user) return;

      const formValue = this.routeForm.value;
      const potDTO = {
        ...formValue,
        priporocenCas: formValue.priporocenCas.toString(), // Ensure string based on my decision before (or number if backend expects it to be casted?) Backend DTO is string.
        uporabnikId: user.id,
        krajiIds: this.selectedKraji,
        znamenitostiIds: this.selectedZanimivosti
      };

      if (this.pot) {
        // Edit logic
        this.apiService.urediPot(this.pot.id, potDTO).subscribe({
          next: () => this.modalCtrl.dismiss('saved', 'saved'),
          error: (err) => this.showError(err)
        });
      } else {
        // Create logic
        this.apiService.vnosPoti(potDTO).subscribe({
          next: () => this.modalCtrl.dismiss('saved', 'saved'),
          error: (err) => this.showError(err)
        });
      }
    } else {
      this.routeForm.markAllAsTouched();
    }
  }

  async showError(err: any) {
    const toast = await this.toastCtrl.create({
      message: 'Napaka pri shranjevanju: ' + (err.error?.message || err.message),
      duration: 3000,
      color: 'danger'
    });
    toast.present();
  }
}
