/* eslint-disable @angular-eslint/no-input-rename */
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
  input,
} from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { TranslocoDirective } from '@jsverse/transloco';
import { Store } from '@ngrx/store';

import { BerechnungStore } from '@dv/shared/data-access/berechnung';
import { selectRouteGesuchId } from '@dv/shared/data-access/gesuch';
import { TranchenBerechnungsresultat } from '@dv/shared/model/gesuch';
import { BerechnungView } from '@dv/shared/model/verfuegung';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';

import {
  FamilienEinnahmenComponent,
  FamilienKostenComponent,
  PersoenlicheEinnahmenComponent,
  PersoenlicheKostenComponent,
} from '../components';
import { BerechnungsCardComponent } from '../components/berechnungs-card/berechnungs-card.component';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-verfuegung-berechnung',
  imports: [
    MatCardModule,
    MatSlideToggleModule,
    MatExpansionModule,
    BerechnungsCardComponent,
    PersoenlicheEinnahmenComponent,
    PersoenlicheKostenComponent,
    FamilienEinnahmenComponent,
    FamilienKostenComponent,
    SharedUiLoadingComponent,
    TranslocoDirective,
  ],
  templateUrl:
    './sachbearbeitung-app-feature-verfuegung-berechnung.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureVerfuegungBerechnungComponent {
  private store = inject(Store);

  indexSig = input.required<string>({ alias: 'index' });
  tranchenIdSig = input<string | null>(null, { alias: 'trancheId' });

  expansionState = {
    persoenlich: {
      einnahmen: false,
      kosten: false,
    },
    familie1: {
      einnahmen: false,
      kosten: false,
    },
    familie2: {
      einnahmen: false,
      kosten: false,
    },
  };
  gesuchIdSig = this.store.selectSignal(selectRouteGesuchId);

  verfuegungIdSig = input<string | null>(null, { alias: 'berechnungId' });
  berechnungStore = inject(BerechnungStore);

  berechnungenRawSig = computed<BerechnungView | null>(() => {
    const zusammenfassung =
      this.berechnungStore.berechnungZusammenfassungViewSig();

    const r = getBerechnungByTrancheIdByIndex(
      zusammenfassung.berechnungsresultate,
      this.tranchenIdSig(),
      this.indexSig(),
    );

    if (!r) {
      return null;
    }

    const view: BerechnungView = {
      persoenlich: {
        ...r.persoenlichesBudgetresultat,
        typ: 'persoenlich',
        yearRange: r.yearRange,
        name: `${r.persoenlichesBudgetresultat.vorname} ${r.persoenlichesBudgetresultat.nachname}`,
        gueltigAb: r.gueltigAb,
        gueltigBis: r.gueltigBis,
      },
      familien: r.familienBudgetresultate.map((v) => ({
        ...v,
        typ: 'familien',
        yearRange: r.yearRange,
        name: `${v.vorname} ${v.nachname}`,
        gueltigAb: r.gueltigAb,
        gueltigBis: r.gueltigBis,
        anzahlMonate: r.persoenlichesBudgetresultat.anzahlMonate,
      })),
      berechnungsStammdaten: r.berechnungsStammdaten,
    };

    return view;
  });

  constructor() {
    effect(() => {
      const gesuchId = this.gesuchIdSig();

      const verfuegungId = this.verfuegungIdSig();

      if (!gesuchId) {
        return;
      }

      if (verfuegungId) {
        // case mit verfuegungId => versionierte Berechnung für Verfuegung
        this.berechnungStore.getBerechnungForVerfuegung$({ verfuegungId });
      } else {
        // case aktuelles gesuch
        this.berechnungStore.getBerechnungForGesuch$({ gesuchId });
      }
    });
  }
}

const getBerechnungByTrancheIdByIndex = (
  berechnung: Record<string, TranchenBerechnungsresultat[]>,
  trancheId: string | null,
  rawIndex: string,
) => {
  if (!trancheId) {
    return undefined;
  }

  const trancheBerechnungsresultate = berechnung[trancheId];

  if (!trancheBerechnungsresultate) {
    return undefined;
  }

  return trancheBerechnungsresultate[+rawIndex - 1];
};
