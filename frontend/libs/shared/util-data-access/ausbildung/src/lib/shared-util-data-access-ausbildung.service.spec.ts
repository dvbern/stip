import { TestBed } from '@angular/core/testing';

import { SharedUtilDataAccessAusbildungService } from './ausbildung.service';

describe('AusbildungService', () => {
  let service: AusbildungService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AusbildungService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
