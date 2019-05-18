import { Component, OnInit } from '@angular/core';
import { PillReminderEntry } from '../models/PillReminderEntry';
import { MeasurementReminderEntry } from '../models/MeasurementReminderEntry';
import { ReminderEntriesService } from '../services/reminder-entries.service';
import { Router } from '@angular/router';
import { MatBottomSheet, MatBottomSheetRef } from '@angular/material';
import { ChoosingMeasurementTypeDialogComponent } from '../measurements/choosing-measurement-type-dialog/choosing-measurement-type-dialog.component';
import { MeasurementType } from '../models/MeasurementType';
import { ReminderItemsService } from '../services/reminder-items.service';

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
  measurementTypes: MeasurementType[];

  constructor(private reminderEntryService: ReminderEntriesService,
      private router: Router,
      private reminderItemsService: ReminderItemsService,
      public dialog: MatBottomSheet) {
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
    this.reminderItemsService.getMeasurementTypes()
      .subscribe(measurementTypes => {this.measurementTypes = measurementTypes;
        this.measurementTypes.forEach(measurementType => {
          switch(measurementType.idMeasurementType){
            case 1:
            measurementType.iconName = "thermometer";
              break;
            case 2:
            measurementType.iconName = "tonometer";
              break;
            case 3:
              measurementType.iconName = "pulse";
              break;
            case 4:
              measurementType.iconName = "glucosemeter";
              break;
            case 5:
              measurementType.iconName = "weight";
              break;
            case 6:
              measurementType.iconName = "burning";
              break;
            case 7:
              measurementType.iconName = "diet";
              break;
            case 8:
              measurementType.iconName = "steps";
              break;
          }
        });
      });
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
        this.router.navigate(['/pills/addOneTime']);
        break;
      case "measCrs":
        const bottomSheetRef = this.dialog.open(ChoosingMeasurementTypeDialogComponent, {
          data: { measurementTypes: this.measurementTypes,
                  isOneTime: false },
        });
        break;
      case "measOnce":
        const bottomSheetRef2 = this.dialog.open(ChoosingMeasurementTypeDialogComponent, {
          data: { measurementTypes: this.measurementTypes,
                  isOneTime: true },
        });
        break;
      default:
        console.log(event);
        break;
    }

    
  }

}
