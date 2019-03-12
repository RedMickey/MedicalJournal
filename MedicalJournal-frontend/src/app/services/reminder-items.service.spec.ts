import { TestBed } from '@angular/core/testing';

import { ReminderItemsService } from './reminder-items.service';

describe('ReminderItemsService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ReminderItemsService = TestBed.get(ReminderItemsService);
    expect(service).toBeTruthy();
  });
});
