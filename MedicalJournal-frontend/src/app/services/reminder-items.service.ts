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

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class ReminderItemsService {
  private ReminderItemsUrl = 'http://localhost:8090/reminders';  // URL to web api

  constructor(
    private http: HttpClient) { }
  
  /*  */
  getAllPillReminders (): Observable<PillReminder[]> {
    return this.http.post<PillReminder[]>(this.ReminderItemsUrl + "/getAllPillReminders", httpOptions)
      .pipe(
        catchError(this.handleError<PillReminder[]>(' getAllPillReminders', []))
      );
  }

  /*  */
  getAllMeasurementReminders (): Observable<MeasurementReminder[]> {
    return this.http.post<MeasurementReminder[]>(this.ReminderItemsUrl + "/getAllMeasurementReminders", httpOptions)
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
        "pillReminderCourse": pillReminderCourse
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
        "measurementReminderCourse": measurementReminderCourse
      },
      httpOptions)
    .pipe(
      catchError(this.handleError<any>('sendMeasurementReminderCourse'))
    );
  }

  getPillReminderCourse(pillReminderId: String): Observable<any> {
    return this.http.post<{pillReminderCourse: PillReminderCourse; 
                            cycleDBInsertEntry: CycleDBInsertEntry}>
     (this.ReminderItemsUrl + "/getPillReminderCourse", 
     {
       "pillReminderId": pillReminderId
     },
     httpOptions)
   .pipe(
     catchError(this.handleError<any>('getPillReminderCourse'))
   );
  }

  getMeasurementReminderCourse(measurementReminderId: String): Observable<any> {
    return this.http.post<{measurementReminderCourse: MeasurementReminderCourse; 
                           cycleDBInsertEntry: CycleDBInsertEntry}>
     (this.ReminderItemsUrl + "/getMeasurementReminderCourse", 
     {
       "measurementReminderId": measurementReminderId
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
      "courseType": deletionReqBody.courseType
    },
    httpOptions)
  .pipe(
    catchError(this.handleError<any>('deleteReminderCourse'))
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
