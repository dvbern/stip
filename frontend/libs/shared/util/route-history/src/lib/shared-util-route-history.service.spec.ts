import { TestBed } from '@angular/core/testing';

import { SharedUtilRouteHistoryService } from './shared-util-route-history.service';

describe('SharedUtilRouteHistoryService', () => {
  let service: SharedUtilRouteHistoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SharedUtilRouteHistoryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
