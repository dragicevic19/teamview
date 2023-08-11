import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PaginationResponse } from '../model/PaginationResponse';
import NewProject from '../pages/new-project/new-project.component';
import { Project } from '../model/Project';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {


  private baseUrl = 'http://localhost:8080/projects';

  constructor(private http: HttpClient) { }

  fetchProjects(rows: number, page: number) {
    return this.http.get<PaginationResponse>(`${this.baseUrl}/${page}/${rows}`);
  }

  newProject(newProject: NewProject) {
    return this.http.post<Project>(this.baseUrl, newProject);
  }

  editProject(newProject: NewProject, id: any) {
    return this.http.put<Project>(`${this.baseUrl}/${id}`, newProject); 
  }
}
