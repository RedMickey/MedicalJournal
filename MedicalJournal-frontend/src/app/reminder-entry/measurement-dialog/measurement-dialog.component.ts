import { Component, OnInit, Inject } from '@angular/core';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';
import {FormControl, FormGroupDirective, NgForm, Validators} from '@angular/forms';
import {ErrorStateMatcher} from '@angular/material/core';

@Component({
  selector: 'app-measurement-dialog',
  templateUrl: './measurement-dialog.component.html',
  styleUrls: ['./measurement-dialog.component.css']
})
export class MeasurementDialogComponent implements OnInit {
  error = "";

  valueStr1FormControl = new FormControl('', [
    Validators.required,
    Validators.pattern('^[0-9]+[.]?[0-9]*$'),
  ]);

  valueStr2FormControl = new FormControl('', [
    Validators.required,
    Validators.pattern('^[0-9]+[.]?[0-9]*$'),
  ]);

  constructor(
    public dialogRef: MatDialogRef< MeasurementDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) { 
    if (data.value1 != -10000)
      this.valueStr1FormControl.setValue(String(data.value1));
    if (data.value2 != -10000)
      this.valueStr2FormControl.setValue(String(data.value2));
  }

  ngOnInit() {
  }

  onDialogResult(isAdd: boolean): void {
    if (isAdd){
      if (this.validateInput()){
        this.dialogRef.close(
          {
            value1: Number(this.valueStr1FormControl.value),
            value2: this.data.idMeasurementType == 2?Number(this.valueStr2FormControl.value):-10000,
            isAdd: isAdd
          }
        );
      }
    }
    else{
      this.dialogRef.close({isAdd: isAdd});
    }
  }

  validateInput(): boolean{
    if (this.valueStr1FormControl.hasError('pattern')||(this.valueStr2FormControl.hasError('pattern')&&this.data.idMeasurementType == 2)){
      this.error = "Некорректное значение"
      return false;
    }
    if (this.valueStr1FormControl.hasError('required')||(this.valueStr2FormControl.hasError('required')&&this.data.idMeasurementType == 2)){
      this.error = "Введите значения"
      return false;
    }
    return true;
  }

}
