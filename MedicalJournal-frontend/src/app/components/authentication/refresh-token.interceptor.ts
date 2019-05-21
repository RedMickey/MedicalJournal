import { Injectable } from "@angular/core";
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from "@angular/common/http";
import { AuthService } from "../../services/auth.service";
import { Observable, BehaviorSubject, throwError } from "rxjs";
import { catchError, take, filter, switchMap, finalize } from 'rxjs/operators';

@Injectable()
export class RefreshTokenInterceptor implements HttpInterceptor {
    private refreshTokenInProgress = false;
    // Refresh Token Subject tracks the current token, or is null if no token is currently
    // available (e.g. refresh pending).
    private refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject<any>(
        null
    );
    constructor(public authService: AuthService) {}

    private addTokenToRequest(request: HttpRequest<any>) : HttpRequest<any> {
        let accessToken = this.authService.getAccessToken(); 
        
        if (accessToken && !(request.url.includes("refreshToken") ||
            request.url.includes("login")||request.url.includes("sign-up"))) {
            request = request.clone({
                setHeaders: { 
                    Authorization: `Bearer ${accessToken}`
                }
            });
        }
        
        return request;
      }
    
    intercept(
        request: HttpRequest<any>,
        next: HttpHandler
    ): Observable<HttpEvent<any>> {
        return next.handle(this.addTokenToRequest(request)).pipe(catchError(error => {
            // We don't want to refresh token for some requests like login or refresh token itself
            // So we verify url and we throw an error if it's the case
            if (
                request.url.includes("refreshToken") ||
                request.url.includes("login") ||
                request.url.includes("sign-up")
            ) {
                // We do another check to see if refresh token failed
                // In this case we want to logout user and to redirect it to login page

                if (request.url.includes("refreshToken")) {
                    this.authService.logout();
                }

                return Observable.throw(error);
            }

            // If error status is different than 401 we want to skip refresh token
            // So we check that and throw the error if it's the case
            if (error.status !== 401) {
                return Observable.throw(error);
            }

            if (this.refreshTokenInProgress) {
                // If refreshTokenInProgress is true, we will wait until refreshTokenSubject has a non-null value
                // – which means the new token is ready and we can retry the request again
                return this.refreshTokenSubject.pipe(
                    filter(result => result !== null),
                    take(1),
                    switchMap(() => next.handle(this.addTokenToRequest(request))));
            } else {
                this.refreshTokenInProgress = true;

                // Set the refreshTokenSubject to null so that subsequent API calls will wait until the new token has been retrieved
                this.refreshTokenSubject.next(null);

                // Call auth.refreshAccessToken(this is an Observable that will be returned)
                return this.authService
                    .refreshAccessToken()
                    .pipe(
                        switchMap((res) => {
                            //When the call to refreshToken completes we reset the refreshTokenInProgress to false
                            // for the next time the token needs to be refreshed
                            this.refreshTokenInProgress = false;
                            this.refreshTokenSubject.next(
                                res.headers.get("Authorization").replace("Bearer", "")
                            );

                            return next.handle(this.addTokenToRequest(request));
                        })
                        ,catchError((err: any) => {
                            this.refreshTokenInProgress = false;

                            this.authService.logout();
                            return Observable.throw(error);
                        }));
            }
        }));
    }
}