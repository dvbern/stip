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
    <ng-container *transloco="let t">
      <!-- Nettoerwerbseinkommen -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.nettoerwerbseinkommen'
          )
        "
        [infoSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.nettoerwerbseinkommen.info',
            {
              freibetragErwerbseinkommen:
                stammdatenSig().freibetragErwerbseinkommen | formatChf,
            }
          )
        "
        [personValueItemsSig]="einnahmenSig().nettoerwerbseinkommen"
        [amountSig]="
          einnahmenSig().nettoerwerbseinkommenTotal | formatChfPositive
        "
      >
      </dv-position>

      <!-- BGSA -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.einnahmenBGSA'
          )
        "
        [personValueItemsSig]="einnahmenSig().einnahmenBGSA"
        [amountSig]="einnahmenSig().einnahmenBGSATotal | formatChfPositive"
      >
        <a href="#1" class="tw:text-xs tw:align-text-top tw:no-underline!">
          1)
        </a>
      </dv-position>

      <!-- Kinder- und Ausbildungszulagen -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.kinderUndAusbildungszulagen'
          )
        "
        [personValueItemsSig]="einnahmenSig().kinderAusbildungszulagen"
        [amountSig]="
          einnahmenSig().kinderAusbildungszulagenTotal | formatChfPositive
        "
      >
      </dv-position>

      <!-- Unterhaltsbeiträge -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.unterhaltsbeitraege'
          )
        "
        [personValueItemsSig]="einnahmenSig().unterhaltsbeitraege"
        [amountSig]="
          einnahmenSig().unterhaltsbeitraegeTotal | formatChfPositive
        "
      >
      </dv-position>

      <!-- EO -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.eoLeistungen'
          )
        "
        [infoSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.eoLeistungen.info'
          )
        "
        [personValueItemsSig]="einnahmenSig().eoLeistungen"
        [amountSig]="einnahmenSig().eoLeistungenTotal | formatChfPositive"
      >
      </dv-position>

      <!-- Taggelder -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.taggelderAHVIV'
          )
        "
        [personValueItemsSig]="einnahmenSig().taggelderAHVIV"
        [amountSig]="einnahmenSig().taggelderAHVIVTotal | formatChfPositive"
      >
      </dv-position>

      <!-- Alimente/Renten -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.renten'
          )
        "
        [personValueItemsSig]="einnahmenSig().renten"
        [amountSig]="einnahmenSig().rentenTotal | formatChfPositive"
      >
      </dv-position>

      <!-- Ergänzungsleistungen -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.ergaenzungsleistungen'
          )
        "
        [personValueItemsSig]="einnahmenSig().ergaenzungsleistungen"
        [amountSig]="
          einnahmenSig().ergaenzungsleistungenTotal | formatChfPositive
        "
      >
      </dv-position>

      <!-- Beiträge an Gemeindeinstitutionen -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.beitraegeGemeindeInstitutionen'
          )
        "
        [amountSig]="
          einnahmenSig().beitraegeGemeindeInstitutionen | formatChfPositive
        "
      >
      </dv-position>

      <!-- Andere Einnahmen -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.andereEinnahmen'
          )
        "
        [infoSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.andereEinnahmen.info'
          )
        "
        [personValueItemsSig]="einnahmenSig().andereEinnahmen"
        [amountSig]="einnahmenSig().andereEinnahmenTotal | formatChfPositive"
      >
      </dv-position>

      <!-- Anrechenbares Vermögen -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.anrechenbaresVermoegen'
          )
        "
        [infoSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.anrechenbaresVermoegen.info',
            einnahmenSig()
          )
        "
        [amountSig]="einnahmenSig().anrechenbaresVermoegen | formatChfPositive"
      >
      </dv-position>

      <!-- Elterliche Leistung -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.elterlicheLeistung'
          )
        "
        [amountSig]="einnahmenSig().elterlicheLeistung | formatChfPositive"
      >
      </dv-position>

      <!-- Total -->
      <div class="tw:flex mt-3 tw:gap-2">
        <div class="h4">
          {{
            t(
              'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.total'
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
export class PersoenlicheEinnahmenComponent {
  einnahmenSig = input.required<PersoenlichesBudgetresultatView['einnahmen']>();
  stammdatenSig = input.required<BerechnungsStammdaten>();
}
