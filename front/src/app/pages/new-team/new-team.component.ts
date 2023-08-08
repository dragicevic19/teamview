import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { Employee } from 'src/app/model/Employee';
import { Team } from 'src/app/model/Team';
import { TeamService } from 'src/app/service/team.service';

export default interface NewTeam {
  name: string;
  members: Employee[];
  lead: any;
}

@Component({
  selector: 'app-new-team',
  templateUrl: './new-team.component.html',
  styleUrls: ['./new-team.component.scss']
})
export class NewTeamComponent implements OnInit {

  newTeam: NewTeam = {
    name: '',
    members: [],
    lead: {}
  };

  leadSelected() {
    return Object.keys(this.newTeam.lead).length !== 0
  }

  constructor(
    public snackBar: MatSnackBar,
    private teamService: TeamService,
    private router: Router) { }


  ngOnInit(): void {

  }

  teamLeadSelected(lead: any) {
    if (this.newTeam.lead === lead) {
      this.snackBar.open(lead.name + ' is already selected as Team Lead', 'OK', {
        duration: 1000
      });
      return;
    }

    if (!this.newTeam.members.includes(lead)) {
      this.newTeam.members.push(lead);
    }
    this.newTeam.lead = lead;
    this.snackBar.open('Successfully selected ' + lead.name + ' as Team Lead!', 'OK', {
      duration: 2000
    });
  }

  employeeAdded(emp: any) {
    if (this.newTeam.members.includes(emp)) {
      this.snackBar.open(emp.name + ' is already in team', 'OK', {
        duration: 1000
      });
      return;
    }
    this.newTeam.members.push(emp);
    this.snackBar.open('Successfully added ' + emp.name + ' to the team!', 'OK', {
      duration: 2000,
    });
  }

  removeLead() {
    this.newTeam.lead = {};
    this.snackBar.open('Team Lead removed!', 'OK', {
      duration: 1000,
    });
  }

  removeMember(member: Employee) {
    this.newTeam.members = this.newTeam.members.filter((m) => m.id !== member.id);
    if (this.newTeam.lead.id === member.id) this.newTeam.lead = {};
  } 

  submit() {
    if (!this.newTeam.name || !this.leadSelected()) {
      this.snackBar.open('Name of the team and team leader are required!', 'OK', {
        duration: 3000,
      });
      return;
    }

    this.teamService.newTeam(this.newTeam).subscribe({
      next: (team: Team) => {
        this.snackBar.open(
          'Successfully added new team: ' + team.name + '!', 'OK', {
            duration: 2000
          }
        );
        this.router.navigate(['/teams']);
      },
      error: (err) => console.log(err)
    });
  }

}
