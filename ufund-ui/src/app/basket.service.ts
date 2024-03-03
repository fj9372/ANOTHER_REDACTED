import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import { Pet } from './pet';
import { User } from './user';


@Injectable({ providedIn: 'root' })
export class BasketService {

  private basketUrl = 'http://localhost:8080/baskets'
  
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(
    private http: HttpClient) { }

  /** GET pets from the server */
  getPetsBaskets(user: String): Observable<Pet[]> {
    const url = `${this.basketUrl}/username/${user}`;
    return this.http.get<Pet[]>(url)
      .pipe(
        tap(_ => this.log('fetched pets')),
        catchError(this.handleError<Pet[]>('getPets', []))
      );
  }

  /** GET pets from the server */
  getAdoptedBaskets(user: String): Observable<Pet[]> {
    const url = `${this.basketUrl}/adopted/${user}`;
    return this.http.get<Pet[]>(url)
      .pipe(
        tap(_ => this.log('fetched pets')),
        catchError(this.handleError<Pet[]>('getPets', []))
      );
  }

  /** GET the adopters from the server */
  getAdopterBaskets(id: number): Observable<Pet[]> {
    const url = `${this.basketUrl}/adopter/${id}`;
    return this.http.get<Pet[]>(url)
      .pipe(
        tap(_ => this.log('fetched pets')),
        catchError(this.handleError<Pet[]>('getPets', []))
      );
  }

  /** POST: add a new pet to the server */
  addPetToAdopt(id: number): Observable<Pet> {
    const url = `${this.basketUrl}/adopt`;
    return this.http.post<Pet>(url, id, this.httpOptions).pipe(
      tap((newPet: Pet) => this.log(`added pet w/ id=${newPet.id}`)),
      catchError(this.handleError<Pet>('addPet'))
    );
  }

  /** POST: add a new pet to the server */
  addPetBaskets(pet: Pet): Observable<Pet> {
    return this.http.post<Pet>(this.basketUrl, pet, this.httpOptions).pipe(
      tap((newPet: Pet) => this.log(`added pet w/ id=${newPet.id}`)),
      catchError(this.handleError<Pet>('addPet'))
    );
  }

  /** DELETE: delete the pet from the server */
  deletePetBaskets(id: number): Observable<Pet> {
    const url = `${this.basketUrl}/${id}`;

    return this.http.delete<Pet>(url, this.httpOptions).pipe(
      tap(_ => this.log(`deleted pet id=${id}`)),
      catchError(this.handleError<Pet>('deletePet'))
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

  /** Log a PetService message with the MessageService */
  private log(message: string) {
    return;
  }
}