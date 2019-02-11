import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import { PillReminderEntry } from '../models/PillReminderEntry';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

//@Injectable({ providedIn: 'root' })
@Injectable()
export class PillReminderEntryService {

  private PillReminderEntriesUrl = 'http://localhost:8090/today';  // URL to web api

  constructor(
    private http: HttpClient) { }

  /** POST PillReminderEntries by date from the server */
  getPillReminderEntriesByDate (date: Date): Observable<PillReminderEntry[]> {
    return this.http.post<PillReminderEntry[]>(this.PillReminderEntriesUrl + "/getReminders", {"date": date}, httpOptions)
      .pipe(
        catchError(this.handleError<PillReminderEntry[]>(' getPillReminderEntriesByDate', []))
      );
  }

  updatePillReminderEntry (updateReminderBody: any): Observable<any> {
    console.log(updateReminderBody);
    return this.http.post(this.PillReminderEntriesUrl + "/updateReminder", 
      {"isDone": updateReminderBody.isDone,
       "id": updateReminderBody.id,
       "date": updateReminderBody.date},
      httpOptions)
    .pipe(
      catchError(this.handleError<any>('updatePillReminderEntry'))
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