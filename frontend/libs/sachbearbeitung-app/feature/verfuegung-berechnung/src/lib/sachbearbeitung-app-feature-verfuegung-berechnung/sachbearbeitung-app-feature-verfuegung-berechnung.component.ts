import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
} from '@angular/core';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';

import { BerechnungStore } from '@dv/sachbearbeitung-app/data-access/berechnung';
import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';

@Component({
  selector: 'lib-sachbearbeitung-app-feature-verfuegung-berechnung',
  standalone: true,
  imports: [CommonModule, TranslateModule],
  templateUrl:
    './sachbearbeitung-app-feature-verfuegung-berechnung.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureVerfuegungBerechnungComponent {
  private store = inject(Store);
  gesuchViewSig = this.store.selectSignal(selectSharedDataAccessGesuchsView);
  berechnungStore = inject(BerechnungStore);

  berechnungenSig = computed(() => {
    const berechnungen = this.berechnungStore.berechnungen().data;
    if (!berechnungen) {
      return [];
    }
    return berechnungen.map((berechnung) => ({
      ...berechnung,
      parsedData: (() => {
        try {
          return JSON.parse(berechnung.berechnungsdaten ?? '');
        } catch (error) {
          return null;
        }
      })(),
    }));
  });

  berechnen(gesuchId: string) {
    this.berechnungStore.calculateBerechnung$({ gesuchId });
  }
}
