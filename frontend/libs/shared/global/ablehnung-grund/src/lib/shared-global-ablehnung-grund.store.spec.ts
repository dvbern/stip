import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { AblehnungGrundStore } from './shared-global-ablehnung-grund.store';

describe('AblehnungGrundStore', () => {
  let store: AblehnungGrundStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AblehnungGrundStore, provideHttpClient()],
    });
    store = TestBed.inject(AblehnungGrundStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.gruende()).toEqual({
      type: 'initial',
      data: undefined,
      error: undefined,
    });
  });
});
