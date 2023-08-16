import { Component, Input, OnInit } from '@angular/core';
import { Employee } from 'src/app/model/Employee';

@Component({
  selector: 'app-employees-on-project',
  templateUrl: './employees-on-project.component.html',
  styleUrls: ['./employees-on-project.component.scss']
})
export class EmployeesOnProjectComponent implements OnInit{
  @Input() employees!: Employee[];

  displayedColumns: string[] = ['name', 'position', 'seniority'];

  ngOnInit(): void {
  }
 
}
