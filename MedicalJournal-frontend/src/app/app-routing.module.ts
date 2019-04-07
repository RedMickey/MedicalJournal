import { NgModule } from '@angular/core';

import { RouterModule, Routes } from '@angular/router';

import { LoginComponent } from './authentication/login/login.component';
import { RegistrationComponent } from './authentication/registration/registration.component';
import { TodayComponent } from './today/today.component';
import { PillsComponent } from './pills/pills.component';
import { AddPillComponent } from './pills/add-pill/add-pill.component';
import { MeasurementsComponent } from './measurements/measurements.component';
import { AddMeasurementComponent } from './measurements/add-measurement/add-measurement.component';
import { AddOneTimeMeasurementComponent } from './measurements/add-one-time-measurement/add-one-time-measurement.component';
import { AddOneTimePillComponent } from './pills/add-one-time-pill/add-one-time-pill.component';

const routes: Routes = [
  { path: '', redirectTo: '/today', pathMatch: 'full' },
  { path: 'today', component: TodayComponent },
  { path: 'pills', component: PillsComponent },
  { path: 'pills/add', component: AddPillComponent },
  { path: 'pills/addOneTime', component: AddOneTimePillComponent },
  { path: 'pills/course/:id', component: AddPillComponent },
  { path: 'measurements', component: MeasurementsComponent },
  { path: 'measurements/add', component: AddMeasurementComponent },
  { path: 'measurements/addOneTime', component: AddOneTimeMeasurementComponent },
  { path: 'measurements/course/:id', component: AddMeasurementComponent },
  { path: 'login', component: LoginComponent },
  { path: 'registration', component: RegistrationComponent },
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
