import { Component, Input, OnInit } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
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
  @Input() allProjects?: Project[];

  displayedColumns: string[] = ['project', 'client', 'team', 'name', 'status', 'startDate', 'endDate', 'action'];
  projects: Project[] = [];

  page = 0;
  rows = 5;
  totalItems = 0;
  loading = true;

  constructor(
    private projectService: ProjectService,
    private router: Router,
    private snackBar: MatSnackBar) { }

  ngOnInit(): void {
    if (this.allProjects !== null && this.allProjects !== undefined) {
      this.projects = this.allProjects;
      this.loading = false;
    }
    else this.fetchProjects();

  }

  fetchProjects() {
    this.loading = true;

    this.projectService.fetchProjects(this.rows, this.page).subscribe({
      next: (res) => {
        // this.totalItems = res.totalItems;
        this.projects = res;
        this.loading = false;
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

  edit(project: Project) {
    localStorage.setItem('editProject', JSON.stringify(project));
    this.router.navigate(['/projects/edit']);
  }

  delete(project: Project) {
    this.projectService.deleteProject(project).subscribe({
      next: () => {
        this.snackBar.open('Successfully removed project: ' + project.title, 'OK', {
          duration: 2000,
        });
      },
      error: (err) => console.log(err)
    });
    window.location.href = window.location.href;
  }


  details(project: Project) {
    localStorage.setItem('project', JSON.stringify(project));
    this.router.navigate(['/projects/' + project.id]);
  }

}
