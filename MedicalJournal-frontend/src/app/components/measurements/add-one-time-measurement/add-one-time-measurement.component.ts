import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroupDirective, NgForm, Validators} from '@angular/forms';
import {MatDatepickerInputEvent} from '@angular/material/datepicker';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import {MeasurementReminderCourse} from '../../../models/MeasurementReminderCourse';
import { ReminderItemsService } from '../../../services/reminder-items.service';

@Component({
  selector: 'app-add-one-time-measurement',
  templateUrl: './add-one-time-measurement.component.html',
  styleUrls: ['./add-one-time-measurement.component.css']
})
export class AddOneTimeMeasurementComponent implements OnInit {

  mealTypePrefix = "до";
  toggleGroupSelectedMeal = "1";
  startDate: Date;
  reminderTime: string;
  measurementType: any;
  measurementValueName: string;

  formControls = {
    "measValue1FormControl": new FormControl('', [
      Validators.required,
      Validators.pattern('^[0-9]+[.]?[0-9]*$'),
    ]),
    "measValue2FormControl": new FormControl('', [
      Validators.required,
      Validators.pattern('^[0-9]+[.]?[0-9]*$'),
    ]),  
    "mealTimeFormControl": new FormControl(10, [
      Validators.required,
      Validators.pattern('^[1-9]{1}[0-9]*$'),
    ]) 
  };

  constructor(private route: ActivatedRoute,
    private router: Router,
    private reminderItemsService: ReminderItemsService) { 
    this.startDate = new Date();
    this.reminderTime = this.startDate.toTimeString().substring(0, 5);
    route.queryParams.subscribe(
      (queryParam: any) => {
          this.measurementType = {
            idMeasurementType: queryParam['idMT'],
            typeName: queryParam['typeName'],
            iconName: queryParam['iconName']
          };
          let numberSpan = document.querySelector('.bar-title');
          numberSpan.textContent = queryParam['typeName'];
          switch(this.measurementType.idMeasurementType){
            case "1":
              this.measurementValueName = "°C";
              break;
            case "2":
              this.measurementValueName = "мм рт. ст.";
              break;
          }
      }
    );
  }

  ngOnInit() {
  }

  havingMealTypeChanged(event): void{
    if (event.value === "2")
      this.mealTypePrefix = "до";
    if (event.value === "4")
      this.mealTypePrefix = "после";
  }

  trackByIndex(index, item): number {
    return index;
  }

  startDateChanged(type: string, event: MatDatepickerInputEvent<Date>){
    this.startDate = new Date(event.value);
  }

  submit(): void{
    let mrc: MeasurementReminderCourse = new MeasurementReminderCourse();
    let value1: number;
    let value2: number;
    if (this.formControls["measValue1FormControl"].valid){
      value1 = Number(this.formControls["measValue1FormControl"].value);
    }
    else{
      this.formControls["measValue1FormControl"].markAsTouched();
      return;
    }
    if (this.measurementType.idMeasurementType == "2"){
      if (this.formControls["measValue2FormControl"].valid){
        value2 = Number(this.formControls["measValue2FormControl"].value);
      }
      else{
        this.formControls["measValue2FormControl"].markAsTouched();
        return;
      }
    }
    else{
      value2 = -10000;
    }
    mrc.idMeasurementType = this.measurementType.idMeasurementType;
    mrc.startDate = this.startDate;
    switch(this.toggleGroupSelectedMeal){
      case "2":
        mrc.idHavingMealsType = 1;
        if (this.formControls["mealTimeFormControl"].valid){
          mrc.havingMealsTime = -this.formControls["mealTimeFormControl"].value;
          break;
        }
        else{
          this.formControls["mealTimeFormControl"].markAsTouched();
          return;
        }
      case "3":
        mrc.idHavingMealsType = 2;
        break;
      case "4":
        mrc.idHavingMealsType = 3;
        if (this.formControls["mealTimeFormControl"].valid){
          mrc.havingMealsTime = this.formControls["mealTimeFormControl"].value;
          break;
        }
        else{
          this.formControls["mealTimeFormControl"].markAsTouched();
          return;
        }
    }
    
    mrc.reminderTimes = [new Date("1970-01-01T" + this.reminderTime + ":00")];
    mrc.isActive = 1;
    console.log(mrc);
    this.reminderItemsService.sendOneTimeMeasurementReminderEntry(mrc, value1, value2)
      .subscribe(() => {
        console.log("done");
        this.router.navigate(['/today']);
    });
  }
}
