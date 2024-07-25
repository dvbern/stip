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

import { GesamtBerechnung } from '../../models';
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

  berechnungenSig = computed<GesamtBerechnung[]>(() => {
    const { gesuch, gesuchFormular } = this.gesuchViewSig();
    const tranche = gesuch?.gesuchTrancheToWorkWith;
    const berechnungen = this.berechnungStore.berechnungen().data;
    if (!berechnungen || !tranche || !gesuchFormular) {
      return [];
    }
    return berechnungen.map(
      ({
        berechnung,
        persoenlichesBudgetresultat: p,
        familienBudgetresultate,
      }) => ({
        total: berechnung,
        persoenlich: {
          typ: 'persoenlich',
          name: `${gesuchFormular.personInAusbildung?.nachname} ${gesuchFormular.personInAusbildung?.vorname}`,
          total: p.persoenlichesbudgetBerechnet,
          totalEinnahmen: p.einnahmenPersoenlichesBudget,
          totalKosten: p.ausgabenPersoenlichesBudget,
          einnahmen: {
            eigenerHaushalt: p.eigenerHaushalt,
            ...formatAllNumbersExceptTotal({
              total: p.einnahmenPersoenlichesBudget,
              nettoerwerbseinkommen: p.einkommen,
              eoLeistungen: p.leistungenEO,
              unterhaltsbeitraege: 0,
              kinderUndAusbildungszulagen: p.kinderAusbildungszulagen,
              ergaenzungsleistungen: p.ergaenzungsleistungen,
              beitraegeGemeindeInstitution: p.gemeindeInstitutionen,
              steuerbaresVermoegen: 0,
              elterlicheLeistung: 0,
              einkommenPartner: p.einkommenPartner,
            }),
          },
          kosten: formatAllNumbersExceptTotal({
            total: p.ausgabenPersoenlichesBudget,
            anteilLebenshaltungskosten: 0,
            mehrkostenVerpflegung: p.verpflegung,
            grundbedarf0Personen: p.grundbedarf,
            wohnkosten0Personen: p.wohnkosten,
            medizinischeGrundversorgung0Personen: p.medizinischeGrundversorgung,
            kantonsGemeindesteuern: p.steuernKantonGemeinde,
            bundessteuern: 0,
            fahrkostenPartner: p.fahrkostenPartner,
            verpflegungPartner: p.verpflegungPartner,
            betreuungskostenKinder: p.fremdbetreuung,
            ausbildungskosten: p.ausbildungskosten,
            fahrkosten: p.fahrkosten,
          }),
        },
        familien:
          familienBudgetresultate.map((f) => ({
            typ: 'familien',
            nameKey: `sachbearbeitung-app.verfuegung.berechnung.familien.typ.${f.familienBudgetTyp}`,
            year: gesuch?.gesuchsperiode.gesuchsjahr.technischesJahr - 1,
            total: f.familienbudgetBerechnet,
            totalEinnahmen: f.einnahmenFamilienbudget,
            totalKosten: f.ausgabenFamilienbudget,
            einnahmen: formatAllNumbersExceptTotal({
              total: f.einnahmenFamilienbudget,
              totalEinkuenfte: f.totalEinkuenfte,
              ergaenzungsleistungen: f.ergaenzungsleistungen,
              steuerbaresVermoegen: f.steuerbaresVermoegen,
              vermoegensaufrechnung: f.vermoegen,
              abzuege: f.einzahlungSaeule23a,
              beitraegeSaule: f.eigenmietwert,
              mietwert: f.eigenmietwert,
              alimenteOderRenten: f.alimente,
              einkommensfreibeitrag: f.einkommensfreibetrag,
            }),
            kosten: formatAllNumbersExceptTotal({
              total: f.ausgabenFamilienbudget,
              anzahlPersonen: f.anzahlPersonenImHaushalt,
              grundbedarf: f.grundbedarf,
              wohnkosten: f.effektiveWohnkosten,
              medizinischeGrundversorgung: f.medizinischeGrundversorgung,
              integrationszulage: f.integrationszulage,
              kantonsGemeindesteuern: f.steuernKantonGemeinde,
              bundessteuern: f.steuernBund,
              fahrkosten: f.fahrkostenPerson1,
              fahrkostenPartner: f.fahrkostenPerson2,
              verpflegung: f.essenskostenPerson1,
              verpflegungPartner: f.essenskostenPerson2,
            }),
          })) ?? [],
      }),
    );
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

type FormatedBerechnung<T extends Record<string, number>> = {
  [K in keyof T]: string;
};

const formatAllNumbersExceptTotal = <
  T extends Record<string, number> & { total: number },
>(
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
