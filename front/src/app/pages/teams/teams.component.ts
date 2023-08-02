import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';

export interface PeriodicElement {
  name: string;
  email: string;
  project: string;
  team: string;
  members: number;
  status: string;
  badge: string;
  startDate: Date;
  endDate: Date;
}

const ELEMENT_DATA: PeriodicElement[] = [
  { name: 'Deep Javiya', email: 'deepjaviya@teamup.com', project: 'Flexy Angular', status: 'On Hold', badge: 'badge-info', team: 'Felxies', members: 10, startDate: new Date(), endDate: new Date() },
  { name: 'Nirav Joshi', email: 'niravjoshi@teamup.com', project: 'Hosting Press HTML', status: 'Completed', badge: 'badge-success', team: 'HostingsPro', members: 4, startDate: new Date(), endDate: new Date() },
  { name: 'Sunil Joshi', email: 'sunil@teamup.com', project: 'Elite Admin', status: 'In Progress', badge: 'badge-primary', team: 'EliteTeam', members: 21, startDate: new Date(), endDate: new Date() },
  { name: 'Maruti Makwana', email: 'marutimakwana@teamup.com', project: 'Material Pro', status: 'In Progress', badge: 'badge-primary', team: 'Materials', members: 11, startDate: new Date(), endDate: new Date() },
];

@Component({
  selector: 'app-teams',
  templateUrl: './teams.component.html',
  styleUrls: ['./teams.component.scss']
})
export class TeamsComponent implements OnInit {
  @Input() selection: boolean = false;
  @Output() teamSelected = new EventEmitter<any>();

  displayedColumns: string[] = ['name', 'team', 'members', 'project', 'status', 'startDate', 'endDate'];
  columnsWithActions: string[] = ['name', 'team', 'members', 'project', 'status', 'startDate', 'endDate', 'actions'];

  dataSource = ELEMENT_DATA;
  teams = ['test'];

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

  teamSelection(row: any) {
    this.teamSelected.emit(row);
  }

}
