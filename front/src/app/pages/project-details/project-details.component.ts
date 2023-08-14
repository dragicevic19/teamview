import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { Employee } from 'src/app/model/Employee';
import { Project } from 'src/app/model/Project';
import { ProjectService } from 'src/app/service/project.service';

@Component({
  selector: 'app-project-details',
  templateUrl: './project-details.component.html',
  styleUrls: ['./project-details.component.scss']
})
export class ProjectDetailsComponent implements OnInit {

  project!: Project;
  members!: Employee[];

  constructor(
    private router: Router,
    private projectService: ProjectService,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.project = JSON.parse(localStorage.getItem('project')!);
    localStorage.removeItem('project');
    if (!this.project) this.router.navigate(['/projects']);

    if (this.project.team && this.project.team.members) {
      this.members = this.project.team.members;
    }
    else this.members = [];
  }

  edit() {
    localStorage.setItem('editProject', JSON.stringify(this.project));
    this.router.navigate(['/projects/edit']);
  }

  delete() {
    this.projectService.deleteProject(this.project).subscribe({
      next: () => {
        this.snackBar.open('Successfully removed project: ' + this.project.title, 'OK', {
          duration: 2000,
        });
      },
      error: (err) => console.log(err)
    });
    window.location.href = window.location.href;

  }
}
