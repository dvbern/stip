import { TestBed } from '@angular/core/testing';

import { SharedUtilHeaderService } from './shared-util-header.service';

describe('SharedUtilHeaderService', () => {
  let service: SharedUtilHeaderService;

  beforeEach(() => {
    TestBed.configureTestingModule({ providers: [SharedUtilHeaderService] });
    service = TestBed.inject(SharedUtilHeaderService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
