import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReminderEntryComponent } from './reminder-entry.component';

describe('PillReminderEntryComponent', () => {
  let component: ReminderEntryComponent;
  let fixture: ComponentFixture<ReminderEntryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReminderEntryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReminderEntryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
