import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Inject, Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { catchError, Observable, of, throwError } from "rxjs";
import { UserService } from "../services/user.service";

@Injectable()
export class AuthInterceptorService implements HttpInterceptor {

  constructor(private userService: UserService, private router: Router) {}

 intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.userService.getCurrentUserToken();
    if (token) {
     request = request.clone({
        setHeaders: {Authorization: `bearer ${token}`}
     });
    }

    return next.handle(request).pipe(catchError(x=> this.handleAuthError(x)));
  }

  private handleAuthError(err: HttpErrorResponse): Observable<any> {
    if (err.status === 401 || err.status === 403) {
      this.router.navigate(['/']);
      localStorage.clear();
      return of(err);
    }
    return throwError(err);
  }
}
