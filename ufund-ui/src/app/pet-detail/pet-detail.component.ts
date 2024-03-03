import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

import { Pet } from '../pet';
import { PetService } from '../pet.service';
import { AppComponent } from '../app.component';

@Component({
  selector: 'app-pet-detail',
  templateUrl: './pet-detail.component.html',
  styleUrls: [ './pet-detail.component.css' ]
})
export class PetDetailComponent implements OnInit {
  pet: Pet | undefined;

  constructor(
    private route: ActivatedRoute,
    private petService: PetService,
    private location: Location,
    private appComponent: AppComponent
  ) {}

  ngOnInit(): void {
    this.getPet();
  }

  getPet(): void {
    const id = parseInt(this.route.snapshot.paramMap.get('id')!, 10);
    this.petService.getPet(id)
      .subscribe(pet => this.pet = pet);
  }

  goBack(): void {
    this.location.back();
  }
  isAdmin(): boolean {
    return this.appComponent.admin;
  }
  save(): void {
    if (this.pet) {
      this.petService.updatePet(this.pet)
        .subscribe(() => this.goBack());
    }
  }
}