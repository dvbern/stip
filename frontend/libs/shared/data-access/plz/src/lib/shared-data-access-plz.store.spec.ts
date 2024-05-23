import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';

import { PlzService, PlzStore } from './shared-data-access-plz.store';

class MockedPlzService {
  getPlz$() {
    return of([
      { plz: 3000, ort: 'Bern' },
      { plz: 3011, ort: 'Bern' },
      { plz: 3084, ort: 'Wabern' },
      { plz: 3094, ort: 'Schliern b. Köniz' },
      { plz: 8000, ort: 'Zürich' },
      { plz: 8452, ort: 'Adlikon' },
    ]);
  }
}

describe('PlzStore', () => {
  let store: PlzStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        PlzStore,
        provideHttpClient(),
        { provide: PlzService, useClass: MockedPlzService },
      ],
    });
    store = TestBed.inject(PlzStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('returns Adlikon (ZH) first using 84 as PLZ lookup instead of Wabern', () => {
    store.loadAllPlz$();
    const plz = store.plzViewSig().data?.fuzzyPlz.search('84');
    expect(plz?.map((p) => p.item.ort)).toEqual(['Adlikon', 'Wabern']);
  });
});
