import { Injectable, Renderer2, RendererFactory2 } from '@angular/core';
import { ApiService } from './api.service';
import { Nastavitev } from '../models/models';

@Injectable({
    providedIn: 'root'
})
export class AppearanceService {
    private renderer: Renderer2;

    constructor(
        private apiService: ApiService,
        rendererFactory: RendererFactory2
    ) {
        this.renderer = rendererFactory.createRenderer(null, null);
    }

    applySettings() {
        this.apiService.getNastavitve().subscribe({
            next: (settings: Nastavitev[]) => {
                settings.forEach(setting => {
                    this.applySetting(setting.key, setting.value);
                });
            },
            error: (err) => {
                console.error('Error loading appearance settings', err);
            }
        });
    }

    private applySetting(key: string, value: string) {
        if (!value) return;
        console.log(`Applying appearance setting: ${key} = ${value}`);

        // Map database keys to CSS variables
        const cssVarMap: { [key: string]: string } = {
            'background-color': '--ion-background-color',
            'text-color': '--ion-text-color',
            'primary-color': '--ion-color-primary',
            'font-family': '--app-font-family'
        };

        const cssVar = cssVarMap[key] || `--app-${key}`;
        document.documentElement.style.setProperty(cssVar, value);

        // Dynamic primary color variations
        if (key === 'primary-color') {
            const normalizedHex = value.startsWith('#') ? value : '#' + value;
            const rgb = this.hexToRgb(normalizedHex);
            if (rgb) {
                document.documentElement.style.setProperty('--ion-color-primary-rgb', `${rgb.r}, ${rgb.g}, ${rgb.b}`);

                // Basic shade/tint/light generation
                document.documentElement.style.setProperty('--ion-color-primary-shade', this.adjustBrightness(normalizedHex, -20));
                document.documentElement.style.setProperty('--ion-color-primary-tint', this.adjustBrightness(normalizedHex, 20));
                document.documentElement.style.setProperty('--ion-color-primary-light', normalizedHex + '1a'); // 10% opacity
            }
        }

        if (key === 'font-family') {
            document.body.style.fontFamily = value;
        }
    }

    private adjustBrightness(hex: string, percent: number) {
        let r = parseInt(hex.slice(1, 3), 16);
        let g = parseInt(hex.slice(3, 5), 16);
        let b = parseInt(hex.slice(5, 7), 16);

        r = Math.min(255, Math.max(0, r + (r * percent / 100)));
        g = Math.min(255, Math.max(0, g + (g * percent / 100)));
        b = Math.min(255, Math.max(0, b + (b * percent / 100)));

        const hr = Math.round(r).toString(16).padStart(2, '0');
        const hg = Math.round(g).toString(16).padStart(2, '0');
        const hb = Math.round(b).toString(16).padStart(2, '0');

        return `#${hr}${hg}${hb}`;
    }

    private hexToRgb(hex: string) {
        const result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
        return result ? {
            r: parseInt(result[1], 16),
            g: parseInt(result[2], 16),
            b: parseInt(result[3], 16)
        } : null;
    }
}
