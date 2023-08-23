import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './auth/login/login.component';
import { SignupComponent } from './auth/signup/signup.component';
import { FullComponent } from './layout/full/full.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { SalesComponent } from './dashboard/dashboard-components/sales/sales.component';
import { ProjectsComponent } from './dashboard/dashboard-components/projects/projects.component';
import { CardsComponent } from './dashboard/dashboard-components/cards/cards.component';
import { TeamViewModule } from './teamview-module';
import { FeatherModule } from 'angular-feather';
import { allIcons } from 'angular-feather/icons';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgApexchartsModule } from 'ng-apexcharts';
import { PeopleComponent } from './pages/people/people.component';
import { TeamsComponent } from './pages/teams/teams.component';
import { NewTeamComponent } from './pages/new-team/new-team.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NewProjectComponent } from './pages/new-project/new-project.component';
import { NewEmployeeComponent } from './pages/new-employee/new-employee.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { SeniorityToBadgePipe } from './pipes/seniority-to-badge.pipe';
import { ProjectStatusToBadgePipe } from './pipes/project-status-to-badge.pipe';
import { ProjectStatusToStringPipe } from './pipes/project-status-to-string.pipe';
import { ProjectDetailsComponent } from './pages/project-details/project-details.component';
import { EmployeesOnProjectComponent } from './components/employees-on-project/employees-on-project.component';
import { ProjectScheduleComponent } from './components/project-schedule/project-schedule.component';
import { EmployeeDetailsComponent } from './pages/employee-details/employee-details.component';
import { EmployersProjectsComponent } from './components/employers-projects/employers-projects.component';
import { TeamDetailsComponent } from './pages/team-details/team-details.component';
import { AuthInterceptor } from './interceptors/auth.interceptor';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    SignupComponent,
    FullComponent,
    DashboardComponent,
    SalesComponent,
    ProjectsComponent,
    CardsComponent,
    PeopleComponent,
    TeamsComponent,
    NewTeamComponent,
    NewProjectComponent,
    NewEmployeeComponent,
    SeniorityToBadgePipe,
    ProjectStatusToBadgePipe,
    ProjectStatusToStringPipe,
    ProjectDetailsComponent,
    EmployeesOnProjectComponent,
    ProjectScheduleComponent,
    EmployeeDetailsComponent,
    EmployersProjectsComponent,
    TeamDetailsComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    TeamViewModule,
    BrowserAnimationsModule,
    FeatherModule.pick(allIcons),
    NgApexchartsModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,

  ],
  providers: [ { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }],
  bootstrap: [AppComponent]
})
export class AppModule { }
