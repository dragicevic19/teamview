import { Component, OnInit } from '@angular/core';

interface Cards {
  image: string;
  btn: string;
}

@Component({
  selector: 'app-cards',
  templateUrl: './cards.component.html',
  styleUrls: ['./cards.component.scss']
})
export class CardsComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

  cards: Cards[] = [
    {
      image: "assets/images/u2.webp",
      btn: "warn",
    },
    {
      image: "assets/images/u3.webp",
      btn: "primary",
    },
    {
      image: "assets/images/u4.webp",
      btn: "accent",
    },
  ]


}
