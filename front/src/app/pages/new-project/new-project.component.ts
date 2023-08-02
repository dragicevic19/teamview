import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

export default interface Project {
  title: string;
  client: string;
  team: any;
  description: string;
}

@Component({
  selector: 'app-new-project',
  templateUrl: './new-project.component.html',
  styleUrls: ['./new-project.component.scss']
})
export class NewProjectComponent implements OnInit {

  newProject: Project = {
    title: '',
    client: '',
    team: {},
    description: '',
  };

  ngOnInit(): void {
  }

  isTeamSelected() {
    return Object.keys(this.newProject.team).length !== 0
  }

  removeTeam() {
    this.newProject.team = {};
  }

  constructor(public snackBar: MatSnackBar) { }

  teamSelected(team: any) {
    if (this.newProject.team === team) {
      this.snackBar.open(team.team + ' is already selected', 'OK', {
        duration: 1000
      });
      return;
    }

    this.newProject.team = team;
    this.snackBar.open('Successfully selected team: ' + team.team, 'OK', {
      duration: 2000
    });
  }

  submit() {
    this.snackBar.open(
      'Successfully added new project: ' + this.newProject.title + '!', 'OK', {
      duration: 2000
    }
    )
  }

}
