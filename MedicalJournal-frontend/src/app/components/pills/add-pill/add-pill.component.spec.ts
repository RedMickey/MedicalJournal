import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddPillComponent } from './add-pill.component';

describe('AddPillComponent', () => {
  let component: AddPillComponent;
  let fixture: ComponentFixture<AddPillComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddPillComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddPillComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
