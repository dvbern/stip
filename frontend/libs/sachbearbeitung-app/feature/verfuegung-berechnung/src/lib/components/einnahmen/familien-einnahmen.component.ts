import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { TranslocoDirective } from '@jsverse/transloco';

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
    <ng-container *transloco="let t">
      <!-- Total Einkünfte -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.totalEinkuenfte'
          )
        "
        [amountSig]="einnahmenSig().totalEinkuenfte | formatChfPositive"
      >
      </dv-position>

      <!-- Einkünfte nach BGSA 1) -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.einnahmenBGSA'
          )
        "
        [amountSig]="einnahmenSig().einnahmenBGSA | formatChfPositive"
      >
      </dv-position>

      <!-- Ergänzungsleistungen -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.ergaenzungsleistungen'
          )
        "
        [amountSig]="einnahmenSig().ergaenzungsleistungen | formatChfPositive"
      >
      </dv-position>

      <!-- Andere Einnahmen -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.andereEinnahmen'
          )
        "
        [amountSig]="einnahmenSig().andereEinnahmen | formatChfPositive"
      >
      </dv-position>

      <!-- Abzüglich Mietwert -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.mietwert'
          )
        "
        [amountSig]="einnahmenSig().eigenmietwert | formatChfNegative: true"
      >
      </dv-position>

      <!-- Abzüglich Unterhaltsbeträge  -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.unterhaltsbeitraege'
          )
        "
        [amountSig]="
          einnahmenSig().unterhaltsbeitraege | formatChfNegative: true
        "
      >
      </dv-position>

      <!-- Beiträge von Selbständigerwerbenden in die Säule 3a -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.beitraegeSaule3a'
          )
        "
        [infoSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.beitraegeSaule3a.info',
            einnahmenSig()
          )
        "
        [amountSig]="einnahmenSig().sauele3 | formatChfNegative: true"
      >
      </dv-position>

      <!-- Beiträge von Selbständigerwerbenden in die 2.Säule -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.beitraegeSaule2'
          )
        "
        [infoSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.beitraegeSaule2.info'
          )
        "
        [amountSig]="einnahmenSig().sauele2 | formatChfNegative: true"
      >
      </dv-position>

      <!-- Abzüglich Alimente/Renten für in Ausbildung stehende Person -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.kinderalimente'
          )
        "
        [amountSig]="einnahmenSig().renten | formatChfNegative: true"
      >
      </dv-position>

      <!-- todo: mit stammdaten? Einkommensfreibetrag  -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.einkommensfreibeitrag'
          )
        "
        [infoSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.einkommensfreibeitrag.info'
          )
        "
        [amountSig]="
          einnahmenSig().einkommensfreibetrag | formatChfNegative: true
        "
      >
      </dv-position>

      <!-- todo: Zwischentotal -->

      <!-- Anrechenbares Vermögen -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.anrechenbaresVermoegen'
          )
        "
        [infoSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.anrechenbaresVermoegen.info',
            einnahmenSig()
          )
        "
        [amountSig]="einnahmenSig().anrechenbaresVermoegen | formatChfPositive"
      >
      </dv-position>

      <!-- Total -->
      <div class="tw:flex mt-3 tw:gap-2">
        <div class="h4 m-0">
          {{
            t(
              'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.total'
            )
          }}
        </div>
        <div class="h4 flex-grow-1 text-end text-nowrap">
          {{ einnahmenSig().total | formatChf }}
        </div>
      </div>
    </ng-container>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class FamilienEinnahmenComponent {
  einnahmenSig = input.required<FamilienBudgetresultatView['einnahmen']>();
}
