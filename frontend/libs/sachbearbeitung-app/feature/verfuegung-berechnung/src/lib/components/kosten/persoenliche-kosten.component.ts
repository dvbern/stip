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

import { HideZeroDirective } from '../../hide-zero.directive';
import { PositionComponent } from '../position/position.component';

@Component({
  selector: 'dv-persoenliche-kosten',
  imports: [
    TranslocoDirective,
    SharedUiFormatChfPipe,
    SharedUiFormatChfPositivePipe,
    PositionComponent,
    HideZeroDirective,
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
      @let hideZero = hideZeroSig();

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
        *dvHideZero="hideZero; value: kosten.ausbildungskostenTotal"
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
        *dvHideZero="hideZero; value: kosten.fahrkostenTotal"
      >
      </dv-position>

      <!-- Mehrkosten für auswärtige Verpflegung -->
      <dv-position
        [titleSig]="t('mehrkostenVerpflegung')"
        [infoSig]="t('nurElternWohnend.info')"
        [amountSig]="kosten.verpflegungskosten | formatChfPositive"
        *dvHideZero="hideZero; value: kosten.verpflegungskosten"
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
        *dvHideZero="hideZero; value: kosten.grundbedarf"
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
        *dvHideZero="hideZero; value: kosten.wohnkosten"
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
        *dvHideZero="hideZero; value: kosten.medizinischeGrundversorgungTotal"
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
        *dvHideZero="hideZero; value: kosten.fahrkostenPartner"
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
        *dvHideZero="hideZero; value: kosten.verpflegungPartner"
      >
      </dv-position>

      <!-- Betreuungskosten für Kinder -->
      <dv-position
        [titleSig]="t('betreuungskostenKinder')"
        [amountSig]="kosten.betreuungskostenKinder | formatChfPositive"
        *dvHideZero="hideZero; value: kosten.betreuungskostenKinder"
      >
      </dv-position>

      <!-- Kantons- und Gemeindesteuern -->
      <dv-position
        [titleSig]="t('kantonsGemeindesteuern')"
        [infoSig]="t('steuern.info')"
        [amountSig]="kosten.kantonsGemeindesteuern | formatChfPositive"
        *dvHideZero="hideZero; value: kosten.kantonsGemeindesteuern"
      >
      </dv-position>

      <!-- Bundessteuern -->
      <dv-position
        [titleSig]="t('bundessteuern')"
        [infoSig]="t('steuern.info')"
        [amountSig]="kosten.bundessteuern | formatChfPositive"
        *dvHideZero="hideZero; value: kosten.bundessteuern"
      >
      </dv-position>

      <!-- Ungedeckter Anteil Lebenshaltungskosten  -->
      <dv-position
        [titleSig]="t('anteilLebenshaltungskosten')"
        [infoSig]="t('anteilLebenshaltungskosten.info')"
        [amountSig]="kosten.anteilLebenshaltungskosten | formatChfPositive"
        *dvHideZero="hideZero; value: kosten.anteilLebenshaltungskosten"
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
  hideZeroSig = input<boolean>(false);
  budgetSig = input.required<PersoenlichesBudgetresultatView>();
}
