import { Component, OnInit, Input } from '@angular/core';
import { PillReminderEntry } from '../models/PillReminderEntry';
import { MeasurementReminderEntry } from '../models/MeasurementReminderEntry';
import { ReminderEntriesService } from '../services/reminder-entries.service';
import { UtilsService } from '../services/utils.service';
import { MatDialog, MatDialogRef } from '@angular/material';
import { MeasurementDialogComponent } from './measurement-dialog/measurement-dialog.component';

@Component({
  selector: 'app-reminder-entry',
  templateUrl: './reminder-entry.component.html',
  styleUrls: ['./reminder-entry.component.css']
})
export class ReminderEntryComponent implements OnInit {
  @Input() reminderEntry: any;
  @Input() type: number;

  // fields
  havingMealsTypePretext: string;
  iconName: string;
  //reminderDate: Date;
  reminderName: string;
  reminderPropertyStr: string;
  checked: boolean = false;

  constructor(private reminderEntryService: ReminderEntriesService,
    private utilsService: UtilsService,
    public dialog: MatDialog) { }

  onIsDoneChange(event: any){
    if (event.checked){
      console.log(event.checked);
      if (this.type == 0){
        this.reminderEntry.isDone = 1;
        this.reminderEntry.date = new Date();
        if (this.reminderEntry.isLate){
          this.reminderEntry.isLate = false;
        }
        this.reminderEntryService.updatePillReminderEntry({
          isDone: this.reminderEntry.isDone,
          id: this.reminderEntry.id,
          date: this.reminderEntry.date
        }).subscribe(() => console.log("done"));
      }
      else{
        const dialogRef = this.dialog.open(MeasurementDialogComponent, {
          data: {
            idMeasurementType: this.reminderEntry.idMeasurementType,
            value1: this.reminderEntry.value1,
            value2: this.reminderEntry.value2,
            iconName: this.iconName,
            measurementValueTypeName: this.reminderEntry.measurementValueTypeName,
            measurementTypeName: this.reminderEntry.measurementTypeName
            }
        });
    
        dialogRef.afterClosed().subscribe(result => {
          console.log(result);
          if (result.isAdd){
            this.reminderEntry.isLate = false;
            this.reminderEntry.value1 = result.value1;
            this.reminderEntry.value2 = result.value2;
            this.reminderPropertyStr = this.setUpReminderCountType();
            this.reminderEntry.isDone = 1;
            this.reminderEntry.date = new Date();

            this.reminderEntryService.updateMeasurementReminderEntry({
              isDone: this.reminderEntry.isDone,
              id: this.reminderEntry.id,
              date: this.reminderEntry.date,
              value1: this.reminderEntry.value1,
              value2: this.reminderEntry.value2
            }).subscribe(() => console.log("done"));
          }
          else{
            this.checked = false;
          }  
        });
      }
    }
    else{
      this.reminderEntry.isDone = 0;
      this.reminderEntry.isLate = new Date()>this.reminderEntry.date?true:false;
      if (this.type == 0){
        this.reminderEntryService.updatePillReminderEntry({
          isDone: this.reminderEntry.isDone,
          id: this.reminderEntry.id,
          date: this.reminderEntry.date
        }).subscribe(() => console.log("done"));
      }
      else{
        this.reminderEntryService.updateMeasurementReminderEntry({
          isDone: this.reminderEntry.isDone,
          id: this.reminderEntry.id,
          date: this.reminderEntry.date,
          value1: this.reminderEntry.value1,
          value2: this.reminderEntry.value2
        }).subscribe(() => console.log("done"));
      }
    }
  }

  ngOnInit() {
    this.havingMealsTypePretext = this.utilsService.createHavingMealsTypePretext(
      this.reminderEntry.havingMealsType
    );
    
    //this.reminderDate = this.reminderEntry.date;
    if (this.type == 0){
      this.iconName = "pill";
      this.reminderName = this.reminderEntry.pillName;
      this.reminderPropertyStr = this.reminderEntry.pillCount + " " + this.reminderEntry.pillCountType;
    }
    else{
      switch(this.reminderEntry.idMeasurementType){
        case 1:
        this.iconName = "thermometer";
          break;
        case 2:
        this.iconName = "tonometer";
          break;
      }
      this.reminderName = this.reminderEntry.measurementTypeName;
      if (this.reminderEntry.isDone == 1)
        this.reminderPropertyStr = this.setUpReminderCountType();
    }

    this.checked = this.reminderEntry.isDone==1?true:false;

    //console.log(this.reminderEntry);
    //console.log(this.type);
  }

  setUpReminderCountType(): string{
    let bufReminderPropertyStr = "";
    if (this.reminderEntry.value1!=-10000){
      bufReminderPropertyStr = String(this.reminderEntry.value1);
      if (this.reminderEntry.value2!=-10000){
        bufReminderPropertyStr = bufReminderPropertyStr + " - " + String(this.reminderEntry.value2) +
        " " + this.reminderEntry.measurementValueTypeName;
      }
      else{
        bufReminderPropertyStr += " " + this.reminderEntry.measurementValueTypeName;
      }
    }
    return bufReminderPropertyStr;
  }

}
