import { TestBed } from '@angular/core/testing';
// eslint-disable-next-line @nx/enforce-module-boundaries
import { provideMockStore } from '@ngrx/store/testing';
import { firstValueFrom } from 'rxjs';

// eslint-disable-next-line @nx/enforce-module-boundaries
import { selectLanguage } from '@dv/shared/data-access/language';
import { provideLandLookupMock } from '@dv/shared/util-fn/comp-test';

import { SharedUiTranslatedLandPipe } from './shared-ui-translated-land.pipe';

describe('SharedUiTranslatedLandPipe', () => {
  describe('with German language', () => {
    let pipe: SharedUiTranslatedLandPipe;

    beforeEach(async () => {
      await TestBed.configureTestingModule({
        providers: [
          SharedUiTranslatedLandPipe,
          provideLandLookupMock(),
          provideMockStore({
            initialState: {
              language: {
                language: 'de',
              },
            },
            selectors: [{ selector: selectLanguage, value: 'de' }],
          }),
        ],
      }).compileComponents();

      pipe = TestBed.inject(SharedUiTranslatedLandPipe);
    });

    it('should create', () => {
      expect(pipe).toBeTruthy();
    });

    it('should transform land ID to German name', async () => {
      const result = pipe.transform('uuid1');
      const translatedName = await firstValueFrom(result);

      expect(translatedName).toBe('Schweiz');
    });
  });

  describe('with French language', () => {
    let pipe: SharedUiTranslatedLandPipe;

    beforeEach(async () => {
      await TestBed.configureTestingModule({
        providers: [
          SharedUiTranslatedLandPipe,
          provideLandLookupMock(),
          provideMockStore({
            initialState: {
              language: {
                language: 'fr',
              },
            },
            selectors: [{ selector: selectLanguage, value: 'fr' }],
          }),
        ],
      }).compileComponents();

      pipe = TestBed.inject(SharedUiTranslatedLandPipe);
    });

    it('should transform land ID to French name', async () => {
      const result = pipe.transform('uuid1');
      const translatedName = await firstValueFrom(result);

      expect(translatedName).toBe('Suisse');
    });
  });

  describe('edge cases', () => {
    let pipe: SharedUiTranslatedLandPipe;

    beforeEach(async () => {
      await TestBed.configureTestingModule({
        providers: [
          SharedUiTranslatedLandPipe,
          provideLandLookupMock(),
          provideMockStore({
            initialState: {
              language: {
                language: 'fr',
              },
            },
            selectors: [{ selector: selectLanguage, value: 'fr' }],
          }),
        ],
      }).compileComponents();

      pipe = TestBed.inject(SharedUiTranslatedLandPipe);
    });

    it('should return empty string for undefined land ID', async () => {
      const result = pipe.transform(undefined);
      const translatedName = await firstValueFrom(result);

      expect(translatedName).toBe('');
    });

    it('should return empty string for non-existent land ID', async () => {
      const result = pipe.transform('uuid4-non-existent');
      const translatedName = await firstValueFrom(result);

      expect(translatedName).toBe('');
    });
  });
});
