import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PaginationResponse } from '../model/PaginationResponse';

@Injectable({
  providedIn: 'root'
})
export class TeamService {

  private baseUrl = 'http://localhost:8080/teams/';

  constructor(private http: HttpClient) { }

  fetchTeams(rows: number, page: number) {
    return this.http.get<PaginationResponse>(`${this.baseUrl}${page}/${rows}`);
  }
}
