import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroupDirective, NgForm, Validators} from '@angular/forms';
import { ISelect } from '../../models/ISelect';
import {MatDatepickerInputEvent} from '@angular/material/datepicker';
import {MeasurementReminderCourse} from '../../models/MeasurementReminderCourse';
import {CycleDBInsertEntry} from '../../models/CycleDBInsertEntry'
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { ReminderItemsService } from '../../services/reminder-items.service';

@Component({
  selector: 'app-add-measurement',
  templateUrl: './add-measurement.component.html',
  styleUrls: ['./add-measurement.component.css']
})
export class AddMeasurementComponent implements OnInit {
  opened = true;
  selectedDoseType: number;
  radioGroupSelectedCycle = 1;
  selectedDateTypeED = 1;
  selectedDateTypeCD = 1;
  selectedDateTypeDI = 1;
  selectedDateTypeDIP = 1;
  toggleGroupSelectedMeal = "1";
  annotation = "";
  startDate: Date;
  weekSchedule: boolean[] = [
    false, false, false, false,
    false, false, false
  ];
  isActiveIndicator = true;

  mealTypePrefix = "до";

  measurementType: any;
  measurementReminderId: any;
  cycleId: string;
  weekScheduleId: string = null;

  formControls = {
    "evrDayFormControl": new FormControl(10, [
      Validators.required,
      Validators.pattern('^[1-9]{1}[0-9]*$'),
    ]),
    "certDaysFormControl": new FormControl(10, [
      Validators.required,
      Validators.pattern('^[1-9]{1}[0-9]*$'),
    ]),
    "dayIntFormControl": new FormControl(10, [
      Validators.required,
      Validators.pattern('^[1-9]{1}[0-9]*$'),
    ]),
    "dayIntPerFormControl": new FormControl(10, [
      Validators.required,
      Validators.pattern('^[1-9]{1}[0-9]*$'),
    ]),
    "mealTimeFormControl": new FormControl(10, [
      Validators.required,
      Validators.pattern('^[1-9]{1}[0-9]*$'),
    ]) 
  };

  constructor(private route: ActivatedRoute,
    private reminderItemsService: ReminderItemsService,
    private router: Router) {
      this.startDate = new Date();
      route.queryParams.subscribe(
        (queryParam: any) => {
            this.measurementType = {
              idMeasurementType: queryParam['idMT'],
              typeName: queryParam['typeName']
            };
            let numberSpan = document.querySelector('.bar-title');
            numberSpan.textContent = queryParam['typeName'];
        }
    );
  }

  /*sngAfterViewInit() {
    let numberSpan = document.querySelector('.bar-title');
    if (this.measurementReminderId)
      numberSpan.textContent = "Редактировать курс лекарств";
    else
      numberSpan.textContent = "Добавить курс лекрств";
  }*/

  change(): void{
    this.opened = !this.opened;
  }

  reminderTimeEntries: string[] = [
    String(new Date().getHours()) + ":00" 
  ]

  doseTypes: ISelect[] = [
    {value: 1, viewValue: 'шт'},
    {value: 5, viewValue: 'мл'},
    {value: 4, viewValue: 'гр'},
    {value: 3, viewValue: 'мг'},
    {value: 2, viewValue: 'капли'}
  ];

  dateTypes: ISelect[] = [
    {value: 1, viewValue: 'дн.'},
    {value: 2, viewValue: 'нед.'},
    {value: 3, viewValue: 'мес.'}
  ];

  ngOnInit() {
    this.measurementReminderId = this.route.snapshot.paramMap.get('id');
    if (this.measurementReminderId)
    {
      this.reminderItemsService.getMeasurementReminderCourse(this.measurementReminderId)
          .subscribe(cycleAndMeasurementComby => {this.setupCourseData(cycleAndMeasurementComby)
              console.log(cycleAndMeasurementComby);
          });
    }
  }

  addReminderTimeEntry(): void{
    this.reminderTimeEntries.push(
      String(new Date().getHours()) + ":00"
    );
    console.log(this.reminderTimeEntries);
  }

  deleteReminderTimeEntry(index): void{
    console.log(this.reminderTimeEntries);
    console.log(index);
    if (this.reminderTimeEntries.length>1)
      this.reminderTimeEntries.splice(index, 1);
    console.log(this.reminderTimeEntries);
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

  setupCourseData(cycleAndMeasurementComby): void{
    let mrc= cycleAndMeasurementComby.measurementReminderCourse;
    let ce = cycleAndMeasurementComby.cycleDBInsertEntry;
    this.cycleId = mrc.idCycle;
    this.isActiveIndicator = mrc.isActive;
    this.radioGroupSelectedCycle = ce.idCyclingType;
    switch(ce.idCyclingType){
      case 1:
        this.formControls["evrDayFormControl"].setValue(ce.period);
        this.selectedDateTypeED = ce.periodDMType;
        break;
      case 2:
        this.formControls["certDaysFormControl"].setValue(ce.period);
        this.selectedDateTypeCD = ce.periodDMType;
        this.weekSchedule = ce.weekSchedule;
        this.weekScheduleId = ce.idWeekSchedule;
        break;
      case 3:
        this.formControls["dayIntFormControl"].setValue(ce.onceAPeriod);
        this.selectedDateTypeDI = ce.onceAPeriodDMType;
        this.formControls["dayIntPerFormControl"].setValue(ce.period);
        this.selectedDateTypeDIP = ce.periodDMType;
        break;
    }
    switch(mrc.idHavingMealsType){
      case 0:
        this.toggleGroupSelectedMeal = "1";
        break;
      case 1:
        this.toggleGroupSelectedMeal = "2";
        this.formControls["mealTimeFormControl"].setValue(Math.abs(mrc.havingMealsTime));
        this.mealTypePrefix = "до";
        break;
      case 2:
        this.toggleGroupSelectedMeal = "3";
        break;
      case 3:
        this.toggleGroupSelectedMeal = "4";
        this.mealTypePrefix = "после";
        this.formControls["mealTimeFormControl"].setValue(mrc.havingMealsTime);
        break;
    }
    this.reminderTimeEntries = mrc.reminderTimes.map(timeStr => timeStr.slice(0,5));
    this.annotation = mrc.annotation;
  }

  submit(): void{
    let cdbie: CycleDBInsertEntry = new CycleDBInsertEntry();
    cdbie.idCyclingType = this.radioGroupSelectedCycle;
    switch(this.radioGroupSelectedCycle){
      case 1:
        if (this.formControls["evrDayFormControl"].valid)
        {
          cdbie.period = this.formControls["evrDayFormControl"].value;
          cdbie.periodDMType = this.selectedDateTypeED;
          break;
        }
        else{
          this.formControls["evrDayFormControl"].markAsTouched();
          return;
        }
      case 2:
        if (this.formControls["certDaysFormControl"].valid){
          cdbie.period = this.formControls["certDaysFormControl"].value;
          cdbie.weekSchedule = this.weekSchedule;
          cdbie.periodDMType = this.selectedDateTypeCD;
          break;
        }
        else{
          this.formControls["certDaysFormControl"].markAsTouched();
          return;
        }
      case 3:
        if (this.formControls["dayIntPerFormControl"].valid && 
            this.formControls["dayIntFormControl"].valid){
          cdbie.period = this.formControls["dayIntPerFormControl"].value;
          cdbie.onceAPeriod = this.formControls["dayIntFormControl"].value;
          cdbie.periodDMType = this.selectedDateTypeDIP;
          cdbie.onceAPeriodDMType = this.selectedDateTypeDI;
          break;
        }
        else{
          this.formControls["dayIntPerFormControl"].markAsTouched();
          this.formControls["dayIntFormControl"].markAsTouched();
          return;
        }
      }

    let mrc: MeasurementReminderCourse = new MeasurementReminderCourse();
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
    mrc.annotation = this.annotation;
    mrc.isActive = Number(this.isActiveIndicator);
    mrc.reminderTimes = new Array();
    this.reminderTimeEntries.forEach(item => {
      mrc.reminderTimes.push(
        new Date("1970-01-01T" + item + ":00")
        );
    });

      console.log(cdbie);
      console.log(mrc);
      let type = 1;
      if (this.measurementReminderId){
        mrc.idMeasurementReminder = this.measurementReminderId;
        mrc.idCycle = this.cycleId;
        cdbie.idCycle = this.cycleId;
        cdbie.idWeekSchedule = this.weekScheduleId;
        type = 2;
      }
      this.reminderItemsService.sendMeasurementReminderCourse(mrc, cdbie, type)
        .subscribe(() => {
          console.log("done");
          //this.router.navigate(['/measurements']);
        });
  }

  deleteCourse(): void{
    let deletionReqBody = {
      idReminder: this.measurementReminderId,
      idCycle: this.cycleId,
      idWeekSchedule: this.weekScheduleId,
      courseType: 2
    }
    this.reminderItemsService.deleteReminderCourse(deletionReqBody)
      .subscribe(() => {
        console.log("done");
        this.router.navigate(['/measurements']);
      });
  }

}
