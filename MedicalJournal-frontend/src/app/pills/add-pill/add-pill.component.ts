import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroupDirective, NgForm, Validators} from '@angular/forms';
import { ISelect } from '../../models/ISelect'

@Component({
  selector: 'app-add-pill',
  templateUrl: './add-pill.component.html',
  styleUrls: ['./add-pill.component.css']
})
export class AddPillComponent implements OnInit {
  opened = true;
  radioGroupSelected = 1;
  selectedDateType1 = 1;
  selectedDateType2 = 1;

  nameFormControl = new FormControl('', [
    Validators.required,
  ]);

  doseFormControl = new FormControl('', [
    Validators.required,
  ]);

  constructor() { }

  change(): void{
    this.opened = !this.opened;
  }

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

  selected: number;

  ngOnInit() {
    this.selected = this.doseTypes[0].value;
  }

}
