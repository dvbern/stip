// eslint-disable-next-line @nx/enforce-module-boundaries
import { signal } from '@angular/core';

import { Land } from '@dv/shared/model/gesuch';
// eslint-disable-next-line @nx/enforce-module-boundaries
import { LandLookupService } from '@dv/shared/util-data-access/land-lookup';

// Mock land data
const mockLaender: Land[] = [
  {
    id: 'uuid1',
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
    id: 'uuid2',
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
    id: 'uuid3',
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
  getCachedLandLookup: vitest.fn().mockReturnValue(signal(mockLaender)),
};

export function provideLandLookupMock() {
  return {
    provide: LandLookupService,
    useValue: mockLandLookupService,
  };
}
