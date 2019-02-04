import { Component, OnInit, Input } from '@angular/core';
import { PillReminderEntry } from '../models/PillReminderEntry';

@Component({
  selector: 'app-pill-reminder-entry',
  templateUrl: './pill-reminder-entry.component.html',
  styleUrls: ['./pill-reminder-entry.component.css']
})
export class PillReminderEntryComponent implements OnInit {
  @Input() pillReminderEntry: PillReminderEntry;

  havingMealsTypePretext: string;

  constructor() { }

  ngOnInit() {
    switch(this.pillReminderEntry.havingMealsType){
      case 1:
        this.havingMealsTypePretext = "до"
        break;
      case 2:
        this.havingMealsTypePretext = "с"
        break;
      case 3:
        this.havingMealsTypePretext = "после"
        break;
    }
  }

}
