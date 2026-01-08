import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { TranslocoDirective } from '@jsverse/transloco';

import { BerechnungsStammdaten } from '@dv/shared/model/gesuch';
import { FamilienBudgetresultatView } from '@dv/shared/model/verfuegung';
import {
  SharedUiFormatChfPipe,
  SharedUiFormatChfPositivePipe,
} from '@dv/shared/ui/format-chf-pipe';

import { PositionComponent } from '../position/position.component';

@Component({
  selector: 'dv-familien-kosten',
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
        prefix: 'sachbearbeitung-app.verfuegung.berechnung.familien.kosten'
      "
    >
      <!-- Grundbedarf  -->
      <dv-position
        [titleSig]="t('grundbedarf')"
        [infoSig]="
          t('anzahlPersonen.info', {
            anzahlPersonen: kostenSig().anzahlPersonenImHaushalt,
          })
        "
        [amountSig]="kostenSig().grundbedarf | formatChfPositive"
      >
      </dv-position>

      <!-- Wohnkosten -->
      <dv-position
        [titleSig]="t('wohnkosten')"
        [infoSig]="
          t('anzahlPersonen.info', {
            anzahlPersonen: kostenSig().anzahlPersonenImHaushalt,
          })
        "
        [amountSig]="kostenSig().wohnkosten | formatChfPositive"
      >
      </dv-position>

      <!-- Medizinische Grundversorgung -->
      <dv-position
        [titleSig]="t('medizinischeGrundversorgung')"
        [infoSig]="
          t('anzahlPersonen.info', {
            anzahlPersonen: kostenSig().anzahlPersonenImHaushalt,
          })
        "
        [amountSig]="
          kostenSig().medizinischeGrundversorgung | formatChfPositive
        "
      >
      </dv-position>

      <!-- lntegrationszulage pro in Ausbildung stehendes Kind -->
      <dv-position
        [titleSig]="
          t('integrationszulage', {
            anzahlKinderInAusbildung: kostenSig().anzahlKinderInAusbildung,
          })
        "
        [infoSig]="
          t('integrationszulage.info', {
            einkommensfreibetrag: stammdatenSig().einkommensfreibetrag,
          })
        "
        [amountSig]="kostenSig().integrationszulage | formatChfPositive"
      >
      </dv-position>

      <!-- Kantons- und Gemeindesteuern -->
      <dv-position
        [titleSig]="t('kantonsGemeindesteuern')"
        [infoSig]="t('steuern.info')"
        [amountSig]="kostenSig().kantonsGemeindesteuern | formatChfPositive"
      >
      </dv-position>

      <!-- Bundessteuern -->
      <dv-position
        [titleSig]="t('bundessteuern')"
        [infoSig]="t('steuern.info')"
        [amountSig]="kostenSig().bundessteuern | formatChfPositive"
      >
      </dv-position>

      <!-- todo: @fabrice, besser Array? - Fahrkosten -->
      <dv-position
        [titleSig]="t('fahrkosten')"
        [infoSig]="t('fahrkosten.info')"
        [amountSig]="kostenSig().fahrkosten | formatChfPositive"
      >
      </dv-position>

      <!-- todo: @fabrice, besser Array? - Verpflegung auswärts -->
      <dv-position
        [titleSig]="t('verpflegung')"
        [infoSig]="t('verpflegung.info')"
        [amountSig]="kostenSig().verpflegung | formatChfPositive"
      >
      </dv-position>

      <!-- Total -->
      <div class="tw:flex mt-3 tw:gap-2">
        <div class="h4 m-0">
          {{ t('total') }}
        </div>
        <div class="h4 flex-grow-1 text-end text-nowrap">
          {{ kostenSig().total | formatChf }}
        </div>
      </div>
    </ng-container>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class FamilienKostenComponent {
  kostenSig = input.required<FamilienBudgetresultatView['kosten']>();
  stammdatenSig = input.required<BerechnungsStammdaten>();
}
