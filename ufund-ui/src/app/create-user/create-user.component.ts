import { Component} from '@angular/core';
import { Router } from '@angular/router';
import { Observable, Subject } from 'rxjs';


import { UserService } from '../user.service';
import { AppComponent } from '../app.component';
import { User } from '../user';
import { Pet } from '../pet';
@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.css']
})
export class CreateUserComponent {
  users: User[] = [];
  pets: number[] = [];
  notifs: String[] = [];
  message = "";

  constructor(
    private userService: UserService,
    private router: Router,
    private AppComponent: AppComponent) {}

  createUser(NewUsername: string, NewPassword: string, confirmPassword: string): void {
    if(NewUsername == "" || NewPassword == "" || confirmPassword == ""){
      this.message = "Required fields not filled in";
      return;
    }
    else{
      if((NewPassword != confirmPassword)){
        this.message = "Passwords don't match"
        return;
      }
      if((NewUsername == "admin")){
        this.message = "Username is reserved"
        return;
      }
      NewUsername = NewUsername.trim();
      NewPassword = NewPassword.trim();
      
      let newUser: User = {
        username: NewUsername,
        password: NewPassword,
        basket_pets: this.pets,
        adopted_pets: this.pets,
        notifs: this.notifs
      }
      this.userService.addUser(newUser)
        .subscribe(user => {
        if(user == undefined){
          this.message = "Username taken"
          return;
        }
        else{
          this.users.push(user);
          this.router.navigateByUrl('/login');
        }
      }); 
    }
  }
  goBack(): void {
    this.router.navigateByUrl('/login');
  }
}
