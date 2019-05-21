import { Component, ViewChild, ElementRef } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { useAnimation } from '@angular/animations';

@Component({
  selector: 'app-main-nav',
  templateUrl: './main-nav.component.html',
  styleUrls: ['./main-nav.component.css']
})
export class MainNavComponent {

  @ViewChild("barTitle")
  barTitle: ElementRef;

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches)
    );

  avatar_urn = "avatar.png"; 
  user: any;

  constructor(private breakpointObserver: BreakpointObserver,
    private authService: AuthService,
    private router: Router) {
      //this.user = this.authService.currentUserValue;
      this.authService.currentUser.subscribe(x => {
        this.user = x
        if (this.user){
          switch(this.user.genderId) {
            case 1:
              this.avatar_urn = "avatar.png";
              break;
            case 2:
              this.avatar_urn = "boy.png";
              break;
            case 3:
              this.avatar_urn = "girl.png";
              break;
          }
        }
        else
          this.avatar_urn = "avatar.png";
      });
    /*router.events.subscribe((val) => {
      console.log(val);
    });*/
  }

  logout(){
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  public isLoggedIn(): boolean{
    return this.authService.loggedIn();
  }

  onNavLinkClicked(directionType){
    /*switch(directionType){
      case 1:
        this.barTitle.nativeElement.textContent = "Сегодня";
        break;
      case 2:
        this.barTitle.nativeElement.textContent = "Медикаменты";
        break;
      case 3:
        this.barTitle.nativeElement.textContent = "Измерения";
        break;
    }*/
  }

}
