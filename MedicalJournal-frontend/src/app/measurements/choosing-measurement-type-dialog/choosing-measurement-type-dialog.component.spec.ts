import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ChoosingMeasurementTypeDialogComponent } from './choosing-measurement-type-dialog.component';

describe('ChoosingMeasurementTypeDialogComponent', () => {
  let component: ChoosingMeasurementTypeDialogComponent;
  let fixture: ComponentFixture<ChoosingMeasurementTypeDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ChoosingMeasurementTypeDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChoosingMeasurementTypeDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
