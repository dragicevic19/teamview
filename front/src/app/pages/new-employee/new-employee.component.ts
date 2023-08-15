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

  editEmployee: any = {};

  newEmployee: NewEmployee = {
    name: '',
    lastName: '',
    position: '',
    seniority: '',
    team: {},
    email: '',
    address: '',
  };

  constructor(
    public snackBar: MatSnackBar,
    private userService: UserService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.editEmployee = JSON.parse(localStorage.getItem('editEmployee')!);
    localStorage.removeItem('editEmployee');

    if (this.editEmployee) {
      this.newEmployee.name = this.editEmployee.name.split(' ')[0];
      this.newEmployee.lastName = this.editEmployee.name.split(' ')[1];
      this.newEmployee.position = this.editEmployee.position;
      this.newEmployee.seniority = this.editEmployee.seniority.toUpperCase();
      this.newEmployee.team = this.editEmployee.team;
      this.newEmployee.email = this.editEmployee.email;
      this.newEmployee.address = this.editEmployee.address;
    }
    console.log(this.editEmployee);
    
  }

  isTeamSelected() {
    return this.newEmployee.team && Object.keys(this.newEmployee.team).length;
  }

  removeTeam() {
    this.newEmployee.team = {};
    this.snackBar.open('Team is removed!', 'OK', {
      duration: 1000
    });
  }

  teamSelected(team: any) {
    if (this.newEmployee.team?.id === team.id) {
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
    if (this.validate()) {
      this.snackBar.open('All fields are required!', 'OK', {
        duration: 3000,
      });
      return;
    }

    if (this.editEmployee && Object.keys(this.editEmployee).length)
      this.sendEdit();
    else
      this.sendNewEmployee();
  }

  sendEdit() {
    this.userService.editEmployee(this.newEmployee, this.editEmployee.id).subscribe({
      next: (res: Employee) => {
        this.snackBar.open(
          'Successfully edited employee: ' + this.newEmployee.name + ' ' + this.newEmployee.lastName + '!', 'OK', {
          duration: 2000
        });
        this.router.navigate(['/people'])
      },
      error: (err) => console.log(err)
    });
  }

  sendNewEmployee() {
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

  validate() {
    return !this.newEmployee.name || !this.newEmployee.lastName || !this.newEmployee.address || !this.newEmployee.email
      || !this.newEmployee.position || !this.newEmployee.seniority;
  }

}
