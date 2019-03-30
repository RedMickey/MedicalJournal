import { Component, ViewChild, ElementRef } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
//import { Router } from '@angular/router';

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

  constructor(private breakpointObserver: BreakpointObserver) {
    /*router.events.subscribe((val) => {
      console.log(val);
    });*/
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
