import {
  HttpClient,
  HttpErrorResponse,
  HttpHeaders,
  HttpParams,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Contact } from '../models/contact';
import { Login } from '../models/login';
import { Registration } from '../models/Registration';
import { LoginResponse } from '../models/LoginResponse';

interface ForgotPasswordRequest {
  userName: string;
}

interface ForgotPasswordResponse {
  resetLink: string;
  message?: string;
  error?: string;
}

interface ResetPasswordRequest {
  token: string;
  newPassword: string;
}

interface ResetPasswordResponse {
  message: string;
  error?: string;
}

@Injectable({
  providedIn: 'root',
})
export class ContactServiceService {
  private baseURlCM = '/contactManagement';
  private baseURlCU = '/contactUser';
  private baseURlAU = '/auth';

  constructor(private http: HttpClient) { }

  // getAllData(): Observable<Contact[]> {
  //   return this.http.get<Contact[]>(`${this.baseURlCU}/findAllContact`)
  //     .pipe(
  //       catchError(this.handleError)
  //     );
  // }

  getAllData(
    page: number = 0,
    size: number = 10,
    sortBy: string = 'id',
    direction: string = 'ASC'
  ): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('direction', direction);

    return this.http
      .get<any>(`${this.baseURlCM}/getAllContact`, { params })
      .pipe(catchError(this.handleError));
  }

  addData(contact: Contact): Observable<string> {
    return this.http
      .post(`${this.baseURlCM}/addContact`, contact, {
        responseType: 'text',
      })
      .pipe(
        catchError(this.handleError) // Call the handleError method
      );
  }

  updateData(id: number, contact: Contact): Observable<string> {
    return this.http
      .put(`${this.baseURlCM}/updateContact/${id}`, contact, {
        responseType: 'text',
      })
      .pipe(
        catchError(this.handleError) // Call the handleError method
      );
  }

  deleteData(id: number): Observable<string> {
    return this.http
      .delete(`${this.baseURlCM}/deleteContact/${id}`, {
        responseType: 'text',
      })
      .pipe(
        catchError(this.handleError) // Call the handleError method
      );
  }

  loginUser(login: Login): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.baseURlAU}/login`, login).pipe(
      catchError(this.handleError) // Call the handleError method
    );
  }

  registrationUser(registration: Registration): Observable<string> {
    return this.http
      .post(`${this.baseURlAU}/registration`, registration, {
        responseType: 'text',
      })
      .pipe(
        catchError(this.handleError) // Call the handleError method
      );
  }

  forgotPassword(email: string): Observable<ForgotPasswordResponse> {
    console.log(
      `Sending forgot password request to: ${this.baseURlAU}/forgotPassword`
    );
    const requestBody: ForgotPasswordRequest = { userName: email };
    return this.http
      .post<ForgotPasswordResponse>(
        `${this.baseURlAU}/forgotPassword`,
        requestBody,
        {
          headers: new HttpHeaders({
            'Content-Type': 'application/json',
          }),
        }
      )
      .pipe(catchError(this.handleError));
  }

  resetPassword(
    token: string,
    password: string
  ): Observable<ResetPasswordResponse> {
    console.log(
      `Sending reset password request to: ${this.baseURlAU}/resetPassword`
    );
    const requestBody: ResetPasswordRequest = { token, newPassword: password };
    return this.http
      .post<ResetPasswordResponse>(
        `${this.baseURlAU}/resetPassword`,
        requestBody,
        {
          headers: new HttpHeaders({
            'Content-Type': 'application/json',
          }),
        }
      )
      .pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    console.error('API Error Object:', error);

    let errorMessage = 'An unknown error occurred. Please try again later.';

    if (error.error) {
      if (typeof error.error === 'string') {
        errorMessage = error.error;
      } else if (error.error.error) {
        errorMessage = error.error.error;
      } else if (error.error.message) {
        errorMessage = error.error.message;
      }
    } else if (error.message) {
      errorMessage = error.message;
    }
    if (error.status === 0) {
      errorMessage =
        'Could not connect to the server. Please check your network connection.';
    } else if (error.status === 404) {
      errorMessage = `Resource not found (404). Please check the API URL. (${error.url})`;
    } else if (error.status === 400 && !errorMessage.includes('required')) {
      errorMessage = `Invalid request (400). ${errorMessage}`;
    } else if (error.status === 500) {
      errorMessage = `Server error (500). Please contact support or try again later. ${errorMessage}`;
    }
    console.error(`Final Error Message: ${errorMessage}`);
    return throwError(() => new Error(errorMessage));
  }
}
