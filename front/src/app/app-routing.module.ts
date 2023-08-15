import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { FullComponent } from './layout/full/full.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { ProjectsComponent } from './dashboard/dashboard-components/projects/projects.component';
import { PeopleComponent } from './pages/people/people.component';
import { TeamsComponent } from './pages/teams/teams.component';
import { NewTeamComponent } from './pages/new-team/new-team.component';
import { NewProjectComponent } from './pages/new-project/new-project.component';
import { NewEmployeeComponent } from './pages/new-employee/new-employee.component';
import { ProjectDetailsComponent } from './pages/project-details/project-details.component';
import { EmployeeDetailsComponent } from './pages/employee-details/employee-details.component';
import { TeamDetailsComponent } from './pages/team-details/team-details.component';

const routes: Routes = [
  {
    path: '',
    component: FullComponent,
    children: [
      { path: '', redirectTo: '/home', pathMatch: 'full' },
      { path: 'home', component: DashboardComponent },
      { path: 'projects', component: ProjectsComponent },
      { path: 'projects/new', component: NewProjectComponent },
      { path: 'projects/edit', component: NewProjectComponent },
      { path: 'projects/:id', component: ProjectDetailsComponent },
      { path: 'people', component: PeopleComponent },
      { path: 'people/new', component: NewEmployeeComponent },
      { path: 'people/edit', component: NewEmployeeComponent },
      { path: 'people/:id', component: EmployeeDetailsComponent },
      { path: 'teams', component: TeamsComponent },
      { path: 'teams/new', component: NewTeamComponent },
      { path: 'teams/edit', component: NewTeamComponent },
      { path: 'teams/:id', component: TeamDetailsComponent },

    ],
  },
  {
    path: 'login',
    component: LoginComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
