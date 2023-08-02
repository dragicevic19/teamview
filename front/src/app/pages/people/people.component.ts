import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';

export interface PeriodicElement {
  name: string;
  email: string;
  project: string;
  team: string;
  badge: string;
  position: string;
  seniority: string;
}

const ELEMENT_DATA: PeriodicElement[] = [
  { name: 'Deep Javiya', email: 'deepjaviya@teamup.com', project: 'Flexy Angular', team: 'Flexies', position: 'Backend Developer', seniority: 'Medior', badge: 'badge-info' },
  { name: 'Nirav Joshi', email: 'niravjoshi@teamup.com', project: 'Elite Admin', team: 'EliteTeam', position: 'Frontend Developer', seniority: 'Senior', badge: 'badge-danger' },
  { name: 'Sunil Joshi', email: 'sunil@teamup.com', project: 'Elite Admin', team: 'EliteTeam', position: 'Frontend Developer', seniority: 'Senior', badge: 'badge-danger' },
  { name: 'Maruti Makwana', email: 'marutimakwana@teamup.com', project: 'Elite Admin', team: 'EliteTeam', position: 'Backend Developer', seniority: 'Junior', badge: 'badge-success' },
];


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
  dataSource = ELEMENT_DATA;
  employees = ['test'];
  page = 0;
  rows = 4;

  constructor() { }

  ngOnInit(): void {
  }

  handlePageEvent(e: PageEvent) {
    this.page = e.pageIndex;
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
