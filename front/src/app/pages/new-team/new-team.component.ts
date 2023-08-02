import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

export default interface Team {
  name: string;
  members: any[];
  lead: any;
}

@Component({
  selector: 'app-new-team',
  templateUrl: './new-team.component.html',
  styleUrls: ['./new-team.component.scss']
})
export class NewTeamComponent implements OnInit {

  newTeam: Team = {
    name: '',
    members: [],
    lead: {}
  };

  leadSelected() {
    return Object.keys(this.newTeam.lead).length !== 0
  }

  constructor(public snackBar: MatSnackBar) { }


  ngOnInit(): void {

  }

  teamLeadSelected(lead: any) {
    if (this.newTeam.lead === lead) {
      this.snackBar.open(lead.name + ' is already selected as Team Lead', 'OK', {
        duration: 2000
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
        duration: 2000
      });
      return;
    }
    this.newTeam.members.push(emp);
    this.snackBar.open('Successfully added ' + emp.name + ' to the team!', 'OK', {
      duration: 2000,
    });
  }

  submit() {
    this.snackBar.open(
      'Successfully added new team: ' + this.newTeam.name + '!', 'OK', {
        duration: 2000
      }
    )
  }

}
