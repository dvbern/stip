import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { FehlgeschlageneZahlungenStore } from './sachbearbeitung-app-data-access-fehlgeschlagene-zahlungen.store';

describe('FehlgeschlageneZahlungenStore', () => {
  let store: FehlgeschlageneZahlungenStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [FehlgeschlageneZahlungenStore, provideHttpClient()],
    });
    store = TestBed.inject(FehlgeschlageneZahlungenStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.paginatedFailedAuszahlungBuchhaltung()).toEqual({
      type: 'initial',
      data: undefined,
      error: undefined,
    });
  });
});
