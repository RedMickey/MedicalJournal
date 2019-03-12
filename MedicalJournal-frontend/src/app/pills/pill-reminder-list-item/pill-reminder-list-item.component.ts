import { Component, OnInit, Input } from '@angular/core';
import { PillReminder } from '../../models/PillReminder';
import { UtilsService } from '../../services/utils.service';

@Component({
  selector: 'app-pill-reminder-list-item',
  templateUrl: './pill-reminder-list-item.component.html',
  styleUrls: ['./pill-reminder-list-item.component.css']
})
export class PillReminderListItemComponent implements OnInit {
  @Input() pillReminderItem: PillReminder;

  // fields
  havingMealsTypePretext: string;
  timesADayStr: string;
  isActiveStr: string = "Активен";

  constructor(private utilsService: UtilsService) { }

  ngOnInit() {
    this.havingMealsTypePretext = this.utilsService.createHavingMealsTypePretext(
      this.pillReminderItem.havingMealsType
    );
    let ending = " раз в день";
    if (this.pillReminderItem.numberOfDoingAction<11||this.pillReminderItem.numberOfDoingAction>20){
      if (this.checkLastDigitOn234(this.pillReminderItem.numberOfDoingAction))
          ending=" раза в день";
    }
    this.timesADayStr = String(this.pillReminderItem.numberOfDoingAction) + 
      ending;
    if (!this.pillReminderItem.isActive)
      this.isActiveStr = "Завершён";
  }

  checkLastDigitOn234(x: number): boolean{
    let buf = x%10;
    return (buf>1&&buf<5)?true:false;
  }

}
