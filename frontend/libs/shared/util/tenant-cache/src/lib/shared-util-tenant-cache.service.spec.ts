import { TestBed } from '@angular/core/testing';

import { SharedUtilTenantCacheService } from './shared-util-tenant-cache.service';

describe('SharedUtilTenantCacheService', () => {
  let service: SharedUtilTenantCacheService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SharedUtilTenantCacheService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
