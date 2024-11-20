import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { provideOAuthClient } from 'angular-oauth2-oidc';

import { PermissionStore } from './shared-data-access-global-permission.store';

describe('PermissionStore', () => {
  let store: PermissionStore;
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [PermissionStore, provideOAuthClient(), provideHttpClient()],
    });
    store = TestBed.inject(PermissionStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial state', () => {
    expect(store.userRoles()).toBeNull();
  });
});
