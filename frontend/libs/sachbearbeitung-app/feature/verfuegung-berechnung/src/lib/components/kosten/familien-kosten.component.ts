import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { TranslocoDirective } from '@jsverse/transloco';

import { BerechnungsStammdaten } from '@dv/shared/model/gesuch';
import { FamilienBudgetresultatView } from '@dv/shared/model/verfuegung';
import {
  SharedUiFormatChfPipe,
  SharedUiFormatChfPositivePipe,
} from '@dv/shared/ui/format-chf-pipe';

import { ShowZeroDirective } from '../../show-zero.directive';
import { PositionComponent } from '../position/position.component';

@Component({
  selector: 'dv-familien-kosten',
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
        prefix: 'sachbearbeitung-app.verfuegung.berechnung.familien.kosten'
      "
    >
      @let budget = budgetSig();
      @let kosten = budget.kosten;
      @let showZero = showZeroSig();
      <!-- Grundbedarf  -->
      <dv-position
        [titleSig]="t('grundbedarf')"
        [infoSig]="
          t('anzahlPersonen.info', {
            anzahlPersonen: budget.anzahlPersonenImHaushalt,
          })
        "
        [amountSig]="kosten.grundbedarf | formatChfPositive"
        *dvShowZero="showZero; value: kosten.grundbedarf"
      >
        <span ngProjectAs="title-appendix" class="tw:text-xs tw:align-text-top">
          2)
        </span>
      </dv-position>

      <!-- Wohnkosten -->
      <dv-position
        [titleSig]="t('wohnkosten')"
        [infoSig]="
          t('anzahlPersonen.info', {
            anzahlPersonen: budget.anzahlPersonenImHaushalt,
          })
        "
        [amountSig]="kosten.wohnkosten | formatChfPositive"
        *dvShowZero="showZero; value: kosten.wohnkosten"
      >
        <span ngProjectAs="title-appendix" class="tw:text-xs tw:align-text-top">
          2)
        </span>
      </dv-position>

      <!-- Medizinische Grundversorgung -->
      <dv-position
        [titleSig]="t('medizinischeGrundversorgung')"
        [infoSig]="
          t('anzahlPersonen.info', {
            anzahlPersonen: budget.anzahlPersonenImHaushalt,
          })
        "
        [amountSig]="kosten.medizinischeGrundversorgung | formatChfPositive"
        *dvShowZero="showZero; value: kosten.medizinischeGrundversorgung"
      >
        <span ngProjectAs="title-appendix" class="tw:text-xs tw:align-text-top">
          2)
        </span>
      </dv-position>

      <!-- Integrationszulage pro in Ausbildung stehendes Kind -->
      <dv-position
        [titleSig]="
          t('integrationszulage', {
            integrationszulageAnzahl: kosten.integrationszulageAnzahl,
          })
        "
        [infoSig]="
          t('integrationszulage.info', {
            abzugslimite: stammdatenSig().abzugslimite | formatChf,
          })
        "
        [amountSig]="kosten.integrationszulageTotal | formatChfPositive"
        *dvShowZero="showZero; value: kosten.integrationszulageTotal"
      >
        <span ngProjectAs="title-appendix" class="tw:text-xs tw:align-text-top">
          2)
        </span>
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

      <!-- Fahrkosten -->
      <dv-position
        [titleSig]="t('fahrkosten')"
        [infoSig]="t('fahrkosten.info')"
        [amountSig]="kosten.fahrkostenTotal | formatChfPositive"
        [personValueItemsSig]="kosten.fahrkosten"
        *dvShowZero="showZero; value: kosten.fahrkostenTotal"
      >
      </dv-position>

      <!-- Verpflegung auswärts -->
      <dv-position
        [titleSig]="t('verpflegung')"
        [infoSig]="t('verpflegung.info')"
        [amountSig]="kosten.verpflegungTotal | formatChfPositive"
        [personValueItemsSig]="kosten.verpflegung"
        *dvShowZero="showZero; value: kosten.verpflegungTotal"
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
export class FamilienKostenComponent {
  showZeroSig = input<boolean>(false);
  budgetSig = input.required<FamilienBudgetresultatView>();
  stammdatenSig = input.required<BerechnungsStammdaten>();
}
