import { Injectable, inject } from '@angular/core';

import { PlzOrtStore } from '@dv/shared/data-access/plz-ort';
import { isInitial } from '@dv/shared/util/remote-data';

@Injectable({ providedIn: 'root' })
export class PlzOrtLookupService {
  private plzStore = inject(PlzOrtStore);

  getCachedPlzLookup() {
    if (isInitial(this.plzStore.plz())) {
      this.plzStore.loadAllPlz$();
    }
    return this.plzStore.plzViewSig;
  }
}
