import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ChangingPasswordDialogComponent } from './changing-password-dialog.component';

describe('ChangingPasswordDialogComponent', () => {
  let component: ChangingPasswordDialogComponent;
  let fixture: ComponentFixture<ChangingPasswordDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ChangingPasswordDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChangingPasswordDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
