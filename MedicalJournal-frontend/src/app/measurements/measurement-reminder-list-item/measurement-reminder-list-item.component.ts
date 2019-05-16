import { Component, OnInit, Input } from '@angular/core';
import { MeasurementReminder } from '../../models/MeasurementReminder';
import { UtilsService } from '../../services/utils.service';

@Component({
  selector: 'app-measurement-reminder-list-item',
  templateUrl: './measurement-reminder-list-item.component.html',
  styleUrls: ['./measurement-reminder-list-item.component.css']
})
export class MeasurementReminderListItemComponent implements OnInit {
  @Input() measurementReminderItem: MeasurementReminder;

  // fields
  havingMealsTypePretext: string;
  timesADayStr: string;
  iconName: string;
  isActiveStr: string = "Активен";

  constructor(private utilsService: UtilsService) { }

  ngOnInit() {
    this.havingMealsTypePretext = this.utilsService.createHavingMealsTypePretext(
      this.measurementReminderItem.havingMealsType
    );
    let ending = " раз в день";
    if (this.measurementReminderItem.numberOfDoingAction<11||this.measurementReminderItem.numberOfDoingAction>20){
      if (this.checkLastDigitOn234(this.measurementReminderItem.numberOfDoingAction))
          ending=" раза в день";
    }
    this.timesADayStr = String(this.measurementReminderItem.numberOfDoingAction) + 
      ending;
    console.log(this.measurementReminderItem);
    if (!this.measurementReminderItem.isActive)
      this.isActiveStr = "Завершён";
    switch(this.measurementReminderItem.idMeasurementType){
      case 1:
      this.iconName = "thermometer";
        break;
      case 2:
      this.iconName = "tonometer";
        break;
      case 3:
        this.iconName = "pulse";
        break;
      case 4:
        this.iconName = "glucosemeter";
        break;
      case 5:
        this.iconName = "weight";
        break;
      case 6:
        this.iconName = "burning";
        break;
      case 7:
        this.iconName = "diet";
        break;
      case 8:
        this.iconName = "steps";
        break;
    }
  }

  checkLastDigitOn234(x: number): boolean{
    let buf = x%10;
    return (buf>1&&buf<5)?true:false;
  }
}
