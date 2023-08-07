import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PaginationResponse } from '../model/PaginationResponse';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private baseUrl = 'http://localhost:8080/employees/';

  constructor(private http: HttpClient) { }

  fetchEmployees(rows: number, page: number) {
    return this.http.get<PaginationResponse>(`${this.baseUrl}${page}/${rows}`);
  }
}
