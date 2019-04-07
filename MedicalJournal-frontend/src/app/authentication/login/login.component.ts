import { Component, OnInit } from '@angular/core';
import {FormControl, Validators} from '@angular/forms';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  hidePassword = true;

  emailFormControl = new FormControl('', [Validators.required, Validators.email]);
  passwordFormControl = new FormControl('', [Validators.required,]);

  constructor(private authService: AuthService) { }

  ngOnInit() {
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
      .subscribe(response => console.log(response));
  }

}
