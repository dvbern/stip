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
import { Store } from '@ngrx/store';
import { TranslatePipe } from '@ngx-translate/core';
import { addDays, differenceInMonths } from 'date-fns';

import { BerechnungStore } from '@dv/shared/data-access/berechnung';
import { selectRouteId } from '@dv/shared/data-access/gesuch';
import { SharedUiFormatChfPipe } from '@dv/shared/ui/format-chf-pipe';
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
        TranslatePipe,
        MatExpansionModule,
        SharedUiFormatChfPipe,
        BerechnungsCardComponent,
        PersoenlicheEinnahmenComponent,
        PersoenlicheKostenComponent,
        FamilienEinnahmenComponent,
        FamilienKostenComponent,
        SharedUiLoadingComponent,
    ],
    templateUrl: './sachbearbeitung-app-feature-verfuegung-berechnung.component.html',
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class SachbearbeitungAppFeatureVerfuegungBerechnungComponent {
  private store = inject(Store);
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

  berechnungenSig = computed(() => {
    const index = this.indexSig();
    const view = this.berechnungStore.berechnungZusammenfassungViewSig();

    if (view.loading || view.berechnungsresultate.length === 0) {
      return { loading: view.loading, list: [] };
    }

    const {
      berechnung,
      gueltigAb,
      gueltigBis,
      nameGesuchsteller,
      berechnungsStammdaten: sd,
      persoenlichesBudgetresultat: p,
      familienBudgetresultate,
      berechnungsanteilKinder,
      type: geteilteBerechnungsArt,
    } = getBerechnungByIndex(view.berechnungsresultate, index);

    return {
      loading: false,
      berechnung: {
        total: view.totalBetragStipendium,
        gueltigAb: gueltigAb,
        gueltigBis: gueltigBis,
        // Add 2 days as date-fns differenceInMonths does have issues with february
        // 2024-07-01 to 2025-03-01 should be 8 months but is 7, with 2025-03-02 it is 8 months
        monate: differenceInMonths(addDays(gueltigBis, 2), gueltigAb),
        persoenlich: {
          typ: 'persoenlich' as const,
          name: nameGesuchsteller,
          total: p.persoenlichesbudgetBerechnet,
          totalEinnahmen: p.einnahmenPersoenlichesBudget,
          totalKosten: p.ausgabenPersoenlichesBudget,
          totalVorTeilung: p.totalVorTeilung,
          geteilteBerechnung: geteilteBerechnungsArt
            ? {
                berechnungsanteilKinder: Math.round(berechnungsanteilKinder),
                anteil: berechnung,
              }
            : null,
          einnahmen: {
            anzahlPersonenImHaushalt: p.anzahlPersonenImHaushalt ?? 0,
            eigenerHaushalt: p.eigenerHaushalt,
            total: p.einnahmenPersoenlichesBudget,
            nettoerwerbseinkommen: p.einkommen,
            alimente: p.alimente,
            eoLeistungen: p.leistungenEO,
            unterhaltsbeitraege: p.rente,
            kinderUndAusbildungszulagen: p.kinderAusbildungszulagen,
            ergaenzungsleistungen: p.ergaenzungsleistungen,
            beitraegeGemeindeInstitution: p.gemeindeInstitutionen,
            steuerbaresVermoegen: p.steuerbaresVermoegen,
            anrechenbaresVermoegen: p.anrechenbaresVermoegen,
            elterlicheLeistung: p.anteilFamilienbudget,
            einkommenPartner: p.einkommenPartner,
            freibetragErwerbseinkommen: sd.freibetragErwerbseinkommen,
            vermoegensanteilInProzent: sd.vermoegensanteilInProzent,
            limiteAlterAntragsstellerHalbierungElternbeitrag:
              sd.limiteAlterAntragsstellerHalbierungElternbeitrag,
          },
          kosten: {
            anzahlPersonenImHaushalt: p.anzahlPersonenImHaushalt ?? 0,
            total: p.ausgabenPersoenlichesBudget,
            anteilLebenshaltungskosten: p.anteilLebenshaltungskosten,
            mehrkostenVerpflegung: p.verpflegung,
            grundbedarfPersonen: p.grundbedarf,
            wohnkostenPersonen: p.wohnkosten,
            medizinischeGrundversorgungPersonen: p.medizinischeGrundversorgung,
            kantonsGemeindesteuern: p.steuernKantonGemeinde,
            bundessteuern: 0,
            fahrkosten: p.fahrkosten,
            fahrkostenPartner: p.fahrkostenPartner,
            verpflegungPartner: p.verpflegungPartner,
            betreuungskostenKinder: p.fremdbetreuung,
            ausbildungskosten: p.ausbildungskosten,
          },
        },
        familien:
          familienBudgetresultate.map((f) => ({
            typ: 'familien' as const,
            nameKey: `sachbearbeitung-app.verfuegung.berechnung.familien.typ.${f.familienBudgetTyp}`,
            year: view.year - 1,
            total: f.familienbudgetBerechnet,
            totalEinnahmen: f.einnahmenFamilienbudget,
            totalKosten: f.ausgabenFamilienbudget,
            einnahmen: {
              total: f.einnahmenFamilienbudget,
              totalEinkuenfte: f.totalEinkuenfte,
              ergaenzungsleistungen: f.ergaenzungsleistungen,
              steuerbaresVermoegen: f.steuerbaresVermoegen,
              anrechenbaresVermoegen: f.anrechenbaresVermoegen,
              vermoegensaufrechnung: f.anrechenbaresVermoegen,
              sauele2: f.saeule2,
              sauele3: f.saeule3a,
              mietwert: f.eigenmietwert,
              kinderalimente: f.alimente,
              einkommensfreibeitrag: sd.einkommensfreibetrag,
              maxSaeule3a: sd.maxSaeule3a,
              freibetragVermoegen: sd.freibetragVermoegen,
              vermoegensanteilInProzent: sd.vermoegensanteilInProzent,
            },
            kosten: {
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
            },
          })) ?? [],
      },
    };
  });

  constructor() {
    effect(
      () => {
        const gesuchId = this.gesuchIdSig();

        if (!gesuchId) {
          return;
        }
        this.berechnungStore.getBerechnungForGesuch$({ gesuchId });
      },
      { allowSignalWrites: true },
    );
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
