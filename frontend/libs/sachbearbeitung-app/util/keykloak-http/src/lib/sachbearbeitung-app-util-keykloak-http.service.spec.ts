import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { provideOAuthClient } from 'angular-oauth2-oidc';

import { KeykloakHttpService } from './sachbearbeitung-app-util-keykloak-http.service';

describe('SachbearbeitungAppUtilKeykloakHttpService', () => {
  let service: KeykloakHttpService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideOAuthClient()],
    });
    service = TestBed.inject(KeykloakHttpService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
