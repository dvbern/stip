import { TestBed } from '@angular/core/testing';

import { SachbearbeitungAppUtilKeykloakHttpService } from './sachbearbeitung-app-util-keykloak-http.service';

describe('SachbearbeitungAppUtilKeykloakHttpService', () => {
  let service: SachbearbeitungAppUtilKeykloakHttpService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SachbearbeitungAppUtilKeykloakHttpService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
