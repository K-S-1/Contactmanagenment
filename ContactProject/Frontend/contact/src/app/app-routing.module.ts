import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ErrorComponent } from './error/error.component';
import { RegistrationComponent } from './registration/registration.component';
import { LoginComponent } from './login/login.component';
import { ForgotpasswordComponent } from './forgotpassword/forgotpassword.component';
import { AddContactComponent } from './add-contact/add-contact.component';
import { UpdateContactComponent } from './update-contact/update-contact.component';
import { DeleteContactComponent } from './delete-contact/delete-contact.component';
import { GetContactComponent } from './get-contact/get-contact.component';
import { HomeComponent } from './home/home.component';
import { authGuard } from './guards/auth.guard';
import { ResetPasswordComponent } from './reset-password/reset-password.component';

const routes: Routes = [{path:'', component:HomeComponent},
  {path:'add-contact', component:AddContactComponent,canActivate:[authGuard]},
  {path:'update-contact', component:UpdateContactComponent,canActivate:[authGuard]},
  {path:'delete-contact', component:DeleteContactComponent,canActivate:[authGuard]},
  {path:'get-contact', component:GetContactComponent,canActivate:[authGuard]},
  {path:'registration-user', component:RegistrationComponent},
  {path:'login-user', component:LoginComponent},
  { path: 'forgot-password', component: ForgotpasswordComponent },
  { path: 'reset-password', component: ResetPasswordComponent },
  {path:'home', component:HomeComponent},
  {path:'**', component:ErrorComponent}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  // imports: [RouterModule.forChild(routes)], // what is this???
  exports: [RouterModule]
})
export class AppRoutingModule { }
