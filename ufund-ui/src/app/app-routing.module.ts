import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { DashboardComponent } from './dashboard/dashboard.component';
import { PetsComponent } from './pets/pets.component';
import { PetDetailComponent } from './pet-detail/pet-detail.component';
import { LoginComponent } from './login/login.component';

import { AdoptionBasketComponent } from './adoption-basket/adoption-basket.component';
import { CreateUserComponent } from './create-user/create-user.component';
import { AccountComponent } from './account/account.component';
import { HistoryComponent } from './history/history.component';
import { DonateComponent } from './donate/donate.component';
import { NotificationsComponent } from './notifications/notifications.component';

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent},
  { path: 'dashboard', component: DashboardComponent },
  { path: 'detail/:id', component: PetDetailComponent },
  { path: 'pets', component: PetsComponent },
  { path: 'baskets', component: AdoptionBasketComponent},
  { path: 'create', component: CreateUserComponent},
  { path: 'account', component: AccountComponent},
  { path: 'history', component: HistoryComponent},
  { path: 'donate', component: DonateComponent},
  { path: 'notifications', component: NotificationsComponent}
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}