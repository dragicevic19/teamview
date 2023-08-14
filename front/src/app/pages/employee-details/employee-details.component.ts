import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Employee } from 'src/app/model/Employee';
import { Project } from 'src/app/model/Project';

@Component({
  selector: 'app-employee-details',
  templateUrl: './employee-details.component.html',
  styleUrls: ['./employee-details.component.scss']
})
export class EmployeeDetailsComponent implements OnInit {

  employee!: Employee;
  projects!: Project[];

  constructor(
    private router: Router
  ) {}


  ngOnInit(): void {
    this.employee =  JSON.parse(localStorage.getItem('employee')!);
    localStorage.removeItem('employee');
    if (!this.employee) this.router.navigate(['/people']);

    this.projects = (this.employee.allProjects) ? this.employee.allProjects : [];

    console.log(this.employee);
    
  }



}
