import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { OAuthService } from 'angular-oauth2-oidc';

import { PermissionStore } from './shared-global-permission.store';

describe('PermissionStore', () => {
  let store: PermissionStore;
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        PermissionStore,
        {
          provide: OAuthService,
          useValue: {
            getAccessToken: () =>
              'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiR2VzdWNoc3RlbGxlciJdfX0.pW71nQV6d_VLc0a8R-4WxVkXOmega_z2RFZo7nTyYJI',
          },
        },
        provideHttpClient(),
      ],
    });
    store = TestBed.inject(PermissionStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial state', () => {
    expect(store.userRoles()).toStrictEqual(['Gesuchsteller']);
  });
});
