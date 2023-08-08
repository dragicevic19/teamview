import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { Employee } from 'src/app/model/Employee';
import { UserService } from 'src/app/service/user.service';

export default interface NewEmployee {
  name: string;
  lastName: string;
  position: string;
  seniority: string;
  team: any;
  email: string;
  address: string;
}

@Component({
  selector: 'app-new-employee',
  templateUrl: './new-employee.component.html',
  styleUrls: ['./new-employee.component.scss']
})
export class NewEmployeeComponent implements OnInit {

  seniorityOptions = [
    { value: 'JUNIOR', show: 'Junior' },
    { value: 'MEDIOR', show: 'Medior' },
    { value: 'SENIOR', show: 'Senior' }
  ];

  newEmployee: NewEmployee = {
    name: '',
    lastName: '',
    position: '',
    seniority: '',
    team: {},
    email: '',
    address: '',
  };

  ngOnInit(): void {
  }

  constructor(
    public snackBar: MatSnackBar,
    private userService: UserService,
    private router: Router
  ) { }


  isTeamSelected() {
    return Object.keys(this.newEmployee.team).length !== 0
  }

  removeTeam() {
    this.newEmployee.team = {};
    this.snackBar.open('Team is removed!', 'OK', {
      duration: 1000
    });
  }

  teamSelected(team: any) {
    if (this.newEmployee.team === team) {
      this.snackBar.open(team.name + ' is already selected', 'OK', {
        duration: 1000
      });
      return;
    }

    this.newEmployee.team = team;
    this.snackBar.open('Successfully selected team: ' + team.name, 'OK', {
      duration: 2000
    });
  }

  submit() {
    // TODO : validation

    this.userService.newEmployee(this.newEmployee).subscribe({
      next: (res: Employee) => {
        this.snackBar.open(
          'Successfully added new employee: ' + this.newEmployee.name + ' ' + this.newEmployee.lastName + '!', 'OK', {
          duration: 2000
        });
        this.router.navigate(['/people'])
      },
      error: (err) => console.log(err)
    });
  }

}
