import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private api: string = `${environment.backendUrl}/AngularUser`;

  constructor(private http: HttpClient) { }

  login(username: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.api}/Login`, { username: username, password: password });
  }

  downloadApp(): Observable<any> {
    return this.http.get<any>(`${this.api}/DownloadApplication`);
  }

  getCurrentUser() {
    const user = localStorage.getItem('user');
    if (user) {
      return JSON.parse(user);
    }
    else {
      return null;
    }
  }

  getCurrentUserToken() {
    const token = localStorage.getItem('token');
    if (token) {
      return token;
    }
    else {
      return null;
    }
  }
}
