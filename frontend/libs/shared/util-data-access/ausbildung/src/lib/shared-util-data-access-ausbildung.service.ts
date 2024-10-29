import { Injectable, inject } from '@angular/core';
import { Store } from '@ngrx/store';

import { AusbildungStore } from '@dv/shared/data-access/ausbildung';
import { selectLanguage } from '@dv/shared/data-access/language';

@Injectable({
  providedIn: 'root',
})
export class AusbildungService {
  private store = inject(Store);
  private ausbildungStore = inject(AusbildungStore);

  getLanguagesSig() {
    return this.store.selectSignal(selectLanguage);
  }
}
