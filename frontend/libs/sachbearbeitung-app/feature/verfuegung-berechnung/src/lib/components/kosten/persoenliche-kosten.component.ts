import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  input,
} from '@angular/core';
import { TranslocoDirective } from '@jsverse/transloco';

import { PersoenlichesBudgetresultatView } from '@dv/shared/model/verfuegung';
import {
  SharedUiFormatChfPipe,
  SharedUiFormatChfPositivePipe,
} from '@dv/shared/ui/format-chf-pipe';

import { ShowZeroDirective } from '../../show-zero.directive';
import { PositionComponent } from '../position/position.component';

@Component({
  selector: 'dv-persoenliche-kosten',
  imports: [
    TranslocoDirective,
    SharedUiFormatChfPipe,
    SharedUiFormatChfPositivePipe,
    PositionComponent,
    ShowZeroDirective,
  ],
  template: `
    <ng-container
      *transloco="
        let t;
        prefix: 'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten'
      "
    >
      @let budget = budgetSig();
      @let kosten = budget.kosten;
      @let showZero = showZeroSig();

      <!-- Ausbildungskosten der/des Auszubildenden -->
      <dv-position
        [titleSig]="t('ausbildungskosten')"
        [infoSig]="
          t('ausbildungskosten.info', {
            ausbildungskosten: kosten.ausbildungskosten | formatChf,
            anzahlPersonenImHaushalt: budget.anzahlPersonenImHaushalt,
          })
        "
        [amountSig]="kosten.ausbildungskostenTotal | formatChfPositive"
        *dvShowZero="showZero; value: kosten.ausbildungskostenTotal"
      >
        <span ngProjectAs="title-appendix" class="tw:text-xs tw:align-text-top">
          2)
        </span>
      </dv-position>

      <!-- Fahrkosten der/des Auszubildenden -->
      <dv-position
        [titleSig]="t('fahrkosten')"
        [infoSig]="
          t('fahrkosten.info', {
            fahrkosten: kosten.fahrkosten | formatChf,
            anzahlPersonenImHaushalt: budget.anzahlPersonenImHaushalt,
          })
        "
        [amountSig]="kosten.fahrkostenTotal | formatChfPositive"
        *dvShowZero="showZero; value: kosten.fahrkostenTotal"
      >
      </dv-position>

      <!-- Mehrkosten für auswärtige Verpflegung -->
      <dv-position
        [titleSig]="t('mehrkostenVerpflegung')"
        [infoSig]="t('nurElternWohnend.info')"
        [amountSig]="kosten.verpflegungskosten | formatChfPositive"
        *dvShowZero="showZero; value: kosten.verpflegungskosten"
      >
        <span ngProjectAs="title-appendix" class="tw:text-xs tw:align-text-top">
          2)
        </span>
      </dv-position>

      <!-- Grundbedarf für 0 Personenhaushalt -->
      <dv-position
        [titleSig]="
          t('grundbedarfPersonen', {
            anzahl: budget.anzahlPersonenImHaushalt,
          })
        "
        [infoSig]="t('nurEigenerHaushalt.info')"
        [amountSig]="kosten.grundbedarf | formatChfPositive"
        *dvShowZero="showZero; value: kosten.grundbedarf"
      >
        <span ngProjectAs="title-appendix" class="tw:text-xs tw:align-text-top">
          2)
        </span>
      </dv-position>

      <!-- Wohnkosten für anz Personenhaushalt -->
      <dv-position
        [titleSig]="
          t('wohnkostenPersonen', {
            anzahl: budget.anzahlPersonenImHaushalt,
          })
        "
        [infoSig]="t('nurEigenerHaushalt.info')"
        [amountSig]="kosten.wohnkosten | formatChfPositive"
        *dvShowZero="showZero; value: kosten.wohnkosten"
      >
        <span ngProjectAs="title-appendix" class="tw:text-xs tw:align-text-top">
          2)
        </span>
      </dv-position>

      <!-- Medizinische Grundversorgung für anz Personenhaushalt -->
      <dv-position
        [titleSig]="
          t('medizinischeGrundversorgungPersonen', {
            anzahl: budget.anzahlPersonenImHaushalt,
          })
        "
        [infoSig]="t('nurEigenerHaushalt.info')"
        [personValueItemsSig]="kosten.medizinischeGrundversorgung"
        [amountSig]="
          kosten.medizinischeGrundversorgungTotal | formatChfPositive
        "
        *dvShowZero="showZero; value: kosten.medizinischeGrundversorgungTotal"
      >
        <span ngProjectAs="title-appendix" class="tw:text-xs tw:align-text-top">
          2)
        </span>
      </dv-position>

      <!-- Fahrkosten Ehepartnerin/Ehepartner -->
      <dv-position
        [titleSig]="t('fahrkostenPartner')"
        [infoSig]="
          t('fahrkostenPartner.info', {
            fahrkosten: kosten.fahrkostenPartner | formatChf,
            anzahlPersonenImHaushalt: budget.anzahlPersonenImHaushalt,
          })
        "
        [amountSig]="kosten.fahrkostenPartner | formatChfPositive"
        *dvShowZero="showZero; value: kosten.fahrkostenPartner"
      >
      </dv-position>

      <!-- Verpflegung Ehepartnerin/Ehepartner -->
      <dv-position
        [titleSig]="t('verpflegungPartner')"
        [infoSig]="
          t('verpflegungPartner.info', {
            verpflegungPartner: kosten.verpflegungPartner | formatChf,
            anzahlPersonenImHaushalt: budget.anzahlPersonenImHaushalt,
          })
        "
        [amountSig]="kosten.verpflegungPartner | formatChfPositive"
        *dvShowZero="showZero; value: kosten.verpflegungPartner"
      >
      </dv-position>

      <!-- Betreuungskosten für Kinder -->
      <dv-position
        [titleSig]="t('betreuungskostenKinder')"
        [amountSig]="kosten.betreuungskostenKinder | formatChfPositive"
        *dvShowZero="showZero; value: kosten.betreuungskostenKinder"
      >
      </dv-position>

      <!-- Kantons- und Gemeindesteuern -->
      <dv-position
        [titleSig]="t('kantonsGemeindesteuern')"
        [infoSig]="t('steuern.info')"
        [amountSig]="kosten.kantonsGemeindesteuern | formatChfPositive"
        *dvShowZero="showZero; value: kosten.kantonsGemeindesteuern"
      >
      </dv-position>

      <!-- Bundessteuern -->
      <dv-position
        [titleSig]="t('bundessteuern')"
        [infoSig]="t('steuern.info')"
        [amountSig]="kosten.bundessteuern | formatChfPositive"
        *dvShowZero="showZero; value: kosten.bundessteuern"
      >
      </dv-position>

      <!-- Ungedeckter Anteil Lebenshaltungskosten  -->
      <dv-position
        [titleSig]="t('anteilLebenshaltungskosten')"
        [infoSig]="t('anteilLebenshaltungskosten.info')"
        [amountSig]="kosten.anteilLebenshaltungskosten | formatChfPositive"
        *dvShowZero="showZero; value: kosten.anteilLebenshaltungskosten"
      >
      </dv-position>

      <!-- Total -->
      <dv-position
        type="title"
        [titleSig]="t('total')"
        [amountSig]="kosten.total | formatChf"
      >
      </dv-position>
    </ng-container>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PersoenlicheKostenComponent {
  @HostBinding('class') classes = 'tw:dv-verfuegung-position-list';
  showZeroSig = input<boolean>(false);
  budgetSig = input.required<PersoenlichesBudgetresultatView>();
}
