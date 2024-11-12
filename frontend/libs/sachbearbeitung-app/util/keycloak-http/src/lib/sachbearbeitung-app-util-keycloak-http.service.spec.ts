import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { provideOAuthClient } from 'angular-oauth2-oidc';

import { KeycloakHttpService } from './sachbearbeitung-app-util-keycloak-http.service';

describe('SachbearbeitungAppUtilKeycloakHttpService', () => {
  let service: KeycloakHttpService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideOAuthClient()],
    });
    service = TestBed.inject(KeycloakHttpService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
