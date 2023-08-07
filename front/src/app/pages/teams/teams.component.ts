import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
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

  displayedColumns: string[] = ['name', 'team', 'members', 'project', 'status', 'startDate', 'endDate'];
  columnsWithActions: string[] = ['name', 'team', 'members', 'project', 'status', 'startDate', 'endDate', 'actions'];

  teams: Team[] = [];

  totalItems = 0;
  page = 0;
  rows = 4;

  constructor(private teamService: TeamService) { }

  ngOnInit(): void {
    this.fetchTeams();
  }

  fetchTeams() {
    this.teamService.fetchTeams(this.rows, this.page).subscribe({
      next: (res) => {
        this.totalItems = res.totalItems;
        this.teams = res.data;
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

}
