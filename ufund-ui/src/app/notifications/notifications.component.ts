import { Component } from '@angular/core';
import { UserService } from '../user.service';
import { AppComponent } from '../app.component';

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.css']
})
export class NotificationsComponent {
  notifications: String[] = [];
  message = "";

  constructor(
    private userService: UserService,
    private appComponent: AppComponent) { }

  ngOnInit(): void {
    this.getNotifs();
  }

  getNotifs(): void {
    this.userService.getUserNotifs(this.appComponent.current_user)
    .subscribe(notifs => this.notifications = notifs);
  }

  deleteNotif(notif: String): void{
    this.notifications = this.notifications.filter((i => v => v !== notif || --i)(1));
    this.userService.deleteNotif(notif).subscribe();
  }

  isAdmin(): boolean{
    return this.appComponent.admin;
  }
}
