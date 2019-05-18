import { Injectable } from '@angular/core';
import { User } from '../models/User';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from './auth.service';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { environment } from '../../environments/environment.prod';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private UserUrl = environment.APIAddress + '/user';  // URL to web api

  userLS: any;

  constructor(
    private http: HttpClient,
    private authService: AuthService) {
      this.authService.currentUser.subscribe(x => this.userLS = x);
    }

  getUserById(): Observable<User> {
    return this.http.post<{userId: number}>
       (this.UserUrl + "/getUserById", 
       {
         "userId": this.userLS.userId
       },
       httpOptions)
      .pipe(
        catchError(this.handleError<any>('getUserById'))
      );
  }

  updateUser(user: User): Observable<any> {
    return this.http.post(this.UserUrl + "/updateUser", 
      user, httpOptions)
      .pipe(
        catchError(this.handleError<any>('updateUser'))
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
