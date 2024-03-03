import { Component } from '@angular/core';
import { AppComponent } from '../app.component';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.css']
})
export class AccountComponent {
  constructor(private appComponent: AppComponent) { }

  Logout():void{
    this.appComponent.ShowNavFalse();
    this.appComponent.adminFalse();
  }
}
