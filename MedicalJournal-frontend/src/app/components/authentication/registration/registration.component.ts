import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { ISelect } from '../../../models/ISelect';
import { Location } from '@angular/common';
import { User } from '../../../models/User';
import { AuthService } from '../../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  hidePassword = true;
  avatar_urn = "avatar.png";

  selectedGenderType = 1;
  selectedYear: number;

  //email = new FormControl('', [Validators.required, Validators.email]);
  hasReqError: boolean = false;

  formControls = {
    "userNameFormControl": new FormControl('', [
      Validators.required,
    ]),
    "userSurnameFormControl": new FormControl('', [
    ]),
    "emailFormControl": new FormControl('', [
      Validators.required,
      Validators.email,
    ]),
    "passwordFormControl": new FormControl('', [
      Validators.required,
      Validators.minLength(5),
    ])
  };

  genderTypes: ISelect[] = [
    {value: 1, viewValue: 'Пол'},
    {value: 2, viewValue: 'Мужской'},
    {value: 3, viewValue: 'Женский'}
  ];

  years: ISelect[] = [];

  constructor(private location: Location,
    private authService: AuthService,
    private router: Router) {
    let endYear = new Date().getFullYear() - 10;
    let startYear = endYear-110;
    for (let i = endYear; i > startYear; i--) {
      this.years.push({value: i, viewValue: String(i)});
    }
    this.selectedYear = endYear;
   }

  ngOnInit() {
  }

  getEmailErrorMessage() {
    return this.formControls['emailFormControl'].hasError('required') ? 'Введите значение' :
        this.formControls['emailFormControl'].hasError('email') ? 'Неверный ввод данных' :
            '';
  }

  backClicked(){
    this.location.back();
  }

  onGenderSelectionChanged(event){
    switch(event.value) {
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

  submit(){
    let user = new User();
    if (this.formControls["userNameFormControl"].valid)
    {
      user.name = this.formControls["userNameFormControl"].value;
    }
    else{
      this.formControls["userNameFormControl"].markAsTouched();
      return;
    }
    user.surname = this.formControls["userSurnameFormControl"].value;
    if (this.formControls["emailFormControl"].valid)
    {
      user.email = this.formControls["emailFormControl"].value;
    }
    else{
      this.formControls["emailFormControl"].markAsTouched();
      return;
    }
    if (this.formControls["passwordFormControl"].valid)
    {
      user.password = this.formControls["passwordFormControl"].value;
    }
    else{
      this.formControls["passwordFormControl"].markAsTouched();
      return;
    }
    user.genderId = this.selectedGenderType;
    user.birthdayYear = this.selectedYear;
    user.synchronizationTime = new Date();
    user.roleId = 1;

    this.authService.register(user).subscribe(insertedUser => {
      if (insertedUser === undefined)
        this.hasReqError = true;
      else
        {
          this.hasReqError = false;
          console.log(insertedUser);
          this.authService.login(user.email, user.password)
            .subscribe(response => {
              console.log(response)
              if (response === undefined)
                this.router.navigate(["/login"]);
              else
                {
                  this.router.navigate(["/today"]);
                }
            });
        }
    });
  }

}
