import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { PotDetailPage } from './pot-detail.page';

const routes: Routes = [
  {
    path: '',
    component: PotDetailPage
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PotDetailPageRoutingModule {}
