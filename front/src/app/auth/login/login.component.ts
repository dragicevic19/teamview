import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { User } from 'src/app/model/User';
import { CognitoService } from 'src/app/service/cognito.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  user: User | undefined;
  alertMessage: string = '';
  showAlert: boolean = false;

  isForgotPassword: boolean = false;
  newPassword: string = '';

  constructor(private router: Router, private cognitoService: CognitoService,
    private snackBar: MatSnackBar) { }


  ngOnInit(): void {
    this.user = {} as User;
    this.cognitoService.signOut();
  }

  signInWithCognito() {
    if (this.user && this.user.email && this.user.password) {
      this.cognitoService.signIn(this.user)
        .then((cogUser) => {
          console.log(cogUser);
          if (!cogUser.signInUserSession) {
            this.cognitoService.changeLeadPassword(cogUser, 'JanaLead123')
              .then((res) => {
                console.log('then');

                this.successSignIn(res);
              })
          }
          else {
            this.successSignIn(cogUser);
          }
        })
        .catch((error: any) => {
          this.displayAlert(error.message);
        })
    }
  }


  successSignIn(cogUser: any) {
    console.log(cogUser);

    this.cognitoService.setAccessToken(cogUser.signInUserSession.idToken);
    if (cogUser.signInUserSession.idToken.payload['cognito:groups'].includes('EmployeeGroup'))
      this.router.navigate(['/home'])
    else if (cogUser.signInUserSession.idToken.payload['cognito:groups'].includes('EngineeringLeadGroup'))
      this.router.navigate(['/lead'])
    else
      this.router.navigate(['/home'])
  }

  private displayAlert(message: string) {
    this.snackBar.open(message, "OK", {
      duration: 3000
    });
  }

  forgotPasswordClicked() {
    if (this.user && this.user.email) {
      this.cognitoService.forgotPassword(this.user)
        .then(() => {
          this.isForgotPassword = true;
        })
        .catch((error: any) => {
          this.displayAlert(error.message);
        })
    }
    else {
      this.displayAlert("Please Enter a valid email address");
    }
  }

  newPasswordSubmit() {
    if (this.user && this.user.code && this.newPassword.trim().length != 0) {
      this.cognitoService.forgotPasswordSubmit(this.user, this.newPassword.trim())
        .then(() => {
          this.displayAlert("Password Updated");
          this.isForgotPassword = false;
        })
        .catch((error: any) => {
          this.displayAlert(error.message);
        })
    }
    else {
      this.displayAlert("Please enter valid input");
    }
  }

}