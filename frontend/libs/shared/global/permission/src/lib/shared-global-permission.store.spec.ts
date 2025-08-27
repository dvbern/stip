import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { provideMockStore } from '@ngrx/store/testing';

import { provideSharedOAuthServiceWithGesuchstellerJWT } from '@dv/shared/pattern/vitest-test-setup';

import { PermissionStore } from './shared-global-permission.store';

describe('PermissionStore', () => {
  let store: PermissionStore;
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideMockStore(),
        PermissionStore,
        provideSharedOAuthServiceWithGesuchstellerJWT(),
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
