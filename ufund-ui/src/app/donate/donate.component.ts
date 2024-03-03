import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../user.service';
import { AppComponent } from '../app.component';

@Component({
  selector: 'app-donate',
  templateUrl: './donate.component.html',
  styleUrls: ['./donate.component.css']
})
export class DonateComponent {
  message = "";
  donated = false;

  constructor(
    private router: Router,
    private userService: UserService,
    private appComponent: AppComponent
    ) {}

  donate(number: string, money: string, name: string, date:string){
    if(number == "" || money == "" || name == "" || date == ""){
      this.message = "Please fill in all fields"
    }
    else if(number.length != 16){
      this.message = "Card number should be 16 digits";
    }
    else{
      let cardNumber = parseInt(number);
      let stringCard = (""+cardNumber).split("");
      let numberCard = stringCard.map(Number);
      if(this.checkNumber(numberCard) == true){
        this.userService.addNotification(this.appComponent.current_user + " has donated " + money + " dollars").subscribe();
        this.message = "thank you for donating! we have stolen " + money + " dollars from you!";
        this.donated = true;
      }
      else{
        this.message = "Please enter a valid credit card number";
      }
    }
  }

  checkNumber(numbers: number[]){
    let sum = 0;
    for(let i = 0; i < numbers.length; i++){
      if(i%2 == 0){
        numbers[i] = numbers[i]*2;
        if(numbers[i] >= 10){
          let tempString = (""+numbers[i]).split("");
          let tempNumber = tempString.map(Number);
          numbers[i] = tempNumber[0]+tempNumber[1];
        }
      }
      sum += numbers[i];
    }
    return(sum%10 == 0);
  }

  goBack(): void {
    this.router.navigateByUrl('/dashboard');
  }
}
