import { Component, OnInit } from '@angular/core';
import { PillReminderEntry } from '../models/PillReminderEntry';
import { PillReminderEntryService } from '../services/pill-reminder-entry.service';

@Component({
  selector: 'app-today',
  templateUrl: './today.component.html',
  styleUrls: ['./today.component.css']
})
export class TodayComponent implements OnInit {
  private today: Date

  pillReminderEntries: PillReminderEntry[];

  constructor(private pillReminderEntryService: PillReminderEntryService) {
    //this.today = new Date();
   }

  ngOnInit() {
    //this.today = new Date(2019, 1, 4, 14, 30, 0);
    this.today = new Date();
    this.getPillReminderEntriesByDate(this.today);
    /*this.pillReminderEntries = [
      {pillCount: 2, pillName: "pill1",
          pillCountType: "mg", isDone: 1, date: new Date,
          isLate: true, id: 1, havingMealsType: 1},
      {pillCount: 2, pillName: "pill2",
          pillCountType: "mg", isDone: 1, date: new Date,
          isLate: false, id: 1, havingMealsType: 3},
      {pillCount: 2, pillName: "pill3",
          pillCountType: "drop", isDone: 1, date: new Date,
          isLate: true, id: 1, havingMealsType: null}
    ];*/
  }

  getPillReminderEntriesByDate (date: Date): void{
    this.pillReminderEntryService.getPillReminderEntriesByDate(date)
      .subscribe(pillReminderEntries => {this.pillReminderEntries = pillReminderEntries; 
        this.pillReminderEntries.forEach(pillReminderEntry =>{
          pillReminderEntry.date = new Date(pillReminderEntry.date);
          if(pillReminderEntry.isDone==0){
            pillReminderEntry.isLate = this.today>pillReminderEntry.date?true:false;
            //console.log(pillReminderEntry.date.constructor.name);
          }
        });
        //console.log(this.pillReminderEntries);
        //console.log(this.today);
      });
  }

}
