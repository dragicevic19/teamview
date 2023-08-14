import { Component,  OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Employee } from 'src/app/model/Employee';
import { Project } from 'src/app/model/Project';

@Component({
  selector: 'app-project-details',
  templateUrl: './project-details.component.html',
  styleUrls: ['./project-details.component.scss']
})
export class ProjectDetailsComponent implements OnInit{
  
  project!: Project;
  members!: Employee[];

  constructor(
    private router: Router
  ) {}

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
}
