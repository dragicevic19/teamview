import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import NewProject from '../pages/new-project/new-project.component';
import { Project } from '../model/Project';
import { environment } from 'src/environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  private localUrl = 'http://localhost:8080/projects';
  private awsUrl = environment.apiPath + 'projects';

  private baseUrl = (environment.localhost) ? this.localUrl : this.awsUrl;

  constructor(private http: HttpClient) { }

  fetchProjects(rows: number, page: number) {
    return this.http.get<Project[]>(`${this.baseUrl}`);
  }

  newProject(newProject: NewProject) {
    return this.http.post<void>(this.baseUrl, newProject);
  }

  editProject(newProject: NewProject, id: any) {
    return this.http.put<void>(`${this.baseUrl}/${id}`, newProject);
  }

  deleteProject(project: any) {
    return this.http.delete<void>(`${this.baseUrl}/${project.id}`);
  }
}
