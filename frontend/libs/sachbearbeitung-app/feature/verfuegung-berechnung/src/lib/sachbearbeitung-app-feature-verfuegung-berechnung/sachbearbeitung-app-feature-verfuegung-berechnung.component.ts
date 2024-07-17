import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
} from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatExpansionModule } from '@angular/material/expansion';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';
import { differenceInMonths } from 'date-fns';

import { BerechnungStore } from '@dv/sachbearbeitung-app/data-access/berechnung';
import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import { SharedUiFormatChfPipe } from '@dv/shared/ui/format-chf-pipe';
import { toFormatedNumber } from '@dv/shared/util/maskito-util';

@Component({
  selector: 'lib-sachbearbeitung-app-feature-verfuegung-berechnung',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    TranslateModule,
    MatExpansionModule,
    SharedUiFormatChfPipe,
  ],
  templateUrl:
    './sachbearbeitung-app-feature-verfuegung-berechnung.component.html',
  styleUrl:
    './sachbearbeitung-app-feature-verfuegung-berechnung.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureVerfuegungBerechnungComponent {
  private store = inject(Store);
  gesuchViewSig = this.store.selectSignal(selectSharedDataAccessGesuchsView);
  viewSig = computed(() => {
    const { gesuch, gesuchFormular } = this.gesuchViewSig();
    const tranche = gesuch?.gesuchTrancheToWorkWith;

    if (!gesuch || !tranche || !gesuchFormular) {
      return null;
    }
    return {
      gesuchId: gesuch.id,
      person: `${gesuchFormular.personInAusbildung?.nachname} ${gesuchFormular.personInAusbildung?.vorname}`,
      ...tranche,
      monate: Math.abs(
        differenceInMonths(tranche.gueltigBis, tranche.gueltigAb),
      ),
    };
  });
  berechnungStore = inject(BerechnungStore);

  berechnungenSig = computed(() => {
    const { gesuch } = this.gesuchViewSig();
    const tranche = gesuch?.gesuchTrancheToWorkWith;
    const berechnungen = this.berechnungStore.berechnungen().data;
    if (!berechnungen || !tranche) {
      return [];
    }
    return berechnungen.map(() => ({
      einnahmen: {
        ...formatAllNumbers(exampleBerechnung.einnahmen),
        total: exampleBerechnung.einnahmen.total,
      },
      kosten: formatAllNumbers(exampleBerechnung.kosten),
    }));
  });

  constructor() {
    effect(
      () => {
        const { gesuchId } = this.gesuchViewSig();

        if (!gesuchId) {
          return;
        }
        this.berechnungStore.calculateBerechnung$({ gesuchId });
      },
      { allowSignalWrites: true },
    );
  }
}

const exampleBerechnung = {
  einnahmen: {
    total: 42524,
    nettoerwerbseinkommen: 0,
    eoLeistungen: 0,
    unterhaltsbeitraege: 0,
    kinderUndAusbildungszulagen: 0,
    ergaenzungsleistungen: 0,
    beitraegeGemeindeInstitution: 3000,
    steuerbaresVermoegen: 0,
    elterlicheLeistung: 39524,
    einkommenPartner: 0,
  },
  kosten: {
    anteilLebenshaltungskosten: 0,
    mehrkostenVerpflegung: 1645,
    grundbedarf0Personen: 0,
    wohnkosten0Personen: 0,
    medizinischeGrundversorgung0Personen: 0,
    kantonsGemeindesteuern: 0,
    bundessteuern: 0,
    fahrkostenPartner: 0,
    verpflegungPartner: 0,
    betreuungskostenKinder: 0,
    ausbildungskosten: 0,
    fahrkosten: 0,
  },
};

type FormatedBerechnung<T extends Record<string, number>> = {
  [K in keyof T]: string;
};

const formatAllNumbers = <T extends Record<string, number>>(obj: T) => {
  const newObj = {} as unknown as FormatedBerechnung<T>;
  for (const key in obj) {
    const value = obj[key];
    if (typeof value === 'number') {
      newObj[key] = toFormatedNumber(value);
    }
  }
  return newObj;
};
