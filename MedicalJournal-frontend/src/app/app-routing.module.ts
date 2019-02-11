import { NgModule } from '@angular/core';

import { RouterModule, Routes } from '@angular/router';

import { TodayComponent } from './today/today.component';
import { PillsComponent } from './pills/pills.component';
import { MeasurementsComponent } from './measurements/measurements.component';

const routes: Routes = [
  { path: '', redirectTo: '/today', pathMatch: 'full' },
  { path: 'today', component: TodayComponent },
  { path: 'pills', component: PillsComponent },
  { path: 'measurements', component: MeasurementsComponent }
];

@NgModule({
  declarations: [],
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [
    RouterModule
  ]
})

export class AppRoutingModule { }
