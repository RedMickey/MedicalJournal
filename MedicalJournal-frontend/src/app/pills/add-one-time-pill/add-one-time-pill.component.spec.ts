import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddOneTimePillComponent } from './add-one-time-pill.component';

describe('AddOneTimePillComponent', () => {
  let component: AddOneTimePillComponent;
  let fixture: ComponentFixture<AddOneTimePillComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddOneTimePillComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddOneTimePillComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
