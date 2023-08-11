import { Component, Input, OnInit } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { Router } from '@angular/router';
import { Project } from 'src/app/model/Project';
import { ProjectService } from 'src/app/service/project.service';

@Component({
  selector: 'app-projects',
  templateUrl: './projects.component.html',
  styleUrls: ['./projects.component.scss']
})
export class ProjectsComponent implements OnInit {
  @Input() dashboard: boolean = false;

  displayedColumns: string[] = ['project', 'client', 'team', 'name', 'status', 'startDate', 'endDate', 'action'];
  projects: Project[] = [];

  page = 0;
  rows = 5;
  totalItems = 0;

  constructor(private projectService: ProjectService, private router: Router) { }

  ngOnInit(): void {
    this.fetchProjects();
  }

  fetchProjects() {
    this.projectService.fetchProjects(this.rows, this.page).subscribe({
      next: (res) => {
        this.totalItems = res.totalItems;
        this.projects = res.data;
      },
      error: (err) => {
        console.log(err);
      }
    })
  }

  handlePageEvent(e: PageEvent) {
    this.page = e.pageIndex;
    this.fetchProjects();
  }

  edit(project: any) {
    localStorage.setItem('editProject', JSON.stringify(project));
    this.router.navigate(['/projects/edit']);
  }

  delete(project: any) {
    throw new Error('Method not implemented.');
  }

}
