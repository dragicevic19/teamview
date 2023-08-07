import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PaginationResponse } from '../model/PaginationResponse';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  private baseUrl = 'http://localhost:8080/projects/';

  constructor(private http: HttpClient) { }

  fetchProjects(rows: number, page: number) {
    return this.http.get<PaginationResponse>(`${this.baseUrl}${page}/${rows}`);
  }
}
