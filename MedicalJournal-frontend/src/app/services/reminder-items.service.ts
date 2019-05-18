import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import { PillReminder } from '../models/PillReminder';
import { MeasurementReminder } from '../models/MeasurementReminder';
import { MeasurementType } from '../models/MeasurementType';
import { CycleDBInsertEntry } from '../models/CycleDBInsertEntry';
import { PillReminderCourse } from '../models/PillReminderCourse';
import { MeasurementReminderCourse } from '../models/MeasurementReminderCourse';
import { AuthService } from './auth.service';
import { environment } from '../../environments/environment.prod';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class ReminderItemsService {
  private ReminderItemsUrl = environment.APIAddress + '/reminders';  // URL to web api

  user: any;

  constructor(
    private http: HttpClient,
    private authService: AuthService) { 
      //this.user = this.authService.currentUserValue;
      this.authService.currentUser.subscribe(x => this.user = x);
    }
  
  /*  */
  getAllPillReminders (): Observable<PillReminder[]> {
    return this.http.post<PillReminder[]>(this.ReminderItemsUrl + "/getAllPillReminders", 
      { "userId": this.user.userId },
      httpOptions)
      .pipe(
        catchError(this.handleError<PillReminder[]>(' getAllPillReminders', []))
      );
  }

  /*  */
  getAllMeasurementReminders (): Observable<MeasurementReminder[]> {
    return this.http.post<MeasurementReminder[]>(this.ReminderItemsUrl + "/getAllMeasurementReminders",
      { "userId": this.user.userId },
      httpOptions)
      .pipe(
        catchError(this.handleError<MeasurementReminder[]>(' getAllMeasurementReminders', []))
      );
  }

  /*  */
  getMeasurementTypes (): Observable<MeasurementType[]> {
    return this.http.post<MeasurementType[]>(this.ReminderItemsUrl + "/getMeasurementTypes", httpOptions)
      .pipe(
        catchError(this.handleError<MeasurementType[]>(' getMeasurementTypes', []))
      );
  }

  sendPillReminderCourse(pillReminderCourse: PillReminderCourse,
     cycleDBEntry: CycleDBInsertEntry, type: number): Observable<any> {
      let path = "";
      if (type == 1)
        path = "/createPillReminderCourse";
      else
        path = "/updatePillReminderCourse"
      return this.http.post(this.ReminderItemsUrl + path, 
      {
        "cycleDBInsertEntry": cycleDBEntry,
        "pillReminderCourse": pillReminderCourse,
        "userId": this.user.userId
      },
      httpOptions)
    .pipe(
      catchError(this.handleError<any>('sendPillReminderCourse'))
    );
  }

  sendMeasurementReminderCourse(measurementReminderCourse: MeasurementReminderCourse,
     cycleDBEntry: CycleDBInsertEntry, type: number): Observable<any> {
      let path = "";
      if (type == 1)
        path = "/createMeasurementReminderCourse";
      else
        path = "/updateMeasurementReminderCourse"
      return this.http.post(this.ReminderItemsUrl + path, 
      {
        "cycleDBInsertEntry": cycleDBEntry,
        "measurementReminderCourse": measurementReminderCourse,
        "userId": this.user.userId
      },
      httpOptions)
    .pipe(
      catchError(this.handleError<any>('sendMeasurementReminderCourse'))
    );
  }

  getPillReminderCourse(pillReminderId: String): Observable<any> {
    return this.http.post<{pillReminderCourse: PillReminderCourse; 
                            cycleDBInsertEntry: CycleDBInsertEntry,
                            userId: number}>
     (this.ReminderItemsUrl + "/getPillReminderCourse", 
     {
       "entryUuid": pillReminderId,
       "userId": this.user.userId
     },
     httpOptions)
   .pipe(
     catchError(this.handleError<any>('getPillReminderCourse'))
   );
  }

  getMeasurementReminderCourse(measurementReminderId: String): Observable<any> {
    return this.http.post<{measurementReminderCourse: MeasurementReminderCourse; 
                            cycleDBInsertEntry: CycleDBInsertEntry,
                            userId: number}>
     (this.ReminderItemsUrl + "/getMeasurementReminderCourse", 
     {
       "entryUuid": measurementReminderId,
       "userId": this.user.userId
     },
     httpOptions)
   .pipe(
     catchError(this.handleError<any>('getMeasurementReminderCourse'))
   );
  }

  deleteReminderCourse(deletionReqBody: any): Observable<any> {
    return this.http.post(this.ReminderItemsUrl + "/deleteReminderCourse", 
      {
        "idReminder": deletionReqBody.idReminder,
        "idCycle": deletionReqBody.idCycle,
        "idWeekSchedule": deletionReqBody.idWeekSchedule,
        "courseType": deletionReqBody.courseType,
        "userId": this.user.userId
      },
      httpOptions)
    .pipe(
      catchError(this.handleError<any>('deleteReminderCourse'))
    );
  }

  sendOneTimeMeasurementReminderEntry(measurementReminderCourse: MeasurementReminderCourse,
    value1: number, value2: number): Observable<any> {
    return this.http.post(this.ReminderItemsUrl + "/createOneTimeMeasurementReminderEntry", 
    {
      "measurementReminderCourse": measurementReminderCourse,
      "value1": value1,
      "value2": value2,
      "userId": this.user.userId
    },
    httpOptions)
    .pipe(
      catchError(this.handleError<any>('sendOneTimeMeasurementReminderEntry'))
    );
  }

sendOneTimePillReminderEntry(pillReminderCourse: PillReminderCourse): Observable<any> {
  pillReminderCourse.userId = this.user.userId;
  return this.http.post(this.ReminderItemsUrl + "/createOneTimePillReminderEntry", 
    {
      "pillReminderCourse": pillReminderCourse
    },
    httpOptions)
    .pipe(
      catchError(this.handleError<any>('sendOneTimePillReminderEntry'))
    );
  }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T> (operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }
}
