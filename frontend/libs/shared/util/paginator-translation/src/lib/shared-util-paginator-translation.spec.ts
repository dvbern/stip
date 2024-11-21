import { TestBed } from '@angular/core/testing';
import { provideTranslateService } from '@ngx-translate/core';

import { SharedUtilPaginatorTranslation } from './shared-util-paginator-translation';

describe('SharedUtilPaginatorTranslationService', () => {
  let service: SharedUtilPaginatorTranslation;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SharedUtilPaginatorTranslation, provideTranslateService()],
    });
    service = TestBed.inject(SharedUtilPaginatorTranslation);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
