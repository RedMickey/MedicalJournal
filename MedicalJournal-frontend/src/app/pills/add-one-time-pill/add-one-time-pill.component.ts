import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroupDirective, NgForm, Validators} from '@angular/forms';
import {MatDatepickerInputEvent} from '@angular/material/datepicker';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { ReminderItemsService } from '../../services/reminder-items.service';
import { ISelect } from '../../models/ISelect';
import { PillReminderCourse } from '../../models/PillReminderCourse';
import { ChoosingPillReminderDialogComponent } from './choosing-pill-reminder-dialog/choosing-pill-reminder-dialog.component';
import { MatDialog, MatDialogRef } from '@angular/material';

@Component({
  selector: 'app-add-one-time-pill',
  templateUrl: './add-one-time-pill.component.html',
  styleUrls: ['./add-one-time-pill.component.css']
})
export class AddOneTimePillComponent implements OnInit {

  mealTypePrefix = "до";
  toggleGroupSelectedMeal = "1";
  startDate: Date;
  reminderTime: string;
  selectedDoseType: number;

  formControls = {
    "doseFormControl": new FormControl(1, [
      Validators.required,
      Validators.pattern('^[1-9]{1}[0-9]*$'),
    ]),
    "pillNameFormControl": new FormControl('', [
      Validators.required,
    ]),
    "mealTimeFormControl": new FormControl(10, [
      Validators.required,
      Validators.pattern('^[1-9]{1}[0-9]*$'),
    ]) 
  };

  doseTypes: ISelect[] = [
    {value: 1, viewValue: 'шт'},
    {value: 5, viewValue: 'мл'},
    {value: 4, viewValue: 'гр'},
    {value: 3, viewValue: 'мг'},
    {value: 2, viewValue: 'капли'}
  ];

  constructor(private route: ActivatedRoute,
    private router: Router,
    private reminderItemsService: ReminderItemsService,
    public dialog: MatDialog) { 
    this.startDate = new Date();
    this.reminderTime = this.startDate.toTimeString().substring(0, 5);
    }

  ngOnInit() {
    this.selectedDoseType = this.doseTypes[0].value; 
  }

  ngAfterViewInit() {
    let numberSpan = document.querySelector('.bar-title');
    numberSpan.textContent = "Добавить лекарство";
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
    let prc: PillReminderCourse = new PillReminderCourse();
    if (this.formControls["pillNameFormControl"].valid){
      prc.pillName = this.formControls["pillNameFormControl"].value;
    }
    else{
      this.formControls["pillNameFormControl"].markAsTouched();
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
    prc.isActive = 1;
    prc.reminderTimes = [new Date("1970-01-01T" + this.reminderTime + ":00")];
    console.log(prc);
    this.reminderItemsService.sendOneTimePillReminderEntry(prc)
      .subscribe(() => {
        console.log("done");
        this.router.navigate(['/today']);
    });
  }

  openChoosingDialog(): void{
    const dialogRef = this.dialog.open(ChoosingPillReminderDialogComponent);

    dialogRef.afterClosed().subscribe(result => {
      if (result !== undefined){
        if (result.isItemSelected){
          this.formControls["pillNameFormControl"].setValue(
            result.pillReminderItem.pillName);
          this.formControls["doseFormControl"].setValue(
            result.pillReminderItem.pillCount);
          this.selectedDoseType = this.doseTypes.find(x => 
            x.viewValue === result.pillReminderItem.pillCountType).value;
          switch(result.pillReminderItem.havingMealsType){
            case 0:
              this.toggleGroupSelectedMeal = "1";
              break;
            case 1:
              this.toggleGroupSelectedMeal = "2";
              this.mealTypePrefix = "до";
              break;
            case 2:
              this.toggleGroupSelectedMeal = "3";
              break;
            case 3:
              this.toggleGroupSelectedMeal = "4";
              this.mealTypePrefix = "после";
              break;
            }
        }
        else{
          this.formControls["pillNameFormControl"].setValue("");
          this.toggleGroupSelectedMeal = "1";
        }
      }
    });
  }
}
