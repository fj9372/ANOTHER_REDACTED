import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Adoption Center';
  showNav:boolean = false;
  admin:boolean = false;
  current_user = "";

  ShowNavTrue(): void{
    this.showNav = true;
  }

  ShowNavFalse(): void{
    this.showNav = false;
  }

  adminTrue(): void{
    this.admin = true;
  }

  adminFalse(): void{
    this.admin = false;
  }


}