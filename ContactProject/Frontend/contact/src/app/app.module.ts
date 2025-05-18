import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RegistrationComponent } from './registration/registration.component';
import { LoginComponent } from './login/login.component';
import { ErrorComponent } from './error/error.component';
import { ForgotpasswordComponent } from './forgotpassword/forgotpassword.component';
import { GetContactComponent } from './get-contact/get-contact.component';
import { UpdateContactComponent } from './update-contact/update-contact.component';
import { DeleteContactComponent } from './delete-contact/delete-contact.component';
import { AddContactComponent } from './add-contact/add-contact.component';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'; // <-- Import for Angular Animations
import {HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { HomeComponent } from './home/home.component';
import { NavbarComponent } from './navbar/navbar.component'; // <-- Import for API calls]
import { NgbModule } from '@ng-bootstrap/ng-bootstrap'; 
import { FormsModule } from '@angular/forms';
import { authInterceptorInterceptor } from './auth-interceptor.interceptor';
import { authGuard } from './guards/auth.guard';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
@NgModule({
  declarations: [
    AppComponent,
    RegistrationComponent,
    LoginComponent,
    ErrorComponent,
    ForgotpasswordComponent,
    GetContactComponent,
    UpdateContactComponent,
    DeleteContactComponent,
    AddContactComponent,
    HomeComponent,
    NavbarComponent,
    ResetPasswordComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    FormsModule,
    NgbModule,
    HttpClientModule, 
    ReactiveFormsModule
  ],
 
  providers: [  {
    provide: HTTP_INTERCEPTORS, useClass: authInterceptorInterceptor, multi: true
  }
  // ,[authGuard]
  // This above is not the correct way to provide a standalone function guard. authGuard doesn't need to be listed in providers at all —Angular will resolve the inject() calls from the injector when used as a standalone guard.
  ],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA] // Add this line
})
export class AppModule { }
