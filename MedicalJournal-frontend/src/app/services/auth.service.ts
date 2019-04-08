import { Injectable } from '@angular/core';
import { catchError, map, tap } from 'rxjs/operators';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../environments/environment.prod';
import { Observable, of, BehaviorSubject } from 'rxjs';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private currentUserSubject: BehaviorSubject<any>;
  public currentUser: Observable<any>;
  jwtHelper: any;

  constructor(private http: HttpClient) {
      this.jwtHelper = new JwtHelperService();
      this.currentUserSubject = new BehaviorSubject<any>(JSON.parse(localStorage.getItem('user')));
      this.currentUser = this.currentUserSubject.asObservable();
    }

  public get currentUserValue(): any {
    return this.currentUserSubject.value;
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
      localStorage.setItem('access_token', res.headers.get("Authorization").replace("Bearer ", "")); 
      localStorage.setItem('refresh_token', res.headers.get("RefreshToken"));
      let decoded = this.jwtHelper.decodeToken(res.headers.get("Authorization"));

      localStorage.setItem('user', JSON.stringify(
      {
        userName: decoded.sub,
        userId: decoded.userId
      }));
      this.currentUserSubject.next({
        userName: decoded.sub,
        userId: decoded.userId
      });
      console.log(decoded);
    }),
      catchError(this.handleError<any>('login'))
    );
  }

  logout() {
    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');
    localStorage.removeItem('user');
    this.currentUserSubject.next(null);
  }

  refreshAccessToken(){
    return this.http.post('http://localhost:8090/auth/refreshToken', 
    {
      "refreshToken": localStorage.getItem('refresh_token')
    },
    {observe: 'response'})
    .pipe(
      tap(res => {
      localStorage.setItem('access_token', res.headers.get("Authorization").replace("Bearer", ""));
    }),
      catchError(this.handleError<any>('refreshToken'))
    );
  }

  public loggedIn(): boolean{
    return localStorage.getItem('access_token') !==  null;
  }

  public getAccessToken(): string{
    return localStorage.getItem('access_token');
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
