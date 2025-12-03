import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { VersteckteElternStore } from './shared-data-access-versteckte-eltern.store';

describe('VersteckteElternStore', () => {
  let store: VersteckteElternStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [VersteckteElternStore, provideHttpClient()],
    });
    store = TestBed.inject(VersteckteElternStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.versteckteEltern()).toEqual({
      type: 'initial',
      data: undefined,
      error: undefined,
    });
  });
});
