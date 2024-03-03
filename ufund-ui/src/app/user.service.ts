import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import { User } from './user';


@Injectable({ providedIn: 'root' })
export class UserService {

  private usersUrl = 'http://localhost:8080/users'

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(
    private http: HttpClient) { }

  /** GET users from the server */
  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.usersUrl)
      .pipe(
        tap(_ => this.log('fetched users')),
        catchError(this.handleError<User[]>('getUsers', []))
      );
  }


  /** GET user by id. Will 404 if id not found */
  getUser(username: String): Observable<User> {
    const url = `${this.usersUrl}/${username}`;
    return this.http.get<User>(url).pipe(
      tap(_ => this.log(`fetched user id=${username}`)),
      catchError(this.handleError<User>(`getUser id=${username}`))
    );
  }

  /** GET user by id. Will 404 if id not found */
  getUserNotifs(username: String): Observable<String[]> {
    const url = `${this.usersUrl}/notifications/${username}`;
    return this.http.get<String[]>(url).pipe(
      tap(_ => this.log(`fetched user id=${username}`)),
      catchError(this.handleError<String[]>(`getUser id=${username}`))
    );
  }

  checkUser(username: String, password: String): Observable<User> {
    const url = `${this.usersUrl}/${username}/${password}`;
    return this.http.get<User>(url).pipe(
      tap(_ => this.log(`fetched user id=${username}`)),
      catchError(this.handleError<User>(`getUser id=${username}`))
    );
  }

  //////// Save methods //////////

  /** POST: add a new user to the server */
  addUser(user: User): Observable<User> {
    return this.http.post<User>(this.usersUrl, user, this.httpOptions).pipe(
      tap((newUser: User) => this.log(`added user w/ username=${newUser.username}`)),
      catchError(this.handleError<User>('addUser'))
    );
  }

  /** POST: add a new notification to admin */
  addNotification(message: String): Observable<String> {
    const url = `${this.usersUrl}/notifications`;
    return this.http.post<String>(url, message, this.httpOptions).pipe(
      tap(_ => this.log(`fetched user id=${message}`)),
      catchError(this.handleError<String>(`getUser id=${message}`))
    );
  }

  /** DELETE: remove a notification from admin */
  deleteNotif(notif: String): Observable<String>{
    const url = `${this.usersUrl}/notifications/${notif}`;
    return this.http.delete<String>(url, this.httpOptions).pipe(
      tap(_ => this.log(`deleted pet id=${notif}`)),
      catchError(this.handleError<String>('deletePet'))
    );
  }
  /**
   * Handle Http operation that failed.
   * Let the app continue.
   *
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      this.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  /** Log a UserService message with the MessageService */
  private log(message: string) {
    return;
  }
}