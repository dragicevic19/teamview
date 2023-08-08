import { Component, OnInit } from '@angular/core';
import { MatDatepicker } from '@angular/material/datepicker';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { ProjectService } from 'src/app/service/project.service';

export default interface NewProject {
  title: string;
  client: string;
  team: any;
  description: string;
  startDate?: Date;
  endDate?: Date;
}

@Component({
  selector: 'app-new-project',
  templateUrl: './new-project.component.html',
  styleUrls: ['./new-project.component.scss']
})
export class NewProjectComponent implements OnInit {

  newProject: NewProject = {
    title: '',
    client: '',
    team: {},
    description: '',
  };

  constructor(
    public snackBar: MatSnackBar,
    private projectService: ProjectService,
    private router: Router) { }

  ngOnInit(): void {
  }

  isTeamSelected() {
    return Object.keys(this.newProject.team).length !== 0
  }

  removeTeam() {
    this.newProject.team = {};
    this.snackBar.open('Team is removed!', 'OK', {
      duration: 1000
    });
  }


  teamSelected(team: any) {
    if (this.newProject.team === team) {
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
    // TODO validation
    this.projectService.newProject(this.newProject).subscribe({
      next: (res) => {
        this.snackBar.open(
          'Successfully added new project: ' + this.newProject.title + '!', 'OK', {
          duration: 2000
        });
        this.router.navigate(['/projects']);
      },
      error: (err) => console.log(err)
    });
  }

}
