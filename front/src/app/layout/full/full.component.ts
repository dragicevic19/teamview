import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CognitoService } from 'src/app/service/cognito.service';
import jwt_decode from 'jwt-decode';

interface SidebarMenu {
  link: string;
  icon: string;
  menu: string;
}

interface LoggedInUser {
  sub: string;
  given_name: string;
}

@Component({
  selector: 'app-full',
  templateUrl: './full.component.html',
  styleUrls: ['./full.component.scss']
})
export class FullComponent implements OnInit {

  search: boolean = false;
  routerActive: string = "activelink";
  user: LoggedInUser = {} as LoggedInUser;

  constructor (private router: Router, private cognitoService: CognitoService) {}

  sidebarMenu: SidebarMenu[] = [
    {
      link: "/home",
      icon: "home",
      menu: "Dashboard",
    },
    {
      link: "/projects",
      icon: "git-pull-request",
      menu: "Projects",
    },
    {
      link: "/people",
      icon: "users",
      menu: "People",
    },
    {
      link: "/teams",
      icon: "code",
      menu: "Teams",
    },
  ]

  ngOnInit(): void {

    const jwt = this.cognitoService.getToken();
    if (!jwt) this.router.navigate(['/login']);

    this.user = jwt_decode(jwt!);
  }
}
