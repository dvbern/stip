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

import { FamilienBerechnung, PersoenlicheBerechnung } from '../../models';
import {
  FamilienEinnahmenComponent,
  FamilienKostenComponent,
  PersoenlicheEinnahmenComponent,
  PersoenlicheKostenComponent,
} from '../components';
import { BerechnungsCardComponent } from '../components/berechnungs-card/berechnungs-card.component';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-verfuegung-berechnung',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    TranslateModule,
    MatExpansionModule,
    SharedUiFormatChfPipe,
    BerechnungsCardComponent,
    PersoenlicheEinnahmenComponent,
    PersoenlicheKostenComponent,
    FamilienEinnahmenComponent,
    FamilienKostenComponent,
  ],
  templateUrl:
    './sachbearbeitung-app-feature-verfuegung-berechnung.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureVerfuegungBerechnungComponent {
  private store = inject(Store);
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

  berechnungenSig = computed<
    {
      persoenlich: PersoenlicheBerechnung;
      familien: FamilienBerechnung[];
    }[]
  >(() => {
    const { gesuch, gesuchFormular } = this.gesuchViewSig();
    const tranche = gesuch?.gesuchTrancheToWorkWith;
    const berechnungen = this.berechnungStore.berechnungen().data ?? [1];
    if (!berechnungen || !tranche || !gesuchFormular) {
      return [];
    }
    return berechnungen.map(() => ({
      persoenlich: {
        typ: 'persoenlich',
        name: `${gesuchFormular.personInAusbildung?.nachname} ${gesuchFormular.personInAusbildung?.vorname}`,
        total: 39809,
        totalEinnahmen: exampleBerechnung.einnahmen.total,
        totalKosten: exampleBerechnung.kosten.total,
        einnahmen: formatAllNumbersExceptTotal(exampleBerechnung.einnahmen),
        kosten: formatAllNumbersExceptTotal(exampleBerechnung.kosten),
      } as const,
      familien:
        gesuchFormular.elterns?.map(
          (e) =>
            ({
              typ: 'familien',
              nameKey: `sachbearbeitung-app.verfuegung.berechnung.familien.typ.${e.elternTyp}`,
              year: gesuch?.gesuchsperiode.gesuchsjahr.technischesJahr - 1,
              total: 39524,
              totalEinnahmen: exampleFamilienBerechnung.einnahmen.total,
              totalKosten: exampleFamilienBerechnung.kosten.total,
              einnahmen: formatAllNumbersExceptTotal(
                exampleFamilienBerechnung.einnahmen,
              ),
              kosten: formatAllNumbersExceptTotal(
                exampleFamilienBerechnung.kosten,
              ),
            }) as const,
        ) ?? [],
    }));
  });

  constructor() {
    effect(
      () => {
        const { gesuchId } = this.gesuchViewSig();

        if (!gesuchId) {
          return;
        }
        // this.berechnungStore.calculateBerechnung$({ gesuchId });
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
    total: 2715,
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
    ausbildungskosten: 500,
    fahrkosten: 570,
  },
};

const exampleFamilienBerechnung = {
  einnahmen: {
    total: 90835,
    totalEinkuenfte: 0,
    ergaenzungsleistungen: 0,
    steuerbaresVermoegen: 0,
    vermoegensaufrechnung: 0,
    abzuege: 0,
    beitraegeSaule: 0,
    mietwert: 0,
    alimenteOderRenten: 0,
    einkommensfreibeitrag: 6000,
  },
  kosten: {
    total: 51311,
    anzahlPersonen: 2,
    grundbedarf: 17940,
    wohnkosten: 9800,
    medizinischeGrundversorgung: 10000,
    integrationszulage: 2400,
    kantonsGemeindesteuern: 10639,
    bundessteuern: 532,
    fahrkosten: 0,
    fahrkostenPartner: 0,
    verpflegung: 0,
    verpflegungPartner: 0,
  },
};

type FormatedBerechnung<T extends Record<string, number>> = {
  [K in keyof T]: string;
};

const formatAllNumbersExceptTotal = <T extends Record<string, number>>(
  obj: T,
) => {
  const newObj = {} as unknown as FormatedBerechnung<T>;
  for (const key in obj) {
    const value = obj[key];
    if (typeof value === 'number') {
      newObj[key] = toFormatedNumber(value);
    }
  }
  return { ...newObj, total: obj['total'] };
};
