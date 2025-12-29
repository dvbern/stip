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
import { addDays, differenceInMonths } from 'date-fns';

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

    const view: BerechnungView = {
      persoenlich: {
        ...r.persoenlichesBudgetresultat,
        typ: 'persoenlich',
        yearRange: [r.ausbildungAb, r.ausbildungBis]
          .map((d) => d.split('.')[1])
          .join('/'),
        name: `${r.vornamePia} ${r.nachnamePia}`,
        geburtsdatum: r.geburtsdatum,
        gueltigAb: r.gueltigAb,
        gueltigBis: r.gueltigBis,
        sozialversicherungsnummer: r.sozialversicherungsnummer,
        geteilteBerechnung: r.type
          ? {
              berechnungsanteilKinder: Math.round(r.berechnungsanteilKinder),
              anteil: r.berechnung,
            }
          : null,
        einnahmen: {
          ...r.persoenlichesBudgetresultat.einnahmen,
          // vornamePia: r.vornamePia,
          // vornamePartner: r.vornamePartner,
        },
        kosten: {
          ...r.persoenlichesBudgetresultat.kosten,
          // vornamePia: r.vornamePia,
          // vornamePartner: r.vornamePartner,
          anzahlPersonenImHaushalt:
            r.persoenlichesBudgetresultat.anzahlPersonenImHaushalt,
        },
      },
      familien: r.familienBudgetresultate.map((v) => ({
        ...v,
        typ: 'familien',
      })),
      berechnung: r.berechnung,
      berechnungsStammdaten: r.berechnungsStammdaten,
    };

    return view;
  });

  // to be removed if not used
  berechnungenSigOld = computed(() => {
    const index = this.indexSig();
    const view = this.berechnungStore.berechnungZusammenfassungViewSig();

    if (view.loading || view.berechnungsresultate.length === 0) {
      return { loading: view.loading };
    }

    const {
      sozialversicherungsnummer,
      geburtsdatum,
      ausbildungAb,
      ausbildungBis,
      berechnung,
      gueltigAb,
      gueltigBis,
      vornamePia,
      nachnamePia,
      vornamePartner,
      berechnungsStammdaten: stammDaten,
      persoenlichesBudgetresultat: p,
      familienBudgetresultate,
      berechnungsanteilKinder,
      type: geteilteBerechnungsArt,
    } = getBerechnungByIndex(view.berechnungsresultate, index);

    const monate = differenceInMonths(addDays(gueltigBis, 2), gueltigAb);

    return {
      loading: false,
      berechnung: {
        total: view.berechnung,
        persoenlich: {
          typ: 'persoenlich' as const,
          name: `${vornamePia} ${nachnamePia}`,
          sozialversicherungsnummer,
          geburtsdatum,
          total: p.total,
          yearRange: [ausbildungAb, ausbildungBis]
            .map((d) => d.split('.')[1])
            .join('/'),
          gueltigAb: gueltigAb,
          gueltigBis: gueltigBis,
          monate,
          berechnung,
          totalVorTeilung: p.total,
          totalEinnahmen: p.einnahmen.total,
          totalKosten: p.kosten.total,
          geteilteBerechnung: geteilteBerechnungsArt
            ? {
                berechnungsanteilKinder: Math.round(berechnungsanteilKinder),
                anteil: berechnung,
              }
            : null,
          einnahmen: {
            vornamePia,
            vornamePartner,
            limiteAlterAntragsstellerHalbierungElternbeitrag:
              stammDaten.limiteAlterAntragsstellerHalbierungElternbeitrag,
            ...p.einnahmen,
          },
          kosten: {
            vornamePia,
            vornamePartner,
            anzahlPersonenImHaushalt: p.anzahlPersonenImHaushalt,
            ...p.kosten,
          },
        },
        familien:
          familienBudgetresultate.map((f) => ({
            typ: 'familien' as const,
            familienBudgetTyp: f.steuerdatenTyp,
            name: `TODO: !!! TBD`, // TODO: find out which names to use here
            sozialversicherungsnummer: `TODO: !!! TBD`, // TODO
            geburtsdatum: `TODO: !!! TBD`, // TODO
            steuerjahr: view.year - 1,
            veranlagungsStatus: `TODO: !!! TBD`, // TODO
            gueltigAb: gueltigAb,
            gueltigBis: gueltigBis,
            monate,
            total: f.total,
            totalEinnahmen: f.einnahmen.total,
            totalKosten: f.kosten.total,
            einnahmen: {
              ...f.einnahmen,
              einkommensfreibetrag: stammDaten.einkommensfreibetrag,
              einkommensfreibetragLimite: stammDaten.abzugslimite, // TODO: check if this is correct
              freibetragVermoegen: stammDaten.freibetragVermoegen,
            },
            kosten: {
              ...f.kosten,
              anzahlPersonenImHaushalt: f.anzahlPersonenImHaushalt,
              integrationszulageLimite: stammDaten.abzugslimite, // TODO: check if this is correct
            },
          })) ?? [],
      },
    };
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
