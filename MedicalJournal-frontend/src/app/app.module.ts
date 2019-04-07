import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MainNavComponent } from './main-nav/main-nav.component';
import { LayoutModule } from '@angular/cdk/layout';
import { AppRoutingModule } from './app-routing.module';
import { TodayComponent } from './today/today.component';
import { FlexLayoutModule } from '@angular/flex-layout';
import { ReminderEntryComponent } from './reminder-entry/reminder-entry.component';
import { HttpClientModule } from "@angular/common/http";
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {EcoFabSpeedDialModule} from '@ecodev/fab-speed-dial';
import { MatButtonModule, 
  MatIconModule, 
  MatListModule,
  MatToolbarModule,
  MatSidenavModule,
  MatGridListModule,
  MatCardModule,
  MatCheckboxModule,

  MatAutocompleteModule,
  MatBadgeModule,
  MatBottomSheetModule,
  MatButtonToggleModule,
  MatChipsModule,
  MatDatepickerModule,
  MatDialogModule,
  MatDividerModule,
  MatExpansionModule,
  MatInputModule,
  MatMenuModule,
  MatNativeDateModule,
  MatPaginatorModule,
  MatProgressBarModule,
  MatProgressSpinnerModule,
  MatRadioModule,
  MatRippleModule,
  MatSelectModule,
  MatSliderModule,
  MatSlideToggleModule,
  MatSnackBarModule,
  MatSortModule,
  MatStepperModule,
  MatTableModule,
  MatTabsModule,
  MatTooltipModule,
  MatTreeModule,
} from '@angular/material';

import { ReminderEntriesService } from './services/reminder-entries.service';

import { registerLocaleData } from '@angular/common';
import localeRu from '@angular/common/locales/ru';
import { PillsComponent } from './pills/pills.component';
import { MeasurementsComponent } from './measurements/measurements.component';
import { from } from 'rxjs';
import { MeasurementDialogComponent } from './reminder-entry/measurement-dialog/measurement-dialog.component';
import { PillReminderListItemComponent } from './pills/pill-reminder-list-item/pill-reminder-list-item.component';
import { MeasurementReminderListItemComponent } from './measurements/measurement-reminder-list-item/measurement-reminder-list-item.component';
import { ChoosingMeasurementTypeDialogComponent } from './measurements/choosing-measurement-type-dialog/choosing-measurement-type-dialog.component';
import { AddMeasurementComponent } from './measurements/add-measurement/add-measurement.component';
import { AddPillComponent } from './pills/add-pill/add-pill.component';
import { NgxMaterialTimepickerModule } from 'ngx-material-timepicker';
import { AddOneTimeMeasurementComponent } from './measurements/add-one-time-measurement/add-one-time-measurement.component';
import { AddOneTimePillComponent } from './pills/add-one-time-pill/add-one-time-pill.component';
import { ChoosingPillReminderDialogComponent } from './pills/add-one-time-pill/choosing-pill-reminder-dialog/choosing-pill-reminder-dialog.component';
import { LoginComponent } from './authentication/login/login.component';
import { RegistrationComponent } from './authentication/registration/registration.component';
import { UserComponent } from './authentication/user/user.component';
//import { JwtModule } from '@auth0/angular-jwt';

//import { Router } from '@angular/router';
//import { JwtHelperService } from '@auth0/angular-jwt';
//import { AuthService } from './services/auth.service';

registerLocaleData(localeRu, 'ru');

@NgModule({
  declarations: [
    AppComponent,
    MainNavComponent,
    TodayComponent,
    ReminderEntryComponent,
    PillsComponent,
    MeasurementsComponent,
    MeasurementDialogComponent,
    PillReminderListItemComponent,
    MeasurementReminderListItemComponent,
    ChoosingMeasurementTypeDialogComponent,
    AddMeasurementComponent,
    AddPillComponent,
    AddOneTimeMeasurementComponent,
    AddOneTimePillComponent,
    ChoosingPillReminderDialogComponent,
    LoginComponent,
    RegistrationComponent,
    UserComponent
  ],
  imports: [
    BrowserModule,
    FlexLayoutModule,
    BrowserAnimationsModule,
    LayoutModule,
    MatButtonModule,
    MatIconModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule, 
    ReactiveFormsModule,
    EcoFabSpeedDialModule,
    NgxMaterialTimepickerModule,
    
    MatListModule,
    MatToolbarModule,
    MatSidenavModule,
    MatGridListModule,
    MatCardModule,
    MatCheckboxModule,
    MatAutocompleteModule,
    MatBadgeModule,
    MatBottomSheetModule,
    MatButtonToggleModule,
    MatChipsModule,
    MatDatepickerModule,
    MatDialogModule,
    MatDividerModule,
    MatExpansionModule,
    MatInputModule,
    MatMenuModule,
    MatNativeDateModule,
    MatPaginatorModule,
    MatProgressBarModule,
    MatProgressSpinnerModule,
    MatRadioModule,
    MatRippleModule,
    MatSelectModule,
    MatSliderModule,
    MatSlideToggleModule,
    MatSnackBarModule,
    MatSortModule,
    MatStepperModule,
    MatTableModule,
    MatTabsModule,
    MatTooltipModule,
    MatTreeModule,

    /*JwtModule.forRoot({
      config: {
        tokenGetter: tokenGetter,
        whitelistedDomains: ['localhost:4000'],
        blacklistedRoutes: ['example.com/examplebadroute/']
      }
    })*/
  ],
  entryComponents: [
    MeasurementDialogComponent,
    ChoosingMeasurementTypeDialogComponent,
    ChoosingPillReminderDialogComponent,
  ],
  providers: [
    ReminderEntriesService,
    //AuthService,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

/*function getAccessToken(router: Router, authService: AuthService): Promise<string> {

  let jwtHelper = new JwtHelperService();
  
  let accessToken = localStorage.getItem('access_token');
  
  if (accessToken == '' || !accessToken || accessToken == undefined || accessToken == null) {
      router.navigate(['./admin/login']);
      return;
  }
  
  if (jwtHelper.isTokenExpired(accessToken)) {
  
      let waitPeriod = (!refreshTokenService.wait);
  
      refreshTokenService.wait = true;
  
      return new Promise((resolve, reject) => {
  
          if (waitPeriod) {
              refreshTokenService.refreshToken(accessToken).subscribe((res: any) => {
                  res = res.json();
  
                  if (res.token) {
                      localStorage.setItem('JWToken', res.token);
                      resolve(res.token);
                      refreshTokenService.wait = false;
                  } else {
                      localStorage.removeItem('JWToken');
                      router.navigate(['./admin/login']);
                  }
  
              });
          } else {
              let interval = setInterval(function () {
                  if(refreshTokenService.wait == false) {
                      resolve(localStorage.getItem('JWToken'));
                      clearInterval(interval);
                  }
              }, 500);
          }
  
      });
  } else {
      return Promise.resolve(accessToken);
  }
}*/