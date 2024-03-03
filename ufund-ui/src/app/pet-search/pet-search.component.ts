import { Component, OnInit } from '@angular/core';

import { Observable, Subject } from 'rxjs';

import {
   debounceTime, distinctUntilChanged, switchMap
 } from 'rxjs/operators';

import { Pet } from '../pet';
import { PetService } from '../pet.service';

@Component({
  selector: 'app-pet-search',
  templateUrl: './pet-search.component.html',
  styleUrls: [ './pet-search.component.css' ]
})
export class PetSearchComponent implements OnInit {
  pets$!: Observable<Pet[]>;
  private searchTerms = new Subject<string>();
  nameSearch:boolean = true;

  constructor(private petService: PetService) {}

  // Push a search term into the observable stream.
  search(term: string): void {
    this.searchTerms.next(term);
  }

  changeSearch():void{
    this.nameSearch = !this.nameSearch;
  }

  ngOnInit(): void {
      this.pets$ = this.searchTerms.pipe(
      // wait 300ms after each keystroke before considering the term
      debounceTime(300),

      // ignore new term if same as previous term
      distinctUntilChanged(),

      switchMap((term: string) => {
        if(this.nameSearch == true){
          return this.petService.searchPets(term);
        } 
        else{
          return this.petService.searchPetsByType(term);
        }
      })
    );
    
  }
}