import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PillReminderListItemComponent } from './pill-reminder-list-item.component';

describe('PillReminderListItemComponent', () => {
  let component: PillReminderListItemComponent;
  let fixture: ComponentFixture<PillReminderListItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PillReminderListItemComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PillReminderListItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
