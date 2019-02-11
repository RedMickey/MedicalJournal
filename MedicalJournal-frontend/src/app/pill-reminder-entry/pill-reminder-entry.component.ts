import { Component, OnInit, Input } from '@angular/core';
import { PillReminderEntry } from '../models/PillReminderEntry';
import { PillReminderEntryService } from '../services/pill-reminder-entry.service';

@Component({
  selector: 'app-pill-reminder-entry',
  templateUrl: './pill-reminder-entry.component.html',
  styleUrls: ['./pill-reminder-entry.component.css']
})
export class PillReminderEntryComponent implements OnInit {
  @Input() pillReminderEntry: PillReminderEntry;

  havingMealsTypePretext: string;
  checked: boolean = false;

  constructor(private pillReminderEntryService: PillReminderEntryService) { }

  onIsDoneChange(event: any){
    if (event.checked){
      console.log(event.checked);
      this.pillReminderEntry.isDone = 1;
      this.pillReminderEntry.date = new Date();
      if (this.pillReminderEntry.isLate){
        this.pillReminderEntry.isLate = false;
      }
    }
    else{
      this.pillReminderEntry.isDone = 0;
      this.pillReminderEntry.isLate = new Date()>this.pillReminderEntry.date?true:false;
    } 
    this.pillReminderEntryService.updatePillReminderEntry({
      isDone: this.pillReminderEntry.isDone,
      id: this.pillReminderEntry.id,
      date: this.pillReminderEntry.date
    }).subscribe(() => console.log("done"));
  }

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
    this.checked = this.pillReminderEntry.isDone==1?true:false;

    console.log(this.pillReminderEntry);
  }

}
