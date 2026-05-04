import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { TranslocoDirective } from '@jsverse/transloco';

import { BerechnungsStammdaten } from '@dv/shared/model/gesuch';
import { FamilienBudgetresultatView } from '@dv/shared/model/verfuegung';
import {
  SharedUiFormatChfNegativePipe,
  SharedUiFormatChfPipe,
  SharedUiFormatChfPositivePipe,
} from '@dv/shared/ui/format-chf-pipe';

import { HideZeroDirective } from '../../hide-zero.directive';
import { PositionComponent } from '../position/position.component';

@Component({
  selector: 'dv-familien-einnahmen',
  imports: [
    TranslocoDirective,
    SharedUiFormatChfPipe,
    SharedUiFormatChfNegativePipe,
    SharedUiFormatChfPositivePipe,
    PositionComponent,
    HideZeroDirective,
  ],
  template: `
    <ng-container
      *transloco="
        let t;
        prefix: 'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen'
      "
    >
      @let budget = budgetSig();
      @let einnahmen = budget.einnahmen;
      @let stammdaten = stammdatenSig();
      @let hideZero = hideZeroSig();

      <!-- Total Einkünfte -->
      <dv-position
        [titleSig]="t('totalEinkuenfte')"
        [infoSig]="t('totalEinkuenfte.info')"
        [amountSig]="einnahmen.totalEinkuenfte | formatChfPositive"
        *dvHideZero="hideZero; value: einnahmen.totalEinkuenfte"
      >
      </dv-position>

      <!-- Einkünfte nach BGSA 1) -->
      <dv-position
        [titleSig]="t('einnahmenBGSA')"
        [amountSig]="einnahmen.einnahmenBGSA | formatChfPositive"
        *dvHideZero="hideZero; value: einnahmen.einnahmenBGSA"
      >
        <span ngProjectAs="title-appendix" class="tw:text-xs tw:align-text-top">
          1)
        </span>
      </dv-position>

      <!-- Ergänzungsleistungen -->
      <dv-position
        [titleSig]="t('ergaenzungsleistungen')"
        [amountSig]="einnahmen.ergaenzungsleistungen | formatChfPositive"
        *dvHideZero="hideZero; value: einnahmen.ergaenzungsleistungen"
      >
      </dv-position>

      <!-- Andere Einnahmen -->
      <dv-position
        [titleSig]="t('andereEinnahmen')"
        [amountSig]="einnahmen.andereEinnahmen | formatChfPositive"
        *dvHideZero="hideZero; value: einnahmen.andereEinnahmen"
      >
      </dv-position>

      <!-- Abzüglich Mietwert -->
      <dv-position
        [titleSig]="t('mietwert')"
        [amountSig]="einnahmen.eigenmietwert | formatChfNegative: true"
        *dvHideZero="hideZero; value: einnahmen.eigenmietwert"
      >
      </dv-position>

      <!-- Abzüglich Unterhaltsbeträge  -->
      <dv-position
        [titleSig]="t('unterhaltsbeitraege')"
        [infoSig]="t('unterhaltsbeitraege.info')"
        [amountSig]="einnahmen.unterhaltsbeitraege | formatChfNegative: true"
        *dvHideZero="hideZero; value: einnahmen.unterhaltsbeitraege"
      >
      </dv-position>

      <!-- Beiträge von Selbständigerwerbenden in die Säule 3a -->
      <dv-position
        [titleSig]="t('beitraegeSaule3a')"
        [infoSig]="t('beitraegeSaule3a.info')"
        [amountSig]="einnahmen.sauele3 | formatChfNegative: true"
        *dvHideZero="hideZero; value: einnahmen.sauele3"
      >
      </dv-position>

      <!-- Beiträge von Selbständigerwerbenden in die 2.Säule -->
      <dv-position
        [titleSig]="t('beitraegeSaule2')"
        [infoSig]="t('beitraegeSaule2.info')"
        [amountSig]="einnahmen.sauele2 | formatChfNegative: true"
        *dvHideZero="hideZero; value: einnahmen.sauele2"
      >
      </dv-position>

      <!-- Abzüglich Alimente/Renten für in Ausbildung stehende Person -->
      <dv-position
        [titleSig]="t('renten')"
        [infoSig]="t('renten.info')"
        [amountSig]="einnahmen.renten | formatChfNegative: true"
        *dvHideZero="hideZero; value: einnahmen.renten"
      >
      </dv-position>

      <!--  Einkommensfreibetrag  -->
      <dv-position
        [titleSig]="t('einkommensfreibeitrag')"
        class="tw:border-b tw:border-gray-500"
        [infoSig]="
          t('einkommensfreibeitrag.info', {
            einkommensfreibetrag: einnahmen.einkommensfreibetrag | formatChf,
          })
        "
        [amountSig]="einnahmen.einkommensfreibetrag | formatChfNegative: true"
        *dvHideZero="hideZero; value: einnahmen.einkommensfreibetrag"
      >
      </dv-position>

      <!-- Zwischentotal anrechenbare, jährliche Einnahmen, welcher betrag? -->
      <dv-position
        [titleSig]="t('zwischentotal')"
        [infoSig]="t('zwischentotal.info')"
        [amountSig]="einnahmen.zwischentotal | formatChf"
        *dvHideZero="hideZero; value: einnahmen.zwischentotal"
      >
      </dv-position>

      <!-- Anrechenbares Vermögen -->
      <dv-position
        [titleSig]="t('anrechenbaresVermoegen')"
        [infoSig]="
          t('anrechenbaresVermoegen.info', {
            vermoegensanteilInProzent: stammdaten.vermoegensanteilInProzent,
            steuerbaresVermoegen: einnahmen.steuerbaresVermoegen | formatChf,
            freibetragVermoegen: stammdaten.freibetragVermoegen | formatChf,
          })
        "
        [amountSig]="einnahmen.anrechenbaresVermoegen | formatChfPositive"
        *dvHideZero="hideZero; value: einnahmen.anrechenbaresVermoegen"
      >
      </dv-position>

      <!-- Total -->
      <dv-position
        type="title"
        sepparator="no-separator"
        [titleSig]="t('total')"
        [amountSig]="einnahmen.total | formatChf"
      >
      </dv-position>
    </ng-container>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class FamilienEinnahmenComponent {
  hideZeroSig = input<boolean>(false);
  budgetSig = input.required<FamilienBudgetresultatView>();
  stammdatenSig = input.required<BerechnungsStammdaten>();
}
