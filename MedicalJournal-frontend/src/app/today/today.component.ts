import { Component, OnInit } from '@angular/core';
import { PillReminderEntry } from '../models/PillReminderEntry';
import { MeasurementReminderEntry } from '../models/MeasurementReminderEntry';
import { ReminderEntriesService } from '../services/reminder-entries.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-today',
  templateUrl: './today.component.html',
  styleUrls: ['./today.component.css']
})
export class TodayComponent implements OnInit {
  private today: Date
  public animationMode = 'fling';

  pillReminderEntries: PillReminderEntry[];
  measurementReminderEntries: MeasurementReminderEntry[];

  constructor(private reminderEntryService: ReminderEntriesService,
      private router: Router) {
    //this.today = new Date();
   }

  ngOnInit() {
    //this.today = new Date(2019, 1, 19, 23, 30, 0);
    this.today = new Date();
    this.getPillReminderEntriesByDate(this.today);
    this.getMeasurementReminderEntriesByDate(this.today);
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

  ngAfterViewInit() {
    let numberSpan = document.querySelector('.bar-title');
      numberSpan.textContent = "Сегодня";
  }

  getPillReminderEntriesByDate (date: Date): void{
    this.reminderEntryService.getPillReminderEntriesByDate(date)
      .subscribe(pillReminderEntries => {this.pillReminderEntries = pillReminderEntries; 
        console.log(this.pillReminderEntries);
        this.pillReminderEntries.forEach(pillReminderEntry => {
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

  getMeasurementReminderEntriesByDate (date: Date): void{
    //let bufDate = new Date(2019, 1, 13, 16, 0, 0);
    this.reminderEntryService.getMeasurementReminderEntriesByDate(date)
      .subscribe(measurementReminderEntries => {this.measurementReminderEntries = measurementReminderEntries; 
        console.log(this.measurementReminderEntries);
        this.measurementReminderEntries.forEach(measurementReminderEntry => {
          measurementReminderEntry.date = new Date(measurementReminderEntry.date);
          if(measurementReminderEntry.isDone==0){
            measurementReminderEntry.isLate = date>measurementReminderEntry.date?true:false;
            //console.log(pillReminderEntry.date.constructor.name);
          }
        });
        //console.log(this.pillReminderEntries);
        //console.log(this.today);
      });
  }

  public doAction(event: any) {
    switch(event){
      case "pillCrs":
        this.router.navigate(['/pills/add']);
        break;
      case "pillOnce":

        break;
      case "measCrs":

        break;
      case "measOnce":

        break;
      default:
        console.log(event);
        break;
    }

    
  }

}
