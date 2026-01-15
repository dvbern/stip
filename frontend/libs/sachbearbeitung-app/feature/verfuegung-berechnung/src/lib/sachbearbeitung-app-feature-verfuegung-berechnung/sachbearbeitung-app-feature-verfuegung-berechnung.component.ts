import { CommonModule } from '@angular/common';
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
import { TranslocoPipe } from '@jsverse/transloco';
import { Store } from '@ngrx/store';

import { BerechnungStore } from '@dv/shared/data-access/berechnung';
import { selectRouteId } from '@dv/shared/data-access/gesuch';
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
    CommonModule,
    MatCardModule,
    TranslocoPipe,
    MatExpansionModule,
    BerechnungsCardComponent,
    PersoenlicheEinnahmenComponent,
    PersoenlicheKostenComponent,
    FamilienEinnahmenComponent,
    FamilienKostenComponent,
    SharedUiLoadingComponent,
  ],
  templateUrl:
    './sachbearbeitung-app-feature-verfuegung-berechnung.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureVerfuegungBerechnungComponent {
  private store = inject(Store);
  // eslint-disable-next-line @angular-eslint/no-input-rename
  indexSig = input.required<string>({ alias: 'index' });
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
  gesuchIdSig = this.store.selectSignal(selectRouteId);
  berechnungStore = inject(BerechnungStore);

  berechnungenRawSig = computed<BerechnungView>(() => {
    const zusammenfassung =
      this.berechnungStore.berechnungZusammenfassungViewSig();

    const r = getBerechnungByIndex(
      zusammenfassung.berechnungsresultate,
      this.indexSig(),
    );

    const yearRange = [r.gueltigAb, r.gueltigBis]
      .map((d) => d.split('-')[0])
      .join('/');

    const view: BerechnungView = {
      persoenlich: {
        ...r.persoenlichesBudgetresultat,
        typ: 'persoenlich',
        yearRange,
        name: `${r.persoenlichesBudgetresultat.vorname} ${r.persoenlichesBudgetresultat.nachname}`,
        gueltigAb: r.gueltigAb,
        gueltigBis: r.gueltigBis,
      },
      familien: r.familienBudgetresultate.map((v) => ({
        ...v,
        typ: 'familien',
        name: `${v.vorname} ${v.nachname}`,
        gueltigAb: r.gueltigAb,
        gueltigBis: r.gueltigBis,
        anzahlMonate: r.persoenlichesBudgetresultat.anzahlMonate,
        yearRange,
      })),
      berechnungsStammdaten: r.berechnungsStammdaten,
    };

    return view;
  });

  constructor() {
    effect(() => {
      const gesuchId = this.gesuchIdSig();

      if (!gesuchId) {
        return;
      }
      this.berechnungStore.getBerechnungForGesuch$({ gesuchId });
    });
  }
}

const getBerechnungByIndex = <T>(berechnung: T[][], rawIndex: string) => {
  const [index, subIndex] = isNaN(+rawIndex)
    ? [
        +rawIndex.slice(0, -1),
        ['a', 'b'].findIndex((s) => s === rawIndex.slice(-1)),
      ]
    : [+rawIndex, 0];

  return berechnung[index - 1][Math.max(subIndex, 0)];
};
