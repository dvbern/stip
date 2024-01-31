import { TestBed } from '@angular/core/testing';
import { TranslateModule } from '@ngx-translate/core';

import { SharedUtilCountriesService } from './shared-util-countries.service';

describe('SharedUtilCountriesService', () => {
  let service: SharedUtilCountriesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TranslateModule.forRoot()],
    });
    service = TestBed.inject(SharedUtilCountriesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
