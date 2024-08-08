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
import { addDays, differenceInMonths } from 'date-fns';

import { BerechnungStore } from '@dv/sachbearbeitung-app/data-access/berechnung';
import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import { SharedUiFormatChfPipe } from '@dv/shared/ui/format-chf-pipe';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
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
    SharedUiLoadingComponent,
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

    if (!gesuch || !gesuchFormular) {
      return null;
    }
    return {
      gesuchId: gesuch.id,
      person: `${gesuchFormular.personInAusbildung?.nachname} ${gesuchFormular.personInAusbildung?.vorname}`,
      // ...tranche,
      // monate: Math.abs(
      //   differenceInMonths(addDays(tranche.gueltigBis, 1), tranche.gueltigAb),
      // ),
    };
  });
  berechnungStore = inject(BerechnungStore);

  berechnungenSig = computed<{ loading: boolean; list: GesamtBerechnung[] }>(
    () => {
      const { gesuch, gesuchFormular } = this.gesuchViewSig();
      const berechnungenRd = this.berechnungStore.berechnungen();
      if (!berechnungenRd.data || !gesuch || !gesuchFormular) {
        return { loading: berechnungenRd.type === 'pending', list: [] };
      }
      const berechnungen = berechnungenRd.data;
      return {
        loading: false,
        list: berechnungen.map(
          ({
            berechnung,
            gueltigAb,
            gueltigBis,
            persoenlichesBudgetresultat: p,
            familienBudgetresultate,
          }) => ({
            total: berechnung,
            gueltigAb: gueltigAb,
            gueltigBis: gueltigBis,
            monate: differenceInMonths(addDays(gueltigBis, 1), gueltigAb),
            persoenlich: {
              typ: 'persoenlich' as const,
              name: `${gesuchFormular.personInAusbildung?.nachname} ${gesuchFormular.personInAusbildung?.vorname}`,
              total: p.persoenlichesbudgetBerechnet,
              totalEinnahmen: p.einnahmenPersoenlichesBudget,
              totalKosten: p.ausgabenPersoenlichesBudget,
              einnahmen: {
                anzahlPersonenImHaushalt: p.anzahlPersonenImHaushalt ?? 0,
                eigenerHaushalt: p.eigenerHaushalt,
                ...formatAllNumbersExceptTotal({
                  total: p.einnahmenPersoenlichesBudget,
                  nettoerwerbseinkommen: p.einkommen,
                  eoLeistungen: p.leistungenEO,
                  unterhaltsbeitraege: 0,
                  kinderUndAusbildungszulagen: p.kinderAusbildungszulagen,
                  ergaenzungsleistungen: p.ergaenzungsleistungen,
                  beitraegeGemeindeInstitution: p.gemeindeInstitutionen,
                  steuerbaresVermoegen: p.vermoegen,
                  elterlicheLeistung: p.anteilFamilienbudget,
                  einkommenPartner: p.einkommenPartner,
                }),
              },
              kosten: {
                anzahlPersonenImHaushalt: p.anzahlPersonenImHaushalt ?? 0,
                ...formatAllNumbersExceptTotal({
                  total: p.ausgabenPersoenlichesBudget,
                  anteilLebenshaltungskosten: 0,
                  mehrkostenVerpflegung: p.verpflegung,
                  grundbedarfPersonen: p.grundbedarf,
                  wohnkostenPersonen: p.wohnkosten,
                  medizinischeGrundversorgungPersonen:
                    p.medizinischeGrundversorgung,
                  kantonsGemeindesteuern: p.steuernKantonGemeinde,
                  bundessteuern: 0,
                  fahrkostenPartner: p.fahrkostenPartner,
                  verpflegungPartner: p.verpflegungPartner,
                  betreuungskostenKinder: p.fremdbetreuung,
                  ausbildungskosten: p.ausbildungskosten,
                  fahrkosten: p.fahrkosten,
                }),
              },
            },
            familien:
              familienBudgetresultate.map((f) => ({
                typ: 'familien' as const,
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
        ),
      };
    },
  );

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
