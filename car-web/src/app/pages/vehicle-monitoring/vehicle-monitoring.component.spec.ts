import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VehicleMonitoringComponent } from './vehicle-monitoring.component';

describe('VehicleMonitoringComponent', () => {
  let component: VehicleMonitoringComponent;
  let fixture: ComponentFixture<VehicleMonitoringComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VehicleMonitoringComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VehicleMonitoringComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
