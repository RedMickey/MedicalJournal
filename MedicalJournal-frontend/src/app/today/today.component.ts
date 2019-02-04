import { Component, OnInit } from '@angular/core';
import { PillReminderEntry } from '../models/PillReminderEntry';

@Component({
  selector: 'app-today',
  templateUrl: './today.component.html',
  styleUrls: ['./today.component.css']
})
export class TodayComponent implements OnInit {
  private today: Date

  pillReminderEntries: PillReminderEntry[];

  constructor() {
    this.today = new Date();
   }

  ngOnInit() {
    this.pillReminderEntries = [
      {pillCount: 2, pillName: "pill1",
          pillCountType: "mg", isDone: 1, date: new Date,
          isLate: true, id: 1, havingMealsType: 1},
      {pillCount: 2, pillName: "pill2",
          pillCountType: "mg", isDone: 1, date: new Date,
          isLate: false, id: 1, havingMealsType: 3},
      {pillCount: 2, pillName: "pill3",
          pillCountType: "drop", isDone: 1, date: new Date,
          isLate: true, id: 1, havingMealsType: null}
    ];
  }

}
