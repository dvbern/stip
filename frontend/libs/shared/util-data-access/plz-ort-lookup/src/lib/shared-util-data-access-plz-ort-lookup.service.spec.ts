import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { PlzOrtLookupService } from './shared-util-data-access-plz-ort-lookup.service';

describe('PlzOrtLookupService', () => {
  let service: PlzOrtLookupService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient()],
    });
    service = TestBed.inject(PlzOrtLookupService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
