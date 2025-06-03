import { TestBed } from '@angular/core/testing';

import { SharedUtilDataAccessLandLookupService } from './land-lookup.service';

describe('LandLookupService', () => {
  let service: LandLookupService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LandLookupService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
