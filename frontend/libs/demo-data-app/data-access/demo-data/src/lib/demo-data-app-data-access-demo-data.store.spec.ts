import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { DemoDataStore } from './demo-data-app-data-access-demo-data.store';

describe('DemoDataStore', () => {
  let store: DemoDataStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DemoDataStore, provideHttpClient()],
    });
    store = TestBed.inject(DemoDataStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.demoData()).toEqual({
      type: 'initial',
      data: undefined,
      error: undefined,
    });
  });
});
