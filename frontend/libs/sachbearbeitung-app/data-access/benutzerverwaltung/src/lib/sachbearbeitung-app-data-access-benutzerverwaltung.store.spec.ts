import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { provideOAuthClient } from 'angular-oauth2-oidc';

import { BenutzerverwaltungStore } from './sachbearbeitung-app-data-access-benutzerverwaltung.store';

describe('BenutzerverwaltungStore', () => {
  let store: BenutzerverwaltungStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        BenutzerverwaltungStore,
        provideHttpClient(),
        provideOAuthClient(),
      ],
    });
    store = TestBed.inject(BenutzerverwaltungStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.benutzer()).toEqual({
      data: undefined,
      error: undefined,
      type: 'initial',
    });
  });
});
