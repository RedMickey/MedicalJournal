import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PillReminderEntryComponent } from './pill-reminder-entry.component';

describe('PillReminderEntryComponent', () => {
  let component: PillReminderEntryComponent;
  let fixture: ComponentFixture<PillReminderEntryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PillReminderEntryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PillReminderEntryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
