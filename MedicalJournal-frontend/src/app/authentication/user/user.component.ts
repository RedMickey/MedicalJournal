import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { ISelect } from '../../models/ISelect';
import { Location } from '@angular/common';
import { User } from '../../models/User';
import { UserService } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';
import { MatDialog, MatDialogRef } from '@angular/material';
import { ChangingPasswordDialogComponent } from './changing-password-dialog/changing-password-dialog.component';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  hidePassword = true;
  avatar_urn = "avatar.png";

  selectedGenderType = 1;
  selectedYear: number;

  hasReqError: boolean = false;

  user: User;

  formControls = {
    "userNameFormControl": new FormControl('', [
      Validators.required,
    ]),
    "userSurnameFormControl": new FormControl('', [
    ]),
    "emailFormControl": new FormControl('', [
      Validators.required,
      Validators.email,
    ])
  };

  genderTypes: ISelect[] = [
    {value: 1, viewValue: 'Пол'},
    {value: 2, viewValue: 'Мужской'},
    {value: 3, viewValue: 'Женский'}
  ];

  years: ISelect[] = [];

  constructor(private location: Location,
    private userService: UserService,
    private authService: AuthService,
    public dialog: MatDialog) {
    let endYear = new Date().getFullYear() - 10;
    let startYear = endYear-110;
    for (let i = endYear; i > startYear; i--) {
      this.years.push({value: i, viewValue: String(i)});
    }
    this.selectedYear = endYear;
   }

  ngOnInit() {
    this.userService.getUserById().subscribe(
      user => { this.user = user;
        this.user.synchronizationTime = new Date(user.synchronizationTime);
        console.log(this.user);

        this.setupAvatarImage(this.user.genderId);
        this.formControls["userNameFormControl"].setValue(this.user.name);
        this.formControls["userSurnameFormControl"].setValue(this.user.surname);
        this.formControls["emailFormControl"].setValue(this.user.email);
        this.selectedGenderType = this.user.genderId;
        this.selectedYear = this.user.birthdayYear;
      }
    );
  }

  ngAfterViewInit() {
    let numberSpan = document.querySelector('.bar-title');
    numberSpan.textContent = "Профиль";
  }

  getEmailErrorMessage() {
    return this.formControls['emailFormControl'].hasError('required') ? 'Введите значение' :
        this.formControls['emailFormControl'].hasError('email') ? 'Неверный ввод данных' :
            '';
  }

  onGenderSelectionChanged(event){
    this.setupAvatarImage(event.value);
  }

  setupAvatarImage(genderId: number){
    switch(genderId) {
      case 1:
        this.avatar_urn = "avatar.png";
        break;
      case 2:
        this.avatar_urn = "boy.png";
        break;
      case 3:
        this.avatar_urn = "girl.png";
        break;
    }
  }

  changePassword(){
    const dialogRef = this.dialog.open(ChangingPasswordDialogComponent);

    dialogRef.afterClosed().subscribe(result => {
      if (result){
        if (result.isAdd){
          this.user.password = result.password;
        }
      }
    });
  }

  submit(){
    if (this.formControls["userNameFormControl"].valid)
    {
      this.user.name = this.formControls["userNameFormControl"].value;
    }
    else{
      this.formControls["userNameFormControl"].markAsTouched();
      return;
    }
    this.user.surname = this.formControls["userSurnameFormControl"].value;
    if (this.formControls["emailFormControl"].valid)
    {
      this.user.email = this.formControls["emailFormControl"].value;
    }
    else{
      this.formControls["emailFormControl"].markAsTouched();
      return;
    }

    this.user.genderId = this.selectedGenderType;
    this.user.birthdayYear = this.selectedYear;
    this.user.roleId = 1;

    console.log(this.user);

    this.userService.updateUser(this.user)
      .subscribe(() => {
        this.authService.updateCurrentUser(this.user);
        console.log("done")

      });
  }

}
