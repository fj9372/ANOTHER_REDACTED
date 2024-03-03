import { Component, OnInit } from '@angular/core';

import { Pet } from '../pet';
import { PetService } from '../pet.service';
import { AppComponent } from '../app.component';

@Component({
  selector: 'app-pets',
  templateUrl: './pets.component.html',
  styleUrls: ['./pets.component.css']
})
export class PetsComponent implements OnInit {
  pets: Pet[] = [];
  message = "";

  constructor(private petService: PetService,
    private appComponent: AppComponent) { }

  ngOnInit(): void {
    this.getPets();
  }

  getPets(): void {
    this.petService.getPets()
    .subscribe(pets => this.pets = pets);
  }

  add(PetName: string, PetType: string): void {
    if(PetName == "" || PetType == ""){
      return;
    }
    PetName = PetName.trim();
    PetType = PetType.trim();
    let newPet: Pet = {
      id: 1,
      animaltype: PetType,
      name: PetName
    }
    this.petService.addPet(newPet)
      .subscribe(pet => {
        this.pets.push(pet);
      });
  }

  addToBasket(pet: Pet): void {;
    this.petService.addPetToBasket(pet).
    subscribe(pets => {
      if (pets == null){
        this.message = pet.name + " already exists in your basket!";
      }
      else {
        this.message = pets.name + " has been added to your basket!";
      }
    });
  }

  delete(pet: Pet): void {
    this.pets = this.pets.filter(h => h !== pet);
    this.petService.deletePet(pet.id).subscribe();
  }

  isAdmin(): boolean {
    return this.appComponent.admin;
  }

}