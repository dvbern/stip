// eslint-disable-next-line @nx/enforce-module-boundaries
import { signal } from '@angular/core';

import { Land } from '@dv/shared/model/gesuch';
// eslint-disable-next-line @nx/enforce-module-boundaries
import { LandLookupService } from '@dv/shared/util-data-access/land-lookup';

// Mock land data
const mockLaender = [
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
].map((land) => ({
  ...land,
  testId: land.deKurzform,
  displayValueDe: land.deKurzform,
  displayValueFr: land.frKurzform,
})) satisfies Land[];

// Mock LandLookupService
const mockLandLookupService = {
  getCachedLandLookup: vitest.fn().mockReturnValue(signal(mockLaender)),

  isValidLandEntry(land: Land | undefined): boolean {
    if (!land) {
      return false;
    }

    return land.eintragGueltig;
  },
};

export function provideLandLookupMock() {
  return {
    provide: LandLookupService,
    useValue: mockLandLookupService,
  };
}
