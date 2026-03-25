import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { NavigationStore } from './shared-data-access-navigation.store';

describe('NavigationStore', () => {
  let store: NavigationStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [NavigationStore, provideHttpClient()],
    });
    store = TestBed.inject(NavigationStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.navigationItems()).toEqual([]);
  });
});
