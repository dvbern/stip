import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { TranslocoDirective } from '@jsverse/transloco';

import { BerechnungsStammdaten } from '@dv/shared/model/gesuch';
import { PersoenlichesBudgetresultatView } from '@dv/shared/model/verfuegung';
import {
  SharedUiFormatChfPipe,
  SharedUiFormatChfPositivePipe,
} from '@dv/shared/ui/format-chf-pipe';

import { PositionComponent } from '../position/position.component';

@Component({
  selector: 'dv-persoenliche-einnahmen',
  imports: [
    TranslocoDirective,
    SharedUiFormatChfPipe,
    SharedUiFormatChfPositivePipe,
    PositionComponent,
  ],
  template: `
    <ng-container
      *transloco="
        let t;
        prefix: 'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen'
      "
    >
      <!-- Nettoerwerbseinkommen -->
      <dv-position
        [titleSig]="t('nettoerwerbseinkommen')"
        [infoSig]="
          t('nettoerwerbseinkommen.info', {
            freibetragErwerbseinkommen:
              stammdatenSig().freibetragErwerbseinkommen | formatChf,
          })
        "
        [personValueItemsSig]="einnahmenSig().nettoerwerbseinkommen"
        [amountSig]="
          einnahmenSig().nettoerwerbseinkommenTotal | formatChfPositive
        "
      >
      </dv-position>

      <!-- BGSA -->
      <dv-position
        [titleSig]="t('einnahmenBGSA')"
        [personValueItemsSig]="einnahmenSig().einnahmenBGSA"
        [amountSig]="einnahmenSig().einnahmenBGSATotal | formatChfPositive"
      >
        <a href="#1" class="tw:text-xs tw:align-text-top tw:no-underline!">
          1)
        </a>
      </dv-position>

      <!-- Kinder- und Ausbildungszulagen -->
      <dv-position
        [titleSig]="t('kinderUndAusbildungszulagen')"
        [personValueItemsSig]="einnahmenSig().kinderAusbildungszulagen"
        [amountSig]="
          einnahmenSig().kinderAusbildungszulagenTotal | formatChfPositive
        "
      >
      </dv-position>

      <!-- Unterhaltsbeiträge -->
      <dv-position
        [titleSig]="t('unterhaltsbeitraege')"
        [personValueItemsSig]="einnahmenSig().unterhaltsbeitraege"
        [amountSig]="
          einnahmenSig().unterhaltsbeitraegeTotal | formatChfPositive
        "
      >
      </dv-position>

      <!-- EO -->
      <dv-position
        [titleSig]="t('eoLeistungen')"
        [infoSig]="t('eoLeistungen.info')"
        [personValueItemsSig]="einnahmenSig().eoLeistungen"
        [amountSig]="einnahmenSig().eoLeistungenTotal | formatChfPositive"
      >
      </dv-position>

      <!-- Taggelder -->
      <dv-position
        [titleSig]="t('taggelderAHVIV')"
        [infoSig]="t('taggelderAHVIV.info')"
        [personValueItemsSig]="einnahmenSig().taggelderAHVIV"
        [amountSig]="einnahmenSig().taggelderAHVIVTotal | formatChfPositive"
      >
      </dv-position>

      <!-- Alimente/Renten -->
      <dv-position
        [titleSig]="t('renten')"
        [infoSig]="t('renten.info')"
        [personValueItemsSig]="einnahmenSig().renten"
        [amountSig]="einnahmenSig().rentenTotal | formatChfPositive"
      >
      </dv-position>

      <!-- Ergänzungsleistungen -->
      <dv-position
        [titleSig]="t('ergaenzungsleistungen')"
        [personValueItemsSig]="einnahmenSig().ergaenzungsleistungen"
        [amountSig]="
          einnahmenSig().ergaenzungsleistungenTotal | formatChfPositive
        "
      >
      </dv-position>

      <!-- Beiträge an Gemeindeinstitutionen -->
      <dv-position
        [titleSig]="t('beitraegeGemeindeInstitutionen')"
        [amountSig]="
          einnahmenSig().beitraegeGemeindeInstitutionen | formatChfPositive
        "
      >
      </dv-position>

      <!-- Andere Einnahmen -->
      <dv-position
        [titleSig]="t('andereEinnahmen')"
        [infoSig]="t('andereEinnahmen.info')"
        [personValueItemsSig]="einnahmenSig().andereEinnahmen"
        [amountSig]="einnahmenSig().andereEinnahmenTotal | formatChfPositive"
      >
      </dv-position>

      <!-- Anrechenbares Vermögen -->
      <dv-position
        [titleSig]="t('anrechenbaresVermoegen')"
        [infoSig]="t('anrechenbaresVermoegen.info', einnahmenSig())"
        [amountSig]="einnahmenSig().anrechenbaresVermoegen | formatChfPositive"
      >
      </dv-position>

      <!-- Elterliche Leistung -->
      <dv-position
        [titleSig]="t('elterlicheLeistung')"
        [amountSig]="einnahmenSig().elterlicheLeistung | formatChfPositive"
      >
      </dv-position>

      <!-- Total -->
      <div class="tw:flex mt-3 tw:gap-2">
        <div class="h4">
          {{ t('total') }}
        </div>
        <div class="h4 flex-grow-1 text-end text-nowrap">
          {{ einnahmenSig().total | formatChf }}
        </div>
      </div>
    </ng-container>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PersoenlicheEinnahmenComponent {
  einnahmenSig = input.required<PersoenlichesBudgetresultatView['einnahmen']>();
  stammdatenSig = input.required<BerechnungsStammdaten>();
}
