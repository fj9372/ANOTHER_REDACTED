import { Component, OnInit } from '@angular/core';

import { Pet } from '../pet';
import { BasketService } from '../basket.service';
import { PetService } from '../pet.service';
import { AppComponent } from '../app.component';
import { concatMap, switchMap } from 'rxjs';
import { UserService } from '../user.service';

@Component({
  selector: 'app-adoption-basket',
  templateUrl: './adoption-basket.component.html',
  styleUrls: ['./adoption-basket.component.css']
})
export class AdoptionBasketComponent {

  pets: Pet[] = [];
  message = "";

  constructor(private basketService: BasketService,
    private petService: PetService,
    private userService: UserService,
    private appComponent: AppComponent) { }

  ngOnInit(): void {
    this.getPets();
  }

  getPets(): void {
    this.basketService.getPetsBaskets(this.appComponent.current_user)
    .subscribe(pets => this.pets = pets);
  }

  add(name: string): void {
    name = name.trim();
    if (!name) { return; }
    this.basketService.addPetBaskets({ name } as Pet)
      .subscribe(pet => {
        this.pets.push(pet);
      });
  }

  delete(pet: Pet): void {
    this.pets = this.pets.filter(h => h !== pet);
    this.basketService.deletePetBaskets(pet.id).subscribe();
    this.message = pet.name + " has been removed from your basket";
  }

  adopt(): void {
    for(let i = 0; i < this.pets.length; i++){
      this.basketService.addPetToAdopt(this.pets[i].id).subscribe();
    }
    for(let i = 0; i < this.pets.length; i++){
      this.petService.deletePet(this.pets[i].id).subscribe();
    }
    this.pets = [];
  }

  hasPets(): boolean{
    return(!(this.pets.length == 0));
  }

}
