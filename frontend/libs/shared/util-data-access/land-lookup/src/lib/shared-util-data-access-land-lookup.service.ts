import { Injectable, inject } from '@angular/core';

import { LandStore } from '@dv/shared/data-access/land';

@Injectable({
  providedIn: 'root',
})
export class LandLookupService {
  private landStore = inject(LandStore);

  getCachedLandLookup() {
    if (this.landStore.laender().type === 'initial') {
      this.landStore.loadLaender$();
    }
    return this.landStore.landListViewSig;
  }
}
