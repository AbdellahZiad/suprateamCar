import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SurveyMonitoringComponent } from './survey-monitoring.component';

describe('SurveyMonitoringComponent', () => {
  let component: SurveyMonitoringComponent;
  let fixture: ComponentFixture<SurveyMonitoringComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SurveyMonitoringComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SurveyMonitoringComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
