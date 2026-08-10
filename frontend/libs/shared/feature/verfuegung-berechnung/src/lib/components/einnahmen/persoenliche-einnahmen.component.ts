import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { TranslocoDirective } from '@jsverse/transloco';

import { BerechnungsStammdaten } from '@dv/shared/model/gesuch';
import { PersoenlichesBudgetresultatView } from '@dv/shared/model/verfuegung';
import {
  SharedUiFormatChfPipe,
  SharedUiFormatChfPositivePipe,
} from '@dv/shared/ui/format-chf-pipe';
import { SharedUiInfoDialogDirective } from '@dv/shared/ui/info-dialog';

import { HideZeroDirective } from '../../hide-zero.directive';
import { PositionComponent } from '../position/position.component';

@Component({
  selector: 'dv-persoenliche-einnahmen',
  imports: [
    CommonModule,
    TranslocoDirective,
    SharedUiFormatChfPipe,
    SharedUiFormatChfPositivePipe,
    PositionComponent,
    HideZeroDirective,
    SharedUiInfoDialogDirective,
  ],
  template: `
    <ng-container
      *transloco="
        let t;
        prefix: 'shared.verfuegung.berechnung.persoenlich.einnahmen'
      "
    >
      @let einnahmen = budgetSig().einnahmen;
      @let hideZero = hideZeroSig();

      <!-- Nettoerwerbseinkommen -->
      <dv-position
        class="tw:border-b tw:border-b-gray-300 tw:py-4"
        [titleSig]="t('nettoerwerbseinkommen')"
        [infoSig]="
          t('nettoerwerbseinkommen.info', {
            freibetragErwerbseinkommen:
              stammdatenSig().freibetragErwerbseinkommen | formatChf,
          })
        "
        [personValueItemsSig]="einnahmen.nettoerwerbseinkommen"
        [amountSig]="einnahmen.nettoerwerbseinkommenTotal | formatChfPositive"
        *dvHideZero="hideZero; value: einnahmen.nettoerwerbseinkommenTotal"
      >
      </dv-position>

      <!-- BGSA -->
      <dv-position
        class="tw:border-b tw:border-b-gray-300 tw:py-4"
        [titleSig]="t('einnahmenBGSA')"
        [amountSig]="einnahmen.einnahmenBGSATotal | formatChfPositive"
        *dvHideZero="hideZero; value: einnahmen.einnahmenBGSATotal"
      >
        <button
          ngProjectAs="title-appendix"
          type="button"
          (click)="hinweisBGSADialog.toggle()"
          class="tw:dv-button-icon tw:h-[unset] tw:text-dv-blue tw:inline tw:align-middle"
          [attr.aria-label]="t('toggle-info-messages')"
        >
          <span class="material-symbols-rounded tw:text-xl!"> info </span>
        </button>
      </dv-position>

      <!-- Kinder- und Ausbildungszulagen -->
      <dv-position
        class="tw:border-b tw:border-b-gray-300 tw:py-4"
        [titleSig]="t('kinderUndAusbildungszulagen')"
        [personValueItemsSig]="einnahmen.kinderAusbildungszulagen"
        [amountSig]="
          einnahmen.kinderAusbildungszulagenTotal | formatChfPositive
        "
        *dvHideZero="hideZero; value: einnahmen.kinderAusbildungszulagenTotal"
      >
      </dv-position>

      <!-- Unterhaltsbeiträge -->
      <dv-position
        class="tw:border-b tw:border-b-gray-300 tw:py-4"
        [titleSig]="t('unterhaltsbeitraege')"
        [personValueItemsSig]="einnahmen.unterhaltsbeitraege"
        [amountSig]="einnahmen.unterhaltsbeitraegeTotal | formatChfPositive"
        *dvHideZero="hideZero; value: einnahmen.unterhaltsbeitraegeTotal"
      >
      </dv-position>

      <!-- EO -->
      <dv-position
        class="tw:border-b tw:border-b-gray-300 tw:py-4"
        [titleSig]="t('eoLeistungen')"
        [infoSig]="t('eoLeistungen.info')"
        [personValueItemsSig]="einnahmen.eoLeistungen"
        [amountSig]="einnahmen.eoLeistungenTotal | formatChfPositive"
        *dvHideZero="hideZero; value: einnahmen.eoLeistungenTotal"
      >
      </dv-position>

      <!-- Taggelder -->
      <dv-position
        class="tw:border-b tw:border-b-gray-300 tw:py-4"
        [titleSig]="t('taggelderAHVIV')"
        [infoSig]="t('taggelderAHVIV.info')"
        [personValueItemsSig]="einnahmen.taggelderAHVIV"
        [amountSig]="einnahmen.taggelderAHVIVTotal | formatChfPositive"
        *dvHideZero="hideZero; value: einnahmen.taggelderAHVIVTotal"
      >
      </dv-position>

      <!-- Renten -->
      <dv-position
        class="tw:border-b tw:border-b-gray-300 tw:py-4"
        [titleSig]="t('renten')"
        [infoSig]="t('renten.info')"
        [personValueItemsSig]="einnahmen.renten"
        [amountSig]="einnahmen.rentenTotal | formatChfPositive"
        *dvHideZero="hideZero; value: einnahmen.rentenTotal"
      >
      </dv-position>

      <!-- Ergänzungsleistungen -->
      <dv-position
        class="tw:border-b tw:border-b-gray-300 tw:py-4"
        [titleSig]="t('ergaenzungsleistungen')"
        [personValueItemsSig]="einnahmen.ergaenzungsleistungen"
        [amountSig]="einnahmen.ergaenzungsleistungenTotal | formatChfPositive"
        *dvHideZero="hideZero; value: einnahmen.ergaenzungsleistungenTotal"
      >
      </dv-position>

      <!-- Beiträge an Gemeindeinstitutionen -->
      <dv-position
        class="tw:border-b tw:border-b-gray-300 tw:py-4"
        [titleSig]="t('beitraegeGemeindeInstitutionen')"
        [amountSig]="
          einnahmen.beitraegeGemeindeInstitutionen | formatChfPositive
        "
        *dvHideZero="hideZero; value: einnahmen.beitraegeGemeindeInstitutionen"
      >
      </dv-position>

      <!-- Andere Einnahmen -->
      <dv-position
        class="tw:border-b tw:border-b-gray-300 tw:py-4"
        [titleSig]="t('andereEinnahmen')"
        [infoSig]="t('andereEinnahmen.info')"
        [personValueItemsSig]="einnahmen.andereEinnahmen"
        [amountSig]="einnahmen.andereEinnahmenTotal | formatChfPositive"
        *dvHideZero="hideZero; value: einnahmen.andereEinnahmenTotal"
      >
      </dv-position>

      <!-- Anrechenbares Vermögen -->
      <dv-position
        class="tw:border-b tw:border-b-gray-300 tw:py-4"
        [titleSig]="t('anrechenbaresVermoegen')"
        [infoSig]="
          t('anrechenbaresVermoegen.info', {
            vermoegensanteilInProzent:
              stammdatenSig().vermoegensanteilInProzent,
            steuerbaresVermoegen: einnahmen.steuerbaresVermoegen | formatChf,
          })
        "
        [amountSig]="einnahmen.anrechenbaresVermoegen | formatChfPositive"
        *dvHideZero="hideZero; value: einnahmen.anrechenbaresVermoegen"
      >
      </dv-position>

      <!-- Elterliche Leistung -->
      <dv-position
        class="tw:border-b tw:border-b-gray-300 tw:py-4"
        [titleSig]="t('elterlicheLeistung')"
        [amountSig]="einnahmen.elterlicheLeistung | formatChfPositive"
        *dvHideZero="hideZero; value: einnahmen.elterlicheLeistung"
      >
      </dv-position>

      <!-- Total -->
      <dv-position
        class="tw:pt-4"
        type="title"
        [titleSig]="t('total')"
        [amountSig]="einnahmen.total | formatChf"
      >
      </dv-position>

      <div
        dvSharedUiInfoDialog
        [forceDialogPosition]="true"
        [dialogTitleKeySig]="'shared.verfuegung.berechnung.hinweis.schwarzarbeitBGSA.title'"
        [dialogMessageKeySig]="'shared.verfuegung.berechnung.hinweis.schwarzarbeitBGSA.message'"
        #hinweisBGSADialog="dvSharedUiInfoDialog"
        class="tw:hidden"
      ></div>
    </ng-container>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PersoenlicheEinnahmenComponent {
  hideZeroSig = input<boolean>(false);
  budgetSig = input.required<PersoenlichesBudgetresultatView>();
  stammdatenSig = input.required<BerechnungsStammdaten>();
}
