import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { User } from 'src/app/model/User';
import { CognitoService } from 'src/app/service/cognito.service';
@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent implements OnInit {

  user: User = {} as User;
  isConfirm: boolean = false;
  alertMessage: string = '';
  showAlert: boolean = false;

  constructor(
    private router: Router,
    private cognitoService: CognitoService,
    private snackBar: MatSnackBar) { }


  ngOnInit(): void {
    this.user = {} as User;
    this.isConfirm = false;
  }


  public signUpWithCognito() {
    if (this.user && this.user.email && this.user.password) {
      this.cognitoService.signUp(this.user)
        .then(() => {
          this.isConfirm = true;
        })
        .catch((error: any) => {
          this.displayAlert(error.message)
        })
    }
    else {
      this.displayAlert("Missing email or password")
    }
  }

  public confirmSignUp() {
    if (this.user) {
      this.cognitoService.confirmSignUp(this.user)
        .then(() => {
          this.router.navigate(['/login'])
        })
        .catch((error: any) => {
          this.displayAlert(error.message);
        })
    }
    else {
      this.displayAlert("Missing user information");
    }
  }

  private displayAlert(message: string) {
    this.snackBar.open(message, "OK", {
      duration: 5000
    });
  }
}