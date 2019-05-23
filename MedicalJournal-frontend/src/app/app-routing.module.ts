import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LoginComponent } from './components/authentication/login/login.component';
import { RegistrationComponent } from './components/authentication/registration/registration.component';
import { TodayComponent } from './components/today/today.component';
import { PillsComponent } from './components/pills/pills.component';
import { AddPillComponent } from './components/pills/add-pill/add-pill.component';
import { MeasurementsComponent } from './components/measurements/measurements.component';
import { AddMeasurementComponent } from './components/measurements/add-measurement/add-measurement.component';
import { AddOneTimeMeasurementComponent } from './components/measurements/add-one-time-measurement/add-one-time-measurement.component';
import { AddOneTimePillComponent } from './components/pills/add-one-time-pill/add-one-time-pill.component';
import { AuthGuard } from './guards/auth.guard';
import { UserComponent } from './components/authentication/user/user.component';
import { HistoryComponent } from './components/history/history.component';

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
  { path: 'history', component: HistoryComponent,
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
