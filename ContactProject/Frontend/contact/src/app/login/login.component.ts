import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ContactServiceService } from '../service/contact-service.service';
import { Login } from '../models/login';
import { finalize, startWith } from 'rxjs';
import { LoginResponse } from '../models/LoginResponse';
import { AuthServiceService } from '../service/auth-service.service';
import { STRING_TYPE, StringToken, StringTokenKind } from '@angular/compiler';
import { Router } from '@angular/router';
// import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  router = inject(Router);

  loginForm!: FormGroup;
  isSubmitting = false;
  showPassword = false;
  submitSuccess = false;

  constructor(
    private fb: FormBuilder,
    private contactService: ContactServiceService
  ) {}

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      userName: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
    });
  }

  get f() {
    return this.loginForm.controls;
  }
  loginService=inject(AuthServiceService)

  // jwttoken:LoginResponse[]=[]
  submittedForm() {
    this.submitSuccess = false;

    if (this.loginForm.valid) {
      this.isSubmitting = true;
      let loginData=this.loginForm?.value;
      // const loginData: Login = this.loginForm.value; 
      this.contactService
            .loginUser(loginData).subscribe({
              next: (response:LoginResponse) => {
                // this.jwttoken=response
                if(response){
                  // sessionStorage.setItem('token',response);
                  this.loginService.setLoginData('token',response.jwtToken);
                }
                console.log('response----------> : ', response);
                console.log('jwttoken =========> ', response.jwtToken);
                this.submitSuccess = true;
                // this.loginForm.reset(); 
                this.router.navigate(['/add-contact']);
              },
              error: (error: Error) => {
                console.error('Error adding contact:', error);
              },
            });
      
      setTimeout(() => {
        console.log('Form submitted:', this.loginForm.value);
        this.isSubmitting = false;
      }, 1500);

      // // Navigate to the ProductListComponent upon successful login
      // this.router.navigate(['/add-contact']);
    }


  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }
}
