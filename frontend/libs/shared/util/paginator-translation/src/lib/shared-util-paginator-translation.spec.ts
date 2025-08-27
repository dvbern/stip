import { TestBed } from '@angular/core/testing';

// eslint-disable-next-line @nx/enforce-module-boundaries
import { getTranslocoModule } from '@dv/shared/pattern/vitest-test-setup';

import { SharedUtilPaginatorTranslation } from './shared-util-paginator-translation';

describe('SharedUtilPaginatorTranslationService', () => {
  let service: SharedUtilPaginatorTranslation;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [getTranslocoModule()],
      providers: [SharedUtilPaginatorTranslation],
    });
    service = TestBed.inject(SharedUtilPaginatorTranslation);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
