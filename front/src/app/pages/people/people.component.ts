import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { Router } from '@angular/router';
import { Employee } from 'src/app/model/Employee';
import { UserService } from 'src/app/service/user.service';


@Component({
  selector: 'app-people',
  templateUrl: './people.component.html',
  styleUrls: ['./people.component.scss']
})
export class PeopleComponent implements OnInit {
  @Input() selection: boolean = false;
  @Output() leadSelected = new EventEmitter<any>();
  @Output() employeeSelected = new EventEmitter<any>();


  displayedColumns: string[] = ['name', 'team', 'project', 'position', 'seniority'];
  columnsWithActions: string[] = ['name', 'team', 'project', 'position', 'seniority', 'actions'];
  employees: Employee[] = [];
  page = 0;
  rows = 4;
  totalItems = 0;
  loading = true;

  constructor(private userService: UserService, private router: Router) { }

  ngOnInit(): void {
    this.fetchEmployees();
  }

  fetchEmployees() {
    this.loading = true;
    this.userService.fetchEmployees(this.rows, this.page).subscribe({
      next: (res: any) => {
        // this.totalItems = res.totalItems;
        this.employees = res;
        this.loading = false;
      },
      error: (err) => {
        console.log(err);
      }
    })
  }

  handlePageEvent(e: PageEvent) {
    this.page = e.pageIndex;
    this.fetchEmployees();
  }

  getDisplayedColumns() {
    return (this.selection) ? this.columnsWithActions : this.displayedColumns;
  }

  leadSelection(row: any) {
    this.leadSelected.emit(row);
  }

  employeeSelection(row: any) {
    this.employeeSelected.emit(row);
  }

  employeeClicked(employee: Employee) {
    if (this.selection) return;

    localStorage.setItem('employee', JSON.stringify(employee));
    this.router.navigate(['/people/' + employee.id]);
  }

}
