import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { LandLookupService } from './shared-util-data-access-land-lookup.service';

describe('LandLookupService', () => {
  let service: LandLookupService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient()],
    });
    service = TestBed.inject(LandLookupService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
