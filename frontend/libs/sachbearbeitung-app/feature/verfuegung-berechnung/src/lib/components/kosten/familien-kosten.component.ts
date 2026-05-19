import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { TranslocoDirective } from '@jsverse/transloco';

import { BerechnungsStammdaten } from '@dv/shared/model/gesuch';
import { FamilienBudgetresultatView } from '@dv/shared/model/verfuegung';
import {
  SharedUiFormatChfPipe,
  SharedUiFormatChfPositivePipe,
} from '@dv/shared/ui/format-chf-pipe';
import { SharedUiInfoDialogDirective } from '@dv/shared/ui/info-dialog';

import { HideZeroDirective } from '../../hide-zero.directive';
import { PositionComponent } from '../position/position.component';

@Component({
  selector: 'dv-familien-kosten',
  imports: [
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
        prefix: 'sachbearbeitung-app.verfuegung.berechnung.familien.kosten'
      "
    >
      @let budget = budgetSig();
      @let kosten = budget.kosten;
      @let hideZero = hideZeroSig();
      <!-- Grundbedarf  -->
      <dv-position
        class="tw:border-b tw:border-b-gray-300 tw:py-4"
        [titleSig]="t('grundbedarf')"
        [infoSig]="
          t('anzahlPersonen.info', {
            anzahlPersonen: budget.anzahlPersonenImHaushalt,
          })
        "
        [amountSig]="kosten.grundbedarf | formatChfPositive"
        *dvHideZero="hideZero; value: kosten.grundbedarf"
      >
        <button
          ngProjectAs="title-appendix"
          type="button"
          (click)="hinweishoechstwerte.toggle()"
          class="tw:dv-button-icon tw:h-[unset] tw:text-dv-blue tw:inline tw:align-middle"
          [attr.aria-label]="t('toggle-info-messages')"
        >
          <span class="material-symbols-rounded tw:text-xl!"> info </span>
        </button>
      </dv-position>

      <!-- Wohnkosten -->
      <dv-position
        class="tw:border-b tw:border-b-gray-300 tw:py-4"
        [titleSig]="t('wohnkosten')"
        [infoSig]="
          t('anzahlPersonen.info', {
            anzahlPersonen: budget.anzahlPersonenImHaushalt,
          })
        "
        [amountSig]="kosten.wohnkosten | formatChfPositive"
        *dvHideZero="hideZero; value: kosten.wohnkosten"
      >
        <span ngProjectAs="title-appendix" class="tw:text-xs tw:align-text-top">
          2)
        </span>
      </dv-position>

      <!-- Medizinische Grundversorgung -->
      <dv-position
        class="tw:border-b tw:border-b-gray-300 tw:py-4"
        [titleSig]="t('medizinischeGrundversorgung')"
        [infoSig]="
          t('anzahlPersonen.info', {
            anzahlPersonen: budget.anzahlPersonenImHaushalt,
          })
        "
        [amountSig]="kosten.medizinischeGrundversorgung | formatChfPositive"
        *dvHideZero="hideZero; value: kosten.medizinischeGrundversorgung"
      >
        <button
          ngProjectAs="title-appendix"
          type="button"
          (click)="hinweishoechstwerte.toggle()"
          class="tw:dv-button-icon tw:h-[unset] tw:text-dv-blue tw:inline tw:align-middle"
          [attr.aria-label]="t('toggle-info-messages')"
        >
          <span class="material-symbols-rounded tw:text-xl!"> info </span>
        </button>
      </dv-position>

      <!-- Integrationszulage pro in Ausbildung stehendes Kind -->
      <dv-position
        class="tw:border-b tw:border-b-gray-300 tw:py-4"
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
        *dvHideZero="hideZero; value: kosten.integrationszulageTotal"
      >
        <button
          ngProjectAs="title-appendix"
          type="button"
          (click)="hinweishoechstwerte.toggle()"
          class="tw:dv-button-icon tw:h-[unset] tw:text-dv-blue tw:inline tw:align-middle"
          [attr.aria-label]="t('toggle-info-messages')"
        >
          <span class="material-symbols-rounded tw:text-xl!"> info </span>
        </button>
      </dv-position>

      <!-- Kantons- und Gemeindesteuern -->
      <dv-position
        class="tw:border-b tw:border-b-gray-300 tw:py-4"
        [titleSig]="t('kantonsGemeindesteuern')"
        [infoSig]="t('steuern.info')"
        [amountSig]="kosten.kantonsGemeindesteuern | formatChfPositive"
        *dvHideZero="hideZero; value: kosten.kantonsGemeindesteuern"
      >
      </dv-position>

      <!-- Bundessteuern -->
      <dv-position
        class="tw:border-b tw:border-b-gray-300 tw:py-4"
        [titleSig]="t('bundessteuern')"
        [infoSig]="t('steuern.info')"
        [amountSig]="kosten.bundessteuern | formatChfPositive"
        *dvHideZero="hideZero; value: kosten.bundessteuern"
      >
      </dv-position>

      <!-- Fahrkosten -->
      <dv-position
        class="tw:border-b tw:border-b-gray-300 tw:py-4"
        [titleSig]="t('fahrkosten')"
        [infoSig]="t('fahrkosten.info')"
        [amountSig]="kosten.fahrkostenTotal | formatChfPositive"
        [personValueItemsSig]="kosten.fahrkosten"
        *dvHideZero="hideZero; value: kosten.fahrkostenTotal"
      >
      </dv-position>

      <!-- Verpflegung auswärts -->
      <dv-position
        class="tw:border-b tw:border-b-gray-300 tw:py-4"
        [titleSig]="t('verpflegung')"
        [infoSig]="t('verpflegung.info')"
        [amountSig]="kosten.verpflegungTotal | formatChfPositive"
        [personValueItemsSig]="kosten.verpflegung"
        *dvHideZero="hideZero; value: kosten.verpflegungTotal"
      >
      </dv-position>

      <!-- Total -->
      <dv-position
        class="tw:pt-4"
        type="title"
        [titleSig]="t('total')"
        [amountSig]="kosten.total | formatChf"
      >
      </dv-position>

      <div
        dvSharedUiInfoDialog
        [forceDialogPosition]="true"
        [dialogTitleKey]="'sachbearbeitung-app.verfuegung.berechnung.hinweis.hoechstwerte.title'"
        [dialogMessageKey]="'sachbearbeitung-app.verfuegung.berechnung.hinweis.hoechstwerte.message'"
        #hinweishoechstwerte="dvSharedUiInfoDialog"
        class="tw:hidden"
      ></div>
    </ng-container>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class FamilienKostenComponent {
  hideZeroSig = input<boolean>(false);
  budgetSig = input.required<FamilienBudgetresultatView>();
  stammdatenSig = input.required<BerechnungsStammdaten>();
}
