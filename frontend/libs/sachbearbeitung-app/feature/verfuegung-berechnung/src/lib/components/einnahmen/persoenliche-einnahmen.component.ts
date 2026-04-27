import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { TranslocoDirective } from '@jsverse/transloco';

import { BerechnungsStammdaten } from '@dv/shared/model/gesuch';
import { PersoenlichesBudgetresultatView } from '@dv/shared/model/verfuegung';
import {
  SharedUiFormatChfPipe,
  SharedUiFormatChfPositivePipe,
} from '@dv/shared/ui/format-chf-pipe';

import { ShowZeroDirective } from '../../show-zero.directive';
import { PositionComponent } from '../position/position.component';

@Component({
  selector: 'dv-persoenliche-einnahmen',
  imports: [
    CommonModule,
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
        prefix: 'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen'
      "
    >
      @let einnahmen = budgetSig().einnahmen;
      @let showZero = showZeroSig();

      <!-- Nettoerwerbseinkommen -->
      <dv-position
        [titleSig]="t('nettoerwerbseinkommen')"
        [infoSig]="
          t('nettoerwerbseinkommen.info', {
            freibetragErwerbseinkommen:
              stammdatenSig().freibetragErwerbseinkommen | formatChf,
          })
        "
        [personValueItemsSig]="einnahmen.nettoerwerbseinkommen"
        [amountSig]="einnahmen.nettoerwerbseinkommenTotal | formatChfPositive"
        *dvShowZero="showZero; value: einnahmen.nettoerwerbseinkommenTotal"
      >
      </dv-position>

      <!-- BGSA -->
      <dv-position
        [titleSig]="t('einnahmenBGSA')"
        [amountSig]="einnahmen.einnahmenBGSATotal | formatChfPositive"
        *dvShowZero="showZero; value: einnahmen.einnahmenBGSATotal"
      >
        <span ngProjectAs="title-appendix" class="tw:text-xs tw:align-text-top">
          1)
        </span>
      </dv-position>

      <!-- Kinder- und Ausbildungszulagen -->
      <dv-position
        [titleSig]="t('kinderUndAusbildungszulagen')"
        [personValueItemsSig]="einnahmen.kinderAusbildungszulagen"
        [amountSig]="
          einnahmen.kinderAusbildungszulagenTotal | formatChfPositive
        "
        *dvShowZero="showZero; value: einnahmen.kinderAusbildungszulagenTotal"
      >
      </dv-position>

      <!-- Unterhaltsbeiträge -->
      <dv-position
        [titleSig]="t('unterhaltsbeitraege')"
        [personValueItemsSig]="einnahmen.unterhaltsbeitraege"
        [amountSig]="einnahmen.unterhaltsbeitraegeTotal | formatChfPositive"
        *dvShowZero="showZero; value: einnahmen.unterhaltsbeitraegeTotal"
      >
      </dv-position>

      <!-- EO -->
      <dv-position
        [titleSig]="t('eoLeistungen')"
        [infoSig]="t('eoLeistungen.info')"
        [personValueItemsSig]="einnahmen.eoLeistungen"
        [amountSig]="einnahmen.eoLeistungenTotal | formatChfPositive"
        *dvShowZero="showZero; value: einnahmen.eoLeistungenTotal"
      >
      </dv-position>

      <!-- Taggelder -->
      <dv-position
        [titleSig]="t('taggelderAHVIV')"
        [infoSig]="t('taggelderAHVIV.info')"
        [personValueItemsSig]="einnahmen.taggelderAHVIV"
        [amountSig]="einnahmen.taggelderAHVIVTotal | formatChfPositive"
        *dvShowZero="showZero; value: einnahmen.taggelderAHVIVTotal"
      >
      </dv-position>

      <!-- Renten -->
      <dv-position
        [titleSig]="t('renten')"
        [infoSig]="t('renten.info')"
        [personValueItemsSig]="einnahmen.renten"
        [amountSig]="einnahmen.rentenTotal | formatChfPositive"
        *dvShowZero="showZero; value: einnahmen.rentenTotal"
      >
      </dv-position>

      <!-- Ergänzungsleistungen -->
      <dv-position
        [titleSig]="t('ergaenzungsleistungen')"
        [personValueItemsSig]="einnahmen.ergaenzungsleistungen"
        [amountSig]="einnahmen.ergaenzungsleistungenTotal | formatChfPositive"
        *dvShowZero="showZero; value: einnahmen.ergaenzungsleistungenTotal"
      >
      </dv-position>

      <!-- Beiträge an Gemeindeinstitutionen -->
      <dv-position
        [titleSig]="t('beitraegeGemeindeInstitutionen')"
        [amountSig]="
          einnahmen.beitraegeGemeindeInstitutionen | formatChfPositive
        "
        *dvShowZero="showZero; value: einnahmen.beitraegeGemeindeInstitutionen"
      >
      </dv-position>

      <!-- Andere Einnahmen -->
      <dv-position
        [titleSig]="t('andereEinnahmen')"
        [infoSig]="t('andereEinnahmen.info')"
        [personValueItemsSig]="einnahmen.andereEinnahmen"
        [amountSig]="einnahmen.andereEinnahmenTotal | formatChfPositive"
        *dvShowZero="showZero; value: einnahmen.andereEinnahmenTotal"
      >
      </dv-position>

      <!-- Anrechenbares Vermögen -->
      <dv-position
        [titleSig]="t('anrechenbaresVermoegen')"
        [infoSig]="
          t('anrechenbaresVermoegen.info', {
            vermoegensanteilInProzent:
              stammdatenSig().vermoegensanteilInProzent,
            steuerbaresVermoegen: einnahmen.steuerbaresVermoegen | formatChf,
          })
        "
        [amountSig]="einnahmen.anrechenbaresVermoegen | formatChfPositive"
        *dvShowZero="showZero; value: einnahmen.anrechenbaresVermoegen"
      >
      </dv-position>

      <!-- Elterliche Leistung -->
      <dv-position
        [titleSig]="t('elterlicheLeistung')"
        [amountSig]="einnahmen.elterlicheLeistung | formatChfPositive"
        *dvShowZero="showZero; value: einnahmen.elterlicheLeistung"
      >
      </dv-position>

      <!-- Total -->
      <dv-position
        type="title"
        [titleSig]="t('total')"
        [amountSig]="einnahmen.total | formatChf"
      >
      </dv-position>
    </ng-container>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PersoenlicheEinnahmenComponent {
  showZeroSig = input<boolean>(false);
  budgetSig = input.required<PersoenlichesBudgetresultatView>();
  stammdatenSig = input.required<BerechnungsStammdaten>();
}
