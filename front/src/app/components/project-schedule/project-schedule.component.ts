import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-project-schedule',
  templateUrl: './project-schedule.component.html',
  styleUrls: ['./project-schedule.component.scss']
})
export class ProjectScheduleComponent implements OnInit {
  @Input() startDate!: Date;
  @Input() endDate!: Date;

  schedule: any[] = [];

  ngOnInit(): void {
    this.schedule = [
      {
        time: this.startDate,
        ringColor: "ring-success",
        message: "Start of the project",
      },
      {
        time: this.endDate,
        ringColor: "ring-danger",
        message: "To finish first version",
      },
    ];
  }
}
