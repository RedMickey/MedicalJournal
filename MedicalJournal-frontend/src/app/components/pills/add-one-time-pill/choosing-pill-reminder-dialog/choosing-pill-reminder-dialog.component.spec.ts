import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ChoosingPillReminderDialogComponent } from './choosing-pill-reminder-dialog.component';

describe('ChoosingPillReminderDialogComponent', () => {
  let component: ChoosingPillReminderDialogComponent;
  let fixture: ComponentFixture<ChoosingPillReminderDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ChoosingPillReminderDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChoosingPillReminderDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
