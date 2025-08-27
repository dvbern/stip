import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

// eslint-disable-next-line @nx/enforce-module-boundaries
import { provideCompileTimeConfig } from '@dv/shared/pattern/vitest-test-setup';

import { DokumentsStore } from './shared-data-access-dokuments.store';

describe('DokumentsStore', () => {
  let store: DokumentsStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideCompileTimeConfig(), provideHttpClient()],
    });
    store = TestBed.inject(DokumentsStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.dokuments()).toEqual({
      data: undefined,
      error: undefined,
      type: 'initial',
    });
    expect(store.documentsToUpload()).toEqual({
      data: undefined,
      error: undefined,
      type: 'initial',
    });
  });
});
