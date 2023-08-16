import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { Employee } from 'src/app/model/Employee';
import { Team } from 'src/app/model/Team';
import { TeamService } from 'src/app/service/team.service';

@Component({
  selector: 'app-team-details',
  templateUrl: './team-details.component.html',
  styleUrls: ['./team-details.component.scss']
})
export class TeamDetailsComponent implements OnInit {

  team!: Team;
  members!: Employee[];

  constructor(
    private router: Router,
    private teamService: TeamService,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.team = JSON.parse(localStorage.getItem('team')!);
    localStorage.removeItem('team');
    if (!this.team) this.router.navigate(['/teams']);

    if (this.team.members) {
      this.members = this.team.members;
    }
    else this.members = [];
  }

  edit() {
    localStorage.setItem('editTeam', JSON.stringify(this.team));
    this.router.navigate(['/teams/edit']);
  }


  delete() {
    this.teamService.deleteTeam(this.team).subscribe({
      next: () => {
        this.snackBar.open('Successfully removed team: ' + this.team.name, 'OK', {
          duration: 2000,
        });
        this.router.navigate(['/teams']);
      },
      error: (err) => console.log(err)
    });
  }
}
