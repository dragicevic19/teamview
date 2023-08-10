import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PaginationResponse } from '../model/PaginationResponse';
import NewTeam from '../pages/new-team/new-team.component';
import { Team } from '../model/Team';

@Injectable({
  providedIn: 'root'
})
export class TeamService {


  private baseUrl = 'http://localhost:8080/teams';

  constructor(private http: HttpClient) { }

  fetchTeams(rows: number, page: number) {
    return this.http.get<PaginationResponse>(`${this.baseUrl}/${page}/${rows}`);
  }

  newTeam(newTeam: NewTeam) {
    return this.http.post<Team>(this.baseUrl, newTeam); 
  }

  editTeam(newTeam: NewTeam, id: number) {
    return this.http.put<Team>(`${this.baseUrl}/${id}`, newTeam); 
  }
}
