import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { HttpClientInMemoryWebApiModule } from 'angular-in-memory-web-api';

import { AppRoutingModule } from './app-routing.module';

import { AppComponent } from './app.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { PetDetailComponent } from './pet-detail/pet-detail.component';
import { PetsComponent } from './pets/pets.component';
import { PetSearchComponent } from './pet-search/pet-search.component';
import { LoginComponent } from './login/login.component';

import { AdoptionBasketComponent } from './adoption-basket/adoption-basket.component';
import { CreateUserComponent } from './create-user/create-user.component';
import { AccountComponent } from './account/account.component';
import { HistoryComponent } from './history/history.component';
import { DonateComponent } from './donate/donate.component';
import { NotificationsComponent } from './notifications/notifications.component';
@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule,
  ],
  declarations: [
    AppComponent,
    DashboardComponent,
    PetsComponent,
    PetDetailComponent,
    PetSearchComponent,
    AdoptionBasketComponent,
    LoginComponent,
    CreateUserComponent,
    AccountComponent,
    HistoryComponent,
    DonateComponent,
    NotificationsComponent
  ],
  bootstrap: [ AppComponent ]
})
export class AppModule { }