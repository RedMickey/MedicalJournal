import { Component, OnInit } from '@angular/core';
import {FormControl, Validators} from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { catchError } from 'rxjs/operators';
import { of, Observable } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  hidePassword = true;

  hasReqError: boolean = false;

  emailFormControl = new FormControl('', [Validators.required, Validators.email]);
  passwordFormControl = new FormControl('', [Validators.required,]);

  constructor(private authService: AuthService,
    private router: Router) { }

  ngOnInit() {
    //console.log(localStorage.getItem("access_token"));
  }

  getEmailErrorMessage() {
    return this.emailFormControl.hasError('required') ? 'Введите значение' :
        this.emailFormControl.hasError('email') ? 'Неверный ввод данных' :
            '';
  }

  submit(){
    if (this.emailFormControl.invalid){
      this.emailFormControl.markAsTouched();
      return;
    }
    if (this.passwordFormControl.invalid){
      this.passwordFormControl.markAsTouched();
      return;
    }
    
    console.log("gfg");

    this.authService.login(this.emailFormControl.value, this.passwordFormControl.value)
      .pipe(
        catchError(err => {
          console.log(err);
          return Observable.throw(err);})
      )
      .subscribe(response => {
        console.log(response)
        if (response === undefined)
          this.hasReqError = true;
        else
          {
            this.hasReqError = false;
            this.router.navigate(["/today"]);
          }
      },
        err => console.log("fdfs"));
  }

}
