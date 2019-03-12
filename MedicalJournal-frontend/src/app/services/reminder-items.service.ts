import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import { PillReminder } from '../models/PillReminder';
import { MeasurementReminder } from '../models/MeasurementReminder';
import { MeasurementType } from '../models/MeasurementType';

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
