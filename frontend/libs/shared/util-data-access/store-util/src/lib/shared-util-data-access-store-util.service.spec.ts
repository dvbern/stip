import { TestBed } from '@angular/core/testing';
import { provideMockStore } from '@ngrx/store/testing';

import { StoreUtilService } from './shared-util-data-access-store-util.service';

describe('StoreUtilService', () => {
  let service: StoreUtilService;

  beforeEach(() => {
    TestBed.configureTestingModule({ providers: [provideMockStore()] });
    service = TestBed.inject(StoreUtilService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
