import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { Project } from 'src/app/model/Project';
import { ProjectService } from 'src/app/service/project.service';

export default interface NewProject {
  id?: string;
  title: string;
  client: string;
  team: any;
  description: string;
  startDate?: any;
  endDate?: any;
  status: string;
  prevTeamId?: string;
}

@Component({
  selector: 'app-new-project',
  templateUrl: './new-project.component.html',
  styleUrls: ['./new-project.component.scss'],

})
export class NewProjectComponent implements OnInit {

  editProject: Project = {} as Project;
  newProject: NewProject = {
    title: '',
    client: '',
    team: {},
    description: '',
    status: '',
  };

  disableSubmitButton = false;

  projectStatusOptions = [
    { show: 'On Hold', value: 'ON_HOLD' },
    { show: 'In Progress', value: 'IN_PROGRESS' },
    { show: 'Completed', value: 'COMPLETED' },
  ]

  constructor(
    public snackBar: MatSnackBar,
    private projectService: ProjectService,
    private router: Router) { }

  ngOnInit(): void {
    this.disableSubmitButton = false;
    this.editProject = JSON.parse(localStorage.getItem('editProject')!);
    localStorage.removeItem('editProject');

    if (this.editProject) {
      this.newProject.id = this.editProject.id;
      this.newProject.title = this.editProject.title;
      this.newProject.client = this.editProject.client;
      this.newProject.description = this.editProject.description;
      this.newProject.startDate = this.editProject.startDate;
      this.newProject.endDate = this.editProject.endDate;
      this.newProject.team = this.editProject.team;
      this.newProject.status = this.editProject.status;
      this.newProject.prevTeamId = this.editProject.team?.id;
    }
  }

  isTeamSelected() {
    return this.newProject.team && Object.keys(this.newProject.team).length
  }

  removeTeam() {
    this.newProject.team = {};
    this.snackBar.open('Team is removed!', 'OK', {
      duration: 1000
    });
  }


  teamSelected(team: any) {
    if (this.newProject.team?.id === team.id) {
      this.snackBar.open(team.name + ' is already selected', 'OK', {
        duration: 1000
      });
      return;
    }

    this.newProject.team = team;
    this.snackBar.open('Successfully selected team: ' + team.name, 'OK', {
      duration: 2000
    });
  }

  submit() {
    if (this.validate()) {
      this.snackBar.open('All fields are required!', 'OK', {
        duration: 3000,
      });
      return;
    }
    this.sendNewProject();

    // if (this.editProject && Object.keys(this.editProject).length) {
    //   this.sendEdit();
    // }
    // else {
    //   this.sendNewProject();
    // }
  }

  sendNewProject() {
    this.disableSubmitButton = true;

    this.projectService.newProject(this.newProject).subscribe({
      next: () => {
        const message = (this.editProject && Object.keys(this.editProject).length) ? 
        "edited" : "added new";

        this.snackBar.open(
          'Successfully ' + message + ' project: ' + this.newProject.title + '!', 'OK', {
          duration: 2000
        });
        this.router.navigate(['/projects']);
      },
      error: (err) => console.log(err)
    });
  }

  // sendEdit() {
  //   this.projectService.editProject(this.newProject, this.editProject.id).subscribe({
  //     next: () => {
  //       this.snackBar.open(
  //         'Successfully edited project: ' + this.newProject.title + '!', 'OK', {
  //         duration: 2000
  //       });
  //       this.router.navigate(['/projects']);
  //     },
  //     error: (err) => console.log(err)
  //   });
  // }

  validate() {
    return !this.newProject.title || !this.newProject.client || !this.newProject.description
      || !this.newProject.startDate || !this.newProject.endDate || !this.newProject.status;
  }


}
