import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddOneTimeMeasurementComponent } from './add-one-time-measurement.component';

describe('AddOneTimeMeasurementComponent', () => {
  let component: AddOneTimeMeasurementComponent;
  let fixture: ComponentFixture<AddOneTimeMeasurementComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddOneTimeMeasurementComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddOneTimeMeasurementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
