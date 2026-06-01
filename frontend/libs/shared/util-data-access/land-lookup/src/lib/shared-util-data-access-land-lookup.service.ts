import { Injectable, inject } from '@angular/core';

import { LandStore } from '@dv/shared/data-access/land';

@Injectable({
  providedIn: 'root',
})
export class LandLookupService {
  private landStore = inject(LandStore);

  constructor() {
    this.landStore.loadLaender$();
  }

  getCachedLandLookup() {
    return this.landStore.autocompleteLandListViewSig;
  }
}
