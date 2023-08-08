import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PaginationResponse } from '../model/PaginationResponse';
import NewEmployee from '../pages/new-employee/new-employee.component';
import { Employee } from '../model/Employee';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private baseUrl = 'http://localhost:8080/employees';

  constructor(private http: HttpClient) { }

  fetchEmployees(rows: number, page: number) {
    return this.http.get<PaginationResponse>(`${this.baseUrl}/${page}/${rows}`);
  }

  newEmployee(newEmployee: NewEmployee) {
    return this.http.post<Employee>(this.baseUrl, newEmployee);
  }

}
