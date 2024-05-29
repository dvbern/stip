import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';

import { PlzOrtStore, PlzService } from './shared-data-access-plz-ort.store';

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

describe('PlzOrtStore', () => {
  let store: PlzOrtStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        PlzOrtStore,
        provideHttpClient(),
        { provide: PlzService, useClass: MockedPlzService },
      ],
    });
    store = TestBed.inject(PlzOrtStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('returns Adlikon (ZH) first using 84 as PLZ lookup instead of Wabern', () => {
    store.loadAllPlz$();
    const plz = store.plzViewSig().list?.fuzzyPlz.search('84');
    expect(plz?.map((p) => p.item.ort)).toEqual(['Adlikon', 'Wabern']);
  });
});
