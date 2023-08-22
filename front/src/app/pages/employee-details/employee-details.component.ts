import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { Employee } from 'src/app/model/Employee';
import { Project } from 'src/app/model/Project';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-employee-details',
  templateUrl: './employee-details.component.html',
  styleUrls: ['./employee-details.component.scss']
})
export class EmployeeDetailsComponent implements OnInit {

  employee!: Employee;
  projects!: Project[];
  loading = true;

  constructor(
    private router: Router,
    private userService: UserService,
    private snackBar: MatSnackBar
  ) { }


  ngOnInit(): void {
    this.employee = JSON.parse(localStorage.getItem('employee')!);
    localStorage.removeItem('employee');
    if (!this.employee) {
      this.router.navigate(['/people']);
    }
    else {
      this.projects = [];
      this.fetchAllEmployeesProjects();
      // this.projects = (this.employee.allProjects) ? this.employee.allProjects : [];
    }
  }
  fetchAllEmployeesProjects() {
    this.loading = true;

    this.userService.fetchEmployeesProjects(this.employee).subscribe({
      next: (res) => {
        this.projects = res;
        this.loading = false;
      },
      error: (err) => console.log(err)
    })
  }

  edit() {
    localStorage.setItem('editEmployee', JSON.stringify(this.employee));
    this.router.navigate(['/people/edit']);
  }

  delete() {
    this.userService.deleteEmployee(this.employee).subscribe({
      next: () => {
        this.snackBar.open('Successfully removed employee: ' + this.employee.name, 'OK', {
          duration: 2000,
        });
        this.router.navigate(['/people']);
      },
      error: (err) => console.log(err)
    });
  }

}
