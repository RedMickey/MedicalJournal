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
      this.domSanitizer.bypassSecurityTrustResourceUrl('../assets/additional_icons/tonometer2.svg'))
      .addSvgIcon('restaurant', 
      this.domSanitizer.bypassSecurityTrustResourceUrl('../assets/additional_icons/food.svg'))
      .addSvgIcon('pill', 
      this.domSanitizer.bypassSecurityTrustResourceUrl('../assets/additional_icons/pill.svg'))
      .addSvgIcon('ruler', 
      this.domSanitizer.bypassSecurityTrustResourceUrl('../assets/additional_icons/ruler.svg'))
      .addSvgIcon('thermometer', 
      this.domSanitizer.bypassSecurityTrustResourceUrl('../assets/additional_icons/thermometer1.svg'))
      .addSvgIcon('porridge', 
      this.domSanitizer.bypassSecurityTrustResourceUrl('../assets/additional_icons/porridge.svg'))
      .addSvgIcon('pulse', 
      this.domSanitizer.bypassSecurityTrustResourceUrl('../assets/additional_icons/pulse.svg'))
      .addSvgIcon('glucosemeter', 
      this.domSanitizer.bypassSecurityTrustResourceUrl('../assets/additional_icons/glucosemeter.svg'))
      .addSvgIcon('weight', 
      this.domSanitizer.bypassSecurityTrustResourceUrl('../assets/additional_icons/weight.svg'))
      .addSvgIcon('burning', 
      this.domSanitizer.bypassSecurityTrustResourceUrl('../assets/additional_icons/burning.svg'))
      .addSvgIcon('diet', 
      this.domSanitizer.bypassSecurityTrustResourceUrl('../assets/additional_icons/diet.svg'))
      .addSvgIcon('steps', 
      this.domSanitizer.bypassSecurityTrustResourceUrl('../assets/additional_icons/steps.svg'))
    }

  title = 'MedicalJournal-frontend';
}
