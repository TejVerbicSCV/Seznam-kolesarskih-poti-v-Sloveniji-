import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { PotDetailPageRoutingModule } from './pot-detail-routing.module';
import { SharedComponentsModule } from '../../components/shared-components.module';

import { PotDetailPage } from './pot-detail.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    PotDetailPageRoutingModule,
    SharedComponentsModule
  ],
  declarations: [PotDetailPage]
})
export class PotDetailPageModule { }
