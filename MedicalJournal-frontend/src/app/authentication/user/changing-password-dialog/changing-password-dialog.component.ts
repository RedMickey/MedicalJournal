import { Component, OnInit, Inject } from '@angular/core';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';
import {FormControl, FormGroupDirective, NgForm, Validators} from '@angular/forms';

@Component({
  selector: 'app-changing-password-dialog',
  templateUrl: './changing-password-dialog.component.html',
  styleUrls: ['./changing-password-dialog.component.css']
})
export class ChangingPasswordDialogComponent implements OnInit {
  error = "";

  password1FormControl = new FormControl('', [
    Validators.required,
    Validators.minLength(5),
  ]);

  password2FormControl = new FormControl('', [
    Validators.required,
    Validators.minLength(5),
  ]);


  constructor(
    public dialogRef: MatDialogRef<ChangingPasswordDialogComponent>
  ) { 

  }

  ngOnInit() {
  }

  onDialogResult(isAdd: boolean): void {
    if (isAdd){
      if (this.password1FormControl.invalid)
      {
        this.password1FormControl.markAsTouched();
        return;
      }
      if (this.password2FormControl.invalid)
      {
        this.password2FormControl.markAsTouched();
        return;
      }
      if (this.password1FormControl.value != this.password2FormControl.value)
      {
        this.error = "Введённые пароли не совпадают";
        return;
      }
      this.dialogRef.close({
        isAdd: isAdd,
        password: this.password1FormControl.value
      });
    }
    else{
      this.dialogRef.close({isAdd: isAdd});
    }
  }

}
