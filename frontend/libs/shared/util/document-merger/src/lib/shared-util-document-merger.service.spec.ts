import { TestBed } from '@angular/core/testing';

import { SharedUtilDocumentMergerService } from './shared-util-document-merger.service';

describe('SharedUtilDocumentMergerService', () => {
  let service: SharedUtilDocumentMergerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SharedUtilDocumentMergerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
