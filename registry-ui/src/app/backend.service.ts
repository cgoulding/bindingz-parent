import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {environment} from "../environments/environment"
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})

export class BackendService {
  private loggedIn = false;
  private token: string;

  constructor(private http: HttpClient) {}

  setLoggedIn(loggedIn: boolean, token?: string) {
    this.loggedIn = loggedIn;
    this.token = token;
  }

  get<T>(path: string, data?: any): Observable<T> {
    const header = (this.loggedIn) ? { Authorization: `Bearer ${this.token}` } : undefined;

    return <Observable<T>>this.http.request('GET', environment.backend + path, {
      body: data,
      responseType: 'json',
      observe: 'body',
      headers: header
    });
  }
}
