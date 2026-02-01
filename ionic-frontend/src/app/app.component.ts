import { Component } from '@angular/core';
import { AppearanceService } from './services/appearance.service';

@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  styleUrls: ['app.component.scss'],
  standalone: false,
})
export class AppComponent {
  constructor(private appearanceService: AppearanceService) {
    this.appearanceService.applySettings();
  }
}
