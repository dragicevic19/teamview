import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { Team } from 'src/app/model/Team';
import { TeamService } from 'src/app/service/team.service';

@Component({
  selector: 'app-teams',
  templateUrl: './teams.component.html',
  styleUrls: ['./teams.component.scss']
})
export class TeamsComponent implements OnInit {
  @Input() selection: boolean = false;
  @Output() teamSelected = new EventEmitter<any>();

  displayedColumns: string[] = ['team', 'name', 'members', 'project', 'status', 'startDate', 'endDate', 'action'];
  columnsWithActions: string[] = ['team', 'name', 'members', 'project', 'status', 'startDate', 'endDate', 'actions'];

  teams: Team[] = [];

  totalItems = 0;
  page = 0;
  rows = 4;
  loading = true;

  constructor(
    private teamService: TeamService,
    private router: Router,
    public snackBar: MatSnackBar,
  ) { }

  ngOnInit(): void {
    this.fetchTeams();
  }

  fetchTeams() {
    this.loading = true;
    this.teamService.fetchTeams(this.rows, this.page).subscribe({
      next: (res) => {
        this.totalItems = res.totalItems;
        this.teams = res.data;
        this.loading = false;
      },
      error: (err) => {
        console.log(err);
      }
    })
  }

  handlePageEvent(e: PageEvent) {
    this.page = e.pageIndex;
    this.fetchTeams();
  }

  getDisplayedColumns() {
    return (this.selection) ? this.columnsWithActions : this.displayedColumns;
  }

  teamSelection(row: any) {
    this.teamSelected.emit(row);
  }

  edit(team: any) {
    localStorage.setItem('editTeam', JSON.stringify(team));
    this.router.navigate(['/teams/edit']);
  }

  delete(team: any) {
    this.teamService.deleteTeam(team).subscribe({
      next: () => {
        this.snackBar.open('Successfully removed team: ' + team.name, 'OK', {
          duration: 2000,
        });
      },
      error: (err) => console.log(err)
    });
    window.location.href = window.location.href;

  }

}
