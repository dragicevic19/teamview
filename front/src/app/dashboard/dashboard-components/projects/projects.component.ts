import { Component, Input, OnInit } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';


export interface PeriodicElement {
  name: string;
  email: string;
  project: string;
  status: string;
  badge: string;
  startDate: Date;
  endDate: Date;
}

const ELEMENT_DATA: PeriodicElement[] = [
  { name: 'Deep Javiya', email: 'deepjaviya@teamup.com', project: 'Flexy Angular', status: 'On Hold', badge: 'badge-info', startDate: new Date(), endDate: new Date() },
  { name: 'Nirav Joshi', email: 'niravjoshi@teamup.com', project: 'Hosting Press HTML', status: 'Completed', badge: 'badge-success', startDate: new Date(), endDate: new Date() },
  { name: 'Sunil Joshi', email: 'sunil@teamup.com', project: 'Elite Admin', status: 'In Progress', badge: 'badge-primary', startDate: new Date(), endDate: new Date() },
  { name: 'Maruti Makwana', email: 'marutimakwana@teamup.com', project: 'Material Pro', status: 'In Progress', badge: 'badge-primary', startDate: new Date(), endDate: new Date() },
];


@Component({
  selector: 'app-projects',
  templateUrl: './projects.component.html',
  styleUrls: ['./projects.component.scss']
})
export class ProjectsComponent implements OnInit {
  @Input() dashboard: boolean = false;

  displayedColumns: string[] = ['name', 'project', 'status', 'startDate', 'endDate'];
  dataSource = ELEMENT_DATA;
  projects = ['test'];

  page = 0;
  rows = 5;

  constructor() { }

  ngOnInit(): void {
  }

  handlePageEvent(e: PageEvent) {
    this.page = e.pageIndex;
  }

}
