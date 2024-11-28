import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { provideOAuthClient } from 'angular-oauth2-oidc';

import { SozialdienstStore } from './shared-data-access-sozialdienst.store';

describe('SozialdienstStore', () => {
  let store: SozialdienstStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SozialdienstStore, provideHttpClient(), provideOAuthClient()],
    });
    store = TestBed.inject(SozialdienstStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.sozialdienst()).toEqual({
      type: 'initial',
      data: undefined,
      error: undefined,
    });
  });
});
