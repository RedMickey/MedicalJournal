import { Injectable } from '@angular/core';
import { catchError, map, tap } from 'rxjs/operators';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../environments/environment.prod';
import { Observable, of } from 'rxjs';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  hdrs = new HttpHeaders({ 'Content-Type': 'application/json' });
  
  jwtHelper: any;

  constructor(private http: HttpClient) {
      this.jwtHelper = new JwtHelperService();
     }

  login(email:string, password:string) {
    return this.http.post('http://localhost:8090/login', 
    {
      "username": email,
      "password": password
    },
    {observe: 'response'})
    .pipe(
      tap(res => {
      console.log(res.headers.get("RefreshToken"));
      localStorage.setItem('access_token', res.headers.get("Authorization")); 
      localStorage.setItem('refresh_token', res.headers.get("RefreshToken"));
      let decoded = this.jwtHelper.decodeToken(res.headers.get("Authorization").replace("Bearer", ""));

      localStorage.setItem('user', JSON.stringify(
      {
        userName: decoded.sub,
        userId: decoded.userId
        }));
      console.log(decoded);
    }),
      catchError(this.handleError<any>('login'))
    );
  }

  logout() {
    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');
    localStorage.removeItem('user');
  }

  /*refreshToken(){
    return this.http.post('http://localhost:8090/auth/refreshToken', 
    {
      "refreshToken": localStorage.getItem('refresh_token')
    },
    {observe: 'response'})
    .pipe(
      tap(res => {
      console.log(res.headers.get("Authorization"));
      localStorage.setItem('access_token', res.headers.get("Authorization"));
    }),
      catchError(this.handleError<any>('refreshToken'))
    );
  }*/

  /*public get loggedIn(): boolean{
    return localStorage.getItem('access_token') !==  null;
  }*/

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
