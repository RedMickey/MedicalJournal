import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import { PillReminderEntry } from '../models/PillReminderEntry';
import { MeasurementReminderEntry } from '../models/MeasurementReminderEntry';

import { AuthService } from './auth.service';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

//@Injectable({ providedIn: 'root' })
@Injectable()
export class ReminderEntriesService {

  private ReminderEntriesUrl = 'http://localhost:8090/today';  // URL to web api

  user: any;

  constructor(
    private http: HttpClient,
    private authService: AuthService) {
      //this.user = this.authService.currentUserValue;
      this.authService.currentUser.subscribe(x => this.user = x);
     }

  /** POST PillReminderEntries by date from the server */
  getPillReminderEntriesByDate (date: Date): Observable<PillReminderEntry[]> {
    return this.http.post<PillReminderEntry[]>(this.ReminderEntriesUrl + "/getPillReminders", 
      {
        "date": date,
        "userId": this.user.userId
      },
      httpOptions)
      .pipe(
        catchError(this.handleError<PillReminderEntry[]>(' getPillReminderEntriesByDate', []))
      );
  }

  /** POST MeasurementReminderEntries by date from the server */
  getMeasurementReminderEntriesByDate (date: Date): Observable<MeasurementReminderEntry[]> {
    return this.http.post<MeasurementReminderEntry[]>(this.ReminderEntriesUrl + "/getMeasurementReminders", 
      {
        "date": date,
        "userId": this.user.userId
      },
      httpOptions)
      .pipe(
        catchError(this.handleError<MeasurementReminderEntry[]>(' getMeasurementReminderEntriesByDate', []))
      );
  }

  updatePillReminderEntry (updateReminderBody: any): Observable<any> {
    console.log(updateReminderBody);
    return this.http.post(this.ReminderEntriesUrl + "/updatePillReminder", 
      {"isDone": updateReminderBody.isDone,
       "id": updateReminderBody.id,
       "date": updateReminderBody.date},
      httpOptions)
    .pipe(
      catchError(this.handleError<any>('updatePillReminderEntry'))
    );
  }

  updateMeasurementReminderEntry (updateReminderBody: any): Observable<any> {
    console.log(updateReminderBody);
    return this.http.post(this.ReminderEntriesUrl + "/updateMeasurementReminder", 
      {
        "isDone": updateReminderBody.isDone,
        "id": updateReminderBody.id,
        "date": updateReminderBody.date,
        "value1": updateReminderBody.value1,
        "value2": updateReminderBody.value2
      },
      httpOptions)
    .pipe(
      catchError(this.handleError<any>('updateMeasurementReminderEntry'))
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