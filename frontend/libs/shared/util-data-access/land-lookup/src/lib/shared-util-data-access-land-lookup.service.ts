import { Injectable, inject } from '@angular/core';

import { LandStore } from '@dv/shared/data-access/land';
import { Land } from '@dv/shared/model/gesuch';

@Injectable({
  providedIn: 'root',
})
export class LandLookupService {
  private landStore = inject(LandStore);

  getCachedLandLookup() {
    if (this.landStore.laender().type === 'initial') {
      this.landStore.loadLaender$();
    }
    return this.landStore.autocompleteLandListViewSig;
  }

  isValidLandEntry(land: Land | undefined): boolean {
    if (!land) {
      return false;
    }

    return land.eintragGueltig;
  }
}
