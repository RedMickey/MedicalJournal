import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MeasurementReminderListItemComponent } from './measurement-reminder-list-item.component';

describe('MeasurementReminderListItemComponent', () => {
  let component: MeasurementReminderListItemComponent;
  let fixture: ComponentFixture<MeasurementReminderListItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MeasurementReminderListItemComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MeasurementReminderListItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
