import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { TranslocoDirective } from '@jsverse/transloco';

import { BerechnungsStammdaten } from '@dv/shared/model/gesuch';
import { FamilienBudgetresultatView } from '@dv/shared/model/verfuegung';
import {
  SharedUiFormatChfNegativePipe,
  SharedUiFormatChfPipe,
  SharedUiFormatChfPositivePipe,
} from '@dv/shared/ui/format-chf-pipe';

import { PositionComponent } from '../position/position.component';

@Component({
  selector: 'dv-familien-einnahmen',
  imports: [
    TranslocoDirective,
    SharedUiFormatChfPipe,
    SharedUiFormatChfNegativePipe,
    SharedUiFormatChfPositivePipe,
    PositionComponent,
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

      <!-- Total Einkünfte -->
      <dv-position
        [titleSig]="t('totalEinkuenfte')"
        [infoSig]="t('totalEinkuenfte.info')"
        [amountSig]="einnahmen.totalEinkuenfte | formatChfPositive"
      >
      </dv-position>

      <!-- Einkünfte nach BGSA 1) -->
      <dv-position
        [titleSig]="t('einnahmenBGSA')"
        [amountSig]="einnahmen.einnahmenBGSA | formatChfPositive"
      >
      </dv-position>

      <!-- Ergänzungsleistungen -->
      <dv-position
        [titleSig]="t('ergaenzungsleistungen')"
        [amountSig]="einnahmen.ergaenzungsleistungen | formatChfPositive"
      >
      </dv-position>

      <!-- Andere Einnahmen -->
      <dv-position
        [titleSig]="t('andereEinnahmen')"
        [amountSig]="einnahmen.andereEinnahmen | formatChfPositive"
      >
      </dv-position>

      <!-- Abzüglich Mietwert -->
      <dv-position
        [titleSig]="t('mietwert')"
        [amountSig]="einnahmen.eigenmietwert | formatChfNegative: true"
      >
      </dv-position>

      <!-- Abzüglich Unterhaltsbeträge  -->
      <dv-position
        [titleSig]="t('unterhaltsbeitraege')"
        [infoSig]="t('unterhaltsbeitraege.info')"
        [amountSig]="einnahmen.unterhaltsbeitraege | formatChfNegative: true"
      >
      </dv-position>

      <!-- Beiträge von Selbständigerwerbenden in die Säule 3a -->
      <dv-position
        [titleSig]="t('beitraegeSaule3a')"
        [infoSig]="t('beitraegeSaule3a.info')"
        [amountSig]="einnahmen.sauele3 | formatChfNegative: true"
      >
      </dv-position>

      <!-- Beiträge von Selbständigerwerbenden in die 2.Säule -->
      <dv-position
        [titleSig]="t('beitraegeSaule2')"
        [infoSig]="t('beitraegeSaule2.info')"
        [amountSig]="einnahmen.sauele2 | formatChfNegative: true"
      >
      </dv-position>

      <!-- Abzüglich Alimente/Renten für in Ausbildung stehende Person -->
      <dv-position
        [titleSig]="t('renten')"
        [infoSig]="t('renten.info')"
        [amountSig]="einnahmen.renten | formatChfNegative: true"
      >
      </dv-position>

      <!--  Einkommensfreibetrag  -->
      <dv-position
        [titleSig]="t('einkommensfreibeitrag')"
        [infoSig]="einnahmen.einkommensfreibetrag | formatChf"
        [amountSig]="einnahmen.einkommensfreibetrag | formatChfNegative: true"
      >
      </dv-position>

      <!-- Zwischentotal anrechenbare, jährliche Einnahmen, welcher betrag? -->
      <dv-position
        [titleSig]="t('zwischentotal')"
        [infoSig]="t('zwischentotal.info')"
        [amountSig]="einnahmen.zwischentotal | formatChf"
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
      >
      </dv-position>

      <!-- Total -->
      <div class="tw:flex mt-3 tw:gap-2">
        <div class="h4 m-0">
          {{ t('total') }}
        </div>
        <div class="h4 flex-grow-1 text-end text-nowrap">
          {{ einnahmen.total | formatChf }}
        </div>
      </div>
    </ng-container>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class FamilienEinnahmenComponent {
  budgetSig = input.required<FamilienBudgetresultatView>();
  stammdatenSig = input.required<BerechnungsStammdaten>();
}
