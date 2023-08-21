import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PaginationResponse } from '../model/PaginationResponse';
import NewTeam from '../pages/new-team/new-team.component';
import { Team } from '../model/Team';
import { environment } from 'src/environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class TeamService {

  private localUrl = 'http://localhost:8080/teams';
  private awsUrl = environment.apiPath + 'teams';

  private baseUrl = (environment.localhost) ? this.localUrl : this.awsUrl;

  constructor(private http: HttpClient) { }

  fetchTeams(rows: number, page: number) {
    return this.http.get<Team[]>(`${this.baseUrl}`);
  }

  newTeam(newTeam: NewTeam) {
    return this.http.post<void>(this.baseUrl, newTeam);
  }

  editTeam(newTeam: NewTeam, id: number) {
    return this.http.put<void>(`${this.baseUrl}/${id}`, newTeam);
  }

  deleteTeam(team: Team) {
    return this.http.delete<void>(`${this.baseUrl}/${team.id}`);
  }

}
