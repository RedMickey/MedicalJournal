import { Injectable } from '@angular/core';
import { AuthService } from './auth.service';
import { environment } from '../../environments/environment.prod';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { PillAndMeasurementReminderEntries } from '../models/PillAndMeasurementReminderEntries';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class HistoryService {

  private HistoryUrl = environment.APIAddress + '/history';  // URL to web api

  user: any;

  constructor(    
    private http: HttpClient,
    private authService: AuthService) {
        this.authService.currentUser.subscribe(x => this.user = x);
      }

  getHistoryForPeriod (startDate: Date, endDate: Date): Observable<PillAndMeasurementReminderEntries[]> {
    return this.http.post<PillAndMeasurementReminderEntries[]>(this.HistoryUrl + "/getHistoryForPeriod", 
      {
        "userId": this.user.userId,
        "startDate": startDate,
        "endDate": endDate
      },
      httpOptions)
      .pipe(
        catchError(this.handleError<PillAndMeasurementReminderEntries[]>(' getHistoryForPeriod', []))
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
