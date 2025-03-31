import { TestBed } from '@angular/core/testing';

import { SharedUtilTenantConfigService } from './shared-util-tenant-config.service';

describe('SharedUtilTenantConfigService', () => {
  let service: SharedUtilTenantConfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SharedUtilTenantConfigService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
