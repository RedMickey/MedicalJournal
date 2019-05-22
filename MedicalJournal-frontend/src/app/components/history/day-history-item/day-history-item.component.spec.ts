import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DayHistoryItemComponent } from './day-history-item.component';

describe('DayHistoryItemComponent', () => {
  let component: DayHistoryItemComponent;
  let fixture: ComponentFixture<DayHistoryItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DayHistoryItemComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DayHistoryItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
