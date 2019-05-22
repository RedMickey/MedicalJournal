import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MainNavComponent } from './components/main-nav/main-nav.component';
import { LayoutModule } from '@angular/cdk/layout';
import { AppRoutingModule } from './app-routing.module';
import { TodayComponent } from './components/today/today.component';
import { FlexLayoutModule } from '@angular/flex-layout';
import { ReminderEntryComponent } from './components/reminder-entry/reminder-entry.component';
import { HttpClientModule, HttpClient, HTTP_INTERCEPTORS } from "@angular/common/http";
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
  MatPaginatorIntl,
} from '@angular/material';

import { ReminderEntriesService } from './services/reminder-entries.service';

import { registerLocaleData } from '@angular/common';
import localeRu from '@angular/common/locales/ru';
import { PillsComponent } from './components/pills/pills.component';
import { MeasurementsComponent } from './components/measurements/measurements.component';
import { from } from 'rxjs';
import { MeasurementDialogComponent } from './components/reminder-entry/measurement-dialog/measurement-dialog.component';
import { PillReminderListItemComponent } from './components/pills/pill-reminder-list-item/pill-reminder-list-item.component';
import { MeasurementReminderListItemComponent } from './components/measurements/measurement-reminder-list-item/measurement-reminder-list-item.component';
import { ChoosingMeasurementTypeDialogComponent } from './components/measurements/choosing-measurement-type-dialog/choosing-measurement-type-dialog.component';
import { AddMeasurementComponent } from './components/measurements/add-measurement/add-measurement.component';
import { AddPillComponent } from './components/pills/add-pill/add-pill.component';
import { NgxMaterialTimepickerModule } from 'ngx-material-timepicker';
import { AddOneTimeMeasurementComponent } from './components/measurements/add-one-time-measurement/add-one-time-measurement.component';
import { AddOneTimePillComponent } from './components/pills/add-one-time-pill/add-one-time-pill.component';
import { ChoosingPillReminderDialogComponent } from './components/pills/add-one-time-pill/choosing-pill-reminder-dialog/choosing-pill-reminder-dialog.component';
import { LoginComponent } from './components/authentication/login/login.component';
import { RegistrationComponent } from './components/authentication/registration/registration.component';
import { UserComponent } from './components/authentication/user/user.component';
//import { JwtInterceptor } from './authentication/jwt.interceptor';
import { RefreshTokenInterceptor } from './components/authentication/refresh-token.interceptor';
import { ChangingPasswordDialogComponent } from './components/authentication/user/changing-password-dialog/changing-password-dialog.component';
import { HistoryComponent, HistoryMatPaginatorIntl } from './components/history/history.component';
import { DayHistoryItemComponent } from './components/history/day-history-item/day-history-item.component';

//import { JwtModule } from '@auth0/angular-jwt';

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
    UserComponent,
    ChangingPasswordDialogComponent,
    HistoryComponent,
    DayHistoryItemComponent,
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
  ],
  entryComponents: [
    MeasurementDialogComponent,
    ChoosingMeasurementTypeDialogComponent,
    ChoosingPillReminderDialogComponent,
    ChangingPasswordDialogComponent,
  ],
  providers: [
    ReminderEntriesService,
    //{ provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: RefreshTokenInterceptor, multi: true },
    { provide: MatPaginatorIntl, useClass: HistoryMatPaginatorIntl}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }