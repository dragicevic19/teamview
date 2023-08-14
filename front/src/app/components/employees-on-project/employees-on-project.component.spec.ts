import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeesOnProjectComponent } from './employees-on-project.component';

describe('EmployeesOnProjectComponent', () => {
  let component: EmployeesOnProjectComponent;
  let fixture: ComponentFixture<EmployeesOnProjectComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EmployeesOnProjectComponent]
    });
    fixture = TestBed.createComponent(EmployeesOnProjectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
