import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PaginationResponse } from '../model/PaginationResponse';
import NewEmployee from '../pages/new-employee/new-employee.component';
import { Employee } from '../model/Employee';
import { environment } from 'src/environments/environment.development';
import { Project } from '../model/Project';

@Injectable({
  providedIn: 'root'
})
export class UserService {


  private localUrl = 'http://localhost:8080/employees';
  private awsUrl = environment.apiPath + 'employees';

  private baseUrl = (environment.localhost) ? this.localUrl : this.awsUrl;

  constructor(private http: HttpClient) { }

  fetchEmployees(rows: number, page: number) {
    return this.http.get<Employee[]>(`${this.baseUrl}`);
  }

  newEmployee(newEmployee: NewEmployee) {
    return this.http.post<void>(this.baseUrl, newEmployee);
  }

  editEmployee(newEmployee: NewEmployee, id: number) {
    return this.http.put<void>(`${this.baseUrl}/${id}`, newEmployee);
  }

  deleteEmployee(employee: Employee) {
    return this.http.delete<void>(`${this.baseUrl}/${employee.id}`);
  }

  fetchEmployeesProjects(employee: Employee) {
    return this.http.get<Project[]>(`${environment.apiPath + 'user-projects?id='}${employee.id}`);
  }
}
