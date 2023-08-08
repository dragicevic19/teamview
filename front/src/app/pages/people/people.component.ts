import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { Employee } from 'src/app/model/Employee';
import { PaginationResponse } from 'src/app/model/PaginationResponse';
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

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.fetchEmployees();
  }

  fetchEmployees() {
    this.userService.fetchEmployees(this.rows, this.page).subscribe({
      next: (res: PaginationResponse) => {
        this.totalItems = res.totalItems;
        this.employees = res.data;
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
}
