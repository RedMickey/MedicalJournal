import { Component, OnInit, ElementRef } from '@angular/core';
import {FormControl, FormGroupDirective, NgForm, Validators} from '@angular/forms';
import { ISelect } from '../../../models/ISelect';
import {MatDatepickerInputEvent} from '@angular/material/datepicker';
import {PillReminderCourse} from '../../../models/PillReminderCourse';
import {CycleDBInsertEntry} from '../../../models/CycleDBInsertEntry'
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { switchMap } from 'rxjs/operators';
import { ReminderItemsService } from '../../../services/reminder-items.service';


@Component({
  selector: 'app-add-pill',
  templateUrl: './add-pill.component.html',
  styleUrls: ['./add-pill.component.css']
})
export class AddPillComponent implements OnInit {
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

  pillReminderId: any;
  cycleId: string;
  weekScheduleId: string = null;

  nameFormControl = new FormControl('', [
    Validators.required,
  ]);

  formControls = {
    "doseFormControl": new FormControl(1, [
      Validators.required,
      Validators.pattern('^[1-9]{1}[0-9]*$'),
    ]),
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
   }

  ngAfterViewInit() {
    let numberSpan = document.querySelector('.bar-title');
    if (this.pillReminderId)
      numberSpan.textContent = "Редактировать курс";
    else
      numberSpan.textContent = "Добавить курс лекрств";
  }

  change(): void{
    this.opened = !this.opened;
  }

  reminderTimeEntries: string[] = [
    new Date().toTimeString().substring(0, 5)
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
    this.selectedDoseType = this.doseTypes[0].value;    
    this.pillReminderId = this.route.snapshot.paramMap.get('id');
    if (this.pillReminderId)
    {
      this.reminderItemsService.getPillReminderCourse(this.pillReminderId)
          .subscribe(cycleAndPillComby => {this.setupCourseData(cycleAndPillComby)
              console.log(cycleAndPillComby);
          });
    }
    console.log(this.pillReminderId);
  }

  addReminderTimeEntry(): void{
    this.reminderTimeEntries.push(
      new Date().toTimeString().substring(0, 5)
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

  setupCourseData(cycleAndPillComby): void{
    let prc= cycleAndPillComby.pillReminderCourse;
    let ce = cycleAndPillComby.cycleDBInsertEntry;
    this.cycleId = prc.idCycle;
    this.isActiveIndicator = prc.isActive;
    this.nameFormControl.setValue(prc.pillName);
    this.formControls["doseFormControl"].setValue(prc.pillCount);
    this.selectedDoseType = prc.idPillCountType;
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
    switch(prc.idHavingMealsType){
      case 0:
        this.toggleGroupSelectedMeal = "1";
        break;
      case 1:
        this.toggleGroupSelectedMeal = "2";
        this.formControls["mealTimeFormControl"].setValue(Math.abs(prc.havingMealsTime));
        this.mealTypePrefix = "до";
        break;
      case 2:
        this.toggleGroupSelectedMeal = "3";
        break;
      case 3:
        this.toggleGroupSelectedMeal = "4";
        this.mealTypePrefix = "после";
        this.formControls["mealTimeFormControl"].setValue(prc.havingMealsTime);
        break;
    }
    this.reminderTimeEntries = prc.reminderTimes.map(timeStr => timeStr.slice(0,5));
    this.annotation = prc.annotation;
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

    let prc: PillReminderCourse = new PillReminderCourse();
      if (this.nameFormControl.valid){
        prc.pillName = this.nameFormControl.value;
      }
      else{
        this.nameFormControl.markAsTouched();
        return;
      }
      if (this.formControls["doseFormControl"].valid){
        prc.pillCount = this.formControls["doseFormControl"].value;
      }
      else{
        this.formControls["doseFormControl"].markAsTouched();
        return;
      }
      prc.idPillCountType = this.selectedDoseType;
      prc.startDate = this.startDate;
      switch(this.toggleGroupSelectedMeal){
        case "2":
          prc.idHavingMealsType = 1;
          if (this.formControls["mealTimeFormControl"].valid){
            prc.havingMealsTime = -this.formControls["mealTimeFormControl"].value;
            break;
          }
          else{
            this.formControls["mealTimeFormControl"].markAsTouched();
            return;
          }
        case "3":
          prc.idHavingMealsType = 2;
          break;
        case "4":
          prc.idHavingMealsType = 3;
          if (this.formControls["mealTimeFormControl"].valid){
            prc.havingMealsTime = this.formControls["mealTimeFormControl"].value;
            break;
          }
          else{
            this.formControls["mealTimeFormControl"].markAsTouched();
            return;
          }
      }
      prc.annotation = this.annotation;
      prc.isActive = Number(this.isActiveIndicator);
      prc.reminderTimes = new Array();

      console.log(this.reminderTimeEntries);
      this.reminderTimeEntries.forEach(item => {
        prc.reminderTimes.push(
          new Date("1970-01-01T" + item + ":00")
          );
      });

      console.log(cdbie);
      console.log(prc);
      let type = 1;
      if (this.pillReminderId){
        prc.idPillReminder = this.pillReminderId;
        prc.idCycle = this.cycleId;
        cdbie.idCycle = this.cycleId;
        cdbie.idWeekSchedule = this.weekScheduleId;
        type = 2;
      }
      this.reminderItemsService.sendPillReminderCourse(prc, cdbie, type)
        .subscribe(() => {
          console.log("done");
          this.router.navigate(['/pills']);
        });
  }

  deleteCourse(): void{
    let deletionReqBody = {
      idReminder: this.pillReminderId,
      idCycle: this.cycleId,
      idWeekSchedule: this.weekScheduleId,
      courseType: 1
    }
    this.reminderItemsService.deleteReminderCourse(deletionReqBody)
      .subscribe(() => {
        console.log("done");
        this.router.navigate(['/pills']);
      });
  }

}
