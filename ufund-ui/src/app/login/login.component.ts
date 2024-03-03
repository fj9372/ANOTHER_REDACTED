import { Component} from '@angular/core';
import { Router } from '@angular/router';
import { Observable, Subject } from 'rxjs';

import { UserService } from '../user.service';
import { BasketService } from '../basket.service';
import { AppComponent } from '../app.component';
import { User } from '../user';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  private Terms = new Subject<string>();
  user: User | undefined;
  message = "";

  constructor(
    private userService: UserService,
    private router: Router,
    private AppComponent: AppComponent,
    private basketService: BasketService) {}

  login(username: string, password: string): void {
    if(username == "" || password == ""){
      this.message = "Required fields not filled in"
      return;
    }
    else{
      if(username == "admin"){
        this.AppComponent.adminTrue();
      }
      this.userService.checkUser(username, password)
      .subscribe(user => {
        this.user = user
        if(this.user != undefined){
          this.AppComponent.current_user = this.user.username;
          this.router.navigateByUrl('/dashboard');
          this.AppComponent.ShowNavTrue();
          this.basketService.getPetsBaskets(this.AppComponent.current_user).subscribe();
        }
        else{
          this.message = "Username or password is incorrect"
        }
      });
    }
  }
}
