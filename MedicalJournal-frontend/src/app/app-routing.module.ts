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
import { AuthGuard } from './guards/auth.guard';
import { UserComponent } from './authentication/user/user.component';

const routes: Routes = [
  { path: '', redirectTo: '/today', pathMatch: 'full' },
  { path: 'today', component: TodayComponent,
    canActivate: [AuthGuard] },
  { path: 'pills', component: PillsComponent,
    canActivate: [AuthGuard] },
  { path: 'pills/add', component: AddPillComponent,
    canActivate: [AuthGuard] },
  { path: 'pills/addOneTime', component: AddOneTimePillComponent,
    canActivate: [AuthGuard] },
  { path: 'pills/course/:id', component: AddPillComponent,
    canActivate: [AuthGuard] },
  { path: 'measurements', component: MeasurementsComponent,
    canActivate: [AuthGuard] },
  { path: 'measurements/add', component: AddMeasurementComponent,
    canActivate: [AuthGuard] },
  { path: 'measurements/addOneTime', component: AddOneTimeMeasurementComponent,
    canActivate: [AuthGuard] },
  { path: 'measurements/course/:id', component: AddMeasurementComponent,
    canActivate: [AuthGuard] },
  { path: 'user', component: UserComponent,
    canActivate: [AuthGuard] },
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
