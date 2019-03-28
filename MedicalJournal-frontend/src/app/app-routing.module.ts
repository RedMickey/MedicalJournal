import { NgModule } from '@angular/core';

import { RouterModule, Routes } from '@angular/router';

import { TodayComponent } from './today/today.component';
import { PillsComponent } from './pills/pills.component';
import { AddPillComponent } from './pills/add-pill/add-pill.component';
import { MeasurementsComponent } from './measurements/measurements.component';
import { AddMeasurementComponent } from './measurements/add-measurement/add-measurement.component';

const routes: Routes = [
  { path: '', redirectTo: '/today', pathMatch: 'full' },
  { path: 'today', component: TodayComponent },
  { path: 'pills', component: PillsComponent },
  { path: 'pills/add', component: AddPillComponent },
  { path: 'pills/course/:id', component: AddPillComponent },
  { path: 'measurements', component: MeasurementsComponent },
  { path: 'measurements/add', component: AddMeasurementComponent },
  { path: 'measurements/course/:id', component: AddMeasurementComponent },
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
