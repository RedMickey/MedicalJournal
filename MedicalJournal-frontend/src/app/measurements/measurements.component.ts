import { Component, OnInit } from '@angular/core';
import { ReminderItemsService } from '../services/reminder-items.service';
import { MeasurementReminder } from '../models/MeasurementReminder';
import { MeasurementType } from '../models/MeasurementType';
import { ChoosingMeasurementTypeDialogComponent } from './choosing-measurement-type-dialog/choosing-measurement-type-dialog.component';
import {MatBottomSheet, MatBottomSheetRef} from '@angular/material';

@Component({
  selector: 'app-measurements',
  templateUrl: './measurements.component.html',
  styleUrls: ['./measurements.component.css']
})
export class MeasurementsComponent implements OnInit {

  measurementTypes: MeasurementType[];
  measurementReminderItems: MeasurementReminder[];

  constructor(private reminderItemsService: ReminderItemsService,
    public dialog: MatBottomSheet) { }

  ngOnInit() {
    this.reminderItemsService.getAllMeasurementReminders()
    .subscribe(measurementReminderItems => {this.measurementReminderItems = measurementReminderItems;
    //console.log(measurementReminderItems);
    });
    /*this.measurementTypes = [
      {idMeasurementType: 1, typeName: "dfgdft1", iconName: "thermometer"},
      {idMeasurementType: 2, typeName: "dfgdft2", iconName: "tonometer"}
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
        }
      });
    });
  }

  ngAfterViewInit() {
    let numberSpan = document.querySelector('.bar-title');
      numberSpan.textContent = "Измерения";
  }

  public addMeasurementReminder(event: any) {
    const bottomSheetRef = this.dialog.open(ChoosingMeasurementTypeDialogComponent, {
      data: { measurementTypes: this.measurementTypes,
              isOneTime: false },
    });
  }
}
