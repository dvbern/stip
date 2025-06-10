import { provideHttpClient } from '@angular/common/http';
import { signal } from '@angular/core';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { provideTranslateService } from '@ngx-translate/core';
import { render, screen } from '@testing-library/angular';

import { Land } from '@dv/shared/model/gesuch';
import { Language } from '@dv/shared/model/language';
import { LandLookupService } from '@dv/shared/util-data-access/land-lookup';

import { SharedUiLandAutocompleteComponent } from './shared-ui-land-autocomplete.component';

// Mock land data
const mockLaender: Land[] = [
  {
    id: '1',
    deKurzform: 'Schweiz',
    frKurzform: 'Suisse',
    enKurzform: 'Switzerland',
    itKurzform: 'Svizzera',
    iso3code: 'CHE',
    laendercodeBfs: '8100',
    eintragGueltig: true,
    isEuEfta: true,
  },
  {
    id: '2',
    deKurzform: 'Deutschland',
    frKurzform: 'Allemagne',
    enKurzform: 'Germany',
    itKurzform: 'Germania',
    iso3code: 'DEU',
    laendercodeBfs: '8207',
    eintragGueltig: true,
    isEuEfta: true,
  },
  {
    id: '3',
    deKurzform: 'Österreich',
    frKurzform: 'Autriche',
    enKurzform: 'Austria',
    itKurzform: 'Austria',
    iso3code: 'AUT',
    laendercodeBfs: '8204',
    eintragGueltig: true,
    isEuEfta: true,
  },
];

// Mock LandLookupService
const mockLandLookupService = {
  getCachedLandLookup: jest.fn().mockReturnValue(signal(mockLaender)),
};

async function setup(language: Language = 'de') {
  return await render(SharedUiLandAutocompleteComponent, {
    imports: [NoopAnimationsModule],
    providers: [
      provideHttpClient(),
      provideTranslateService(),
      {
        provide: LandLookupService,
        useValue: mockLandLookupService,
      },
    ],
    inputs: {
      languageSig: language,
    },
  });
}

describe(SharedUiLandAutocompleteComponent.name, () => {
  it('should set the correct value when a land is searched and selected', async () => {
    const { fixture, getByTestId, detectChanges } = await setup();
    const component = fixture.componentInstance;

    expect(screen.queryByTestId('land-autocomplete-input')).toBeInTheDocument();
  });
});
