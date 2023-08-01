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
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    TeamViewModule,
    BrowserAnimationsModule,
    FeatherModule.pick(allIcons),
    NgApexchartsModule

  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
