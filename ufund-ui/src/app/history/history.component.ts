import { Component, OnInit } from '@angular/core';

import { Pet } from '../pet';
import { BasketService } from '../basket.service';
import { PetService } from '../pet.service';
import { AppComponent } from '../app.component';
import { User } from '../user';

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.css']
})
export class HistoryComponent {
  pets: Pet[] = [];
  adopters: User[] = [];
  message = "";

  constructor(private basketService: BasketService,
    private petService: PetService,
    private appComponent: AppComponent) { }

  ngOnInit(): void {
    this.getPets();
  }

  getPets(): void {
    this.basketService.getAdoptedBaskets(this.appComponent.current_user)
    .subscribe(pets => this.pets = pets);
  }

  hasPets(): boolean{
    return(!(this.pets.length == 0));
  }
}
