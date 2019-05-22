import { Component, OnInit, Input } from '@angular/core';
import { PillAndMeasurementReminderEntries } from '../../../models/PillAndMeasurementReminderEntries';

@Component({
  selector: 'app-day-history-item',
  templateUrl: './day-history-item.component.html',
  styleUrls: ['./day-history-item.component.css']
})
export class DayHistoryItemComponent implements OnInit {

  @Input() pillAndMeasurementReminderEntries: PillAndMeasurementReminderEntries;

  public historyDate: Date;
  isToday: boolean;

  constructor() { }

  ngOnInit() {
    let today = new Date();
    this.pillAndMeasurementReminderEntries.measurementReminderEntries.forEach(measurementReminderEntry => {
      measurementReminderEntry.date = new Date(measurementReminderEntry.date);
      if(measurementReminderEntry.isDone==0){
        measurementReminderEntry.isLate = today>measurementReminderEntry.date?true:false;
      }
    });
    this.pillAndMeasurementReminderEntries.pillReminderEntries.forEach(pillReminderEntry => {
      pillReminderEntry.date = new Date(pillReminderEntry.date);
      if(pillReminderEntry.isDone==0){
        pillReminderEntry.isLate = today>pillReminderEntry.date?true:false;
      }
    });

    this.historyDate = this.pillAndMeasurementReminderEntries.pillReminderEntries.length>0
      ?this.pillAndMeasurementReminderEntries.pillReminderEntries[0].date
      :this.pillAndMeasurementReminderEntries.measurementReminderEntries[0].date;
    this.isToday = this.compareDateWithoutTime(today, this.historyDate);
  }

  compareDateWithoutTime(date1: Date, date2: Date): boolean{
    let d1 = new Date(date1);
    let d2 = new Date(date2);
    d1.setHours(0, 0, 0, 0);
    d2.setHours(0, 0, 0, 0);
    return d1.getTime()==d2.getTime();
  }

}
