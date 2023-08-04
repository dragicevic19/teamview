import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

export default interface Employee {
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
    {value: 'JUNIOR', show: 'Junior'},
    {value: 'MEDIOR', show: 'Medior'},
    {value: 'SENIOR', show: 'Senior'}
  ];

  newEmployee: Employee = {
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

  constructor(public snackBar: MatSnackBar) { }


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
      this.snackBar.open(team.team + ' is already selected', 'OK', {
        duration: 1000
      });
      return;
    }

    this.newEmployee.team = team;
    this.snackBar.open('Successfully selected team: ' + team.team, 'OK', {
      duration: 2000
    });
  }

  submit() {
    this.snackBar.open(
      'Successfully added new employee: ' + this.newEmployee.name + ' ' + this.newEmployee.lastName + '!', 'OK', {
      duration: 2000
    }
    )
  }

}
