import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployersProjectsComponent } from './employers-projects.component';

describe('EmployersProjectsComponent', () => {
  let component: EmployersProjectsComponent;
  let fixture: ComponentFixture<EmployersProjectsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EmployersProjectsComponent]
    });
    fixture = TestBed.createComponent(EmployersProjectsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
