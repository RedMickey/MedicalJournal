import { Component, OnInit } from '@angular/core';
import {FormControl, Validators} from '@angular/forms';
import { ISelect } from '../../models/ISelect';
import {Location} from '@angular/common';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  hidePassword = true;

  selectedGenderType = 1;
  selectedYear: number;

  email = new FormControl('', [Validators.required, Validators.email]);

  genderTypes: ISelect[] = [
    {value: 1, viewValue: 'Пол'},
    {value: 2, viewValue: 'Мужской'},
    {value: 3, viewValue: 'Женский'}
  ];

  years: ISelect[] = [];

  constructor(private location: Location) {
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
    return this.email.hasError('required') ? 'You must enter a value' :
        this.email.hasError('email') ? 'Not a valid email' :
            '';
  }

  backClicked(){
    this.location.back();
  }

}
