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

const routes: Routes = [
  {
    path: '',
    component: FullComponent,
    children: [
      { path: '', redirectTo: '/home', pathMatch: 'full' },
      { path: 'home', component: DashboardComponent },
      { path: 'projects', component: ProjectsComponent },
      { path: 'projects/new', component: NewProjectComponent },
      { path: 'people', component: PeopleComponent },
      { path: 'people/new', component: NewEmployeeComponent },
      { path: 'teams', component: TeamsComponent },
      { path: 'teams/new', component: NewTeamComponent },
      { path: 'teams/edit', component: NewTeamComponent },
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
