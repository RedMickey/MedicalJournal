import { Component } from '@angular/core';
import { MatIconRegistry } from "@angular/material";
import { DomSanitizer } from "@angular/platform-browser";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  public constructor(private domSanitizer: DomSanitizer,
    public matIconRegistry: MatIconRegistry){
      this.matIconRegistry.addSvgIcon('tonometer', 
      this.domSanitizer.bypassSecurityTrustResourceUrl('../assets/additional_icons/tonometer.svg'))
      .addSvgIcon('restaurant', 
      this.domSanitizer.bypassSecurityTrustResourceUrl('../assets/additional_icons/food.svg'))
      .addSvgIcon('pill', 
      this.domSanitizer.bypassSecurityTrustResourceUrl('../assets/additional_icons/pill.svg'))
    }

  title = 'MedicalJournal-frontend';
}
