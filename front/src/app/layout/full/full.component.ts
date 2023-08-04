import { Component } from '@angular/core';

interface SidebarMenu {
  link: string;
  icon: string;
  menu: string;
}

@Component({
  selector: 'app-full',
  templateUrl: './full.component.html',
  styleUrls: ['./full.component.scss']
})
export class FullComponent {
  search: boolean = false;
  routerActive: string = "activelink";

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
}
