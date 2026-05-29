import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  input,
} from '@angular/core';
import { TranslocoDirective } from '@jsverse/transloco';

import { PersoenlichesBudgetresultatView } from '@dv/shared/model/verfuegung';
import {
  SharedUiFormatChfPipe,
  SharedUiFormatChfPositivePipe,
} from '@dv/shared/ui/format-chf-pipe';
import { SharedUiInfoDialogDirective } from '@dv/shared/ui/info-dialog';

import { HideZeroDirective } from '../../hide-zero.directive';
import { PositionComponent } from '../position/position.component';

@Component({
  selector: 'dv-persoenliche-kosten',
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
        prefix: 'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten'
      "
    >
      @let budget = budgetSig();
      @let kosten = budget.kosten;
      @let hideZero = hideZeroSig();

      <!-- Ausbildungskosten der/des Auszubildenden -->
      <dv-position
        class="tw:border-b tw:border-b-gray-300 tw:py-4"
        [titleSig]="t('ausbildungskosten')"
        [infoSig]="
          t('ausbildungskosten.info', {
            ausbildungskosten: kosten.ausbildungskosten | formatChf,
            anzahlPersonenImHaushalt: budget.anzahlPersonenImHaushalt,
          })
        "
        [amountSig]="kosten.ausbildungskostenTotal | formatChfPositive"
        *dvHideZero="hideZero; value: kosten.ausbildungskostenTotal"
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

      <!-- Fahrkosten der/des Auszubildenden -->
      <dv-position
        class="tw:border-b tw:border-b-gray-300 tw:py-4"
        [titleSig]="t('fahrkosten')"
        [infoSig]="
          t('fahrkosten.info', {
            fahrkosten: kosten.fahrkosten | formatChf,
            anzahlPersonenImHaushalt: budget.anzahlPersonenImHaushalt,
          })
        "
        [amountSig]="kosten.fahrkostenTotal | formatChfPositive"
        *dvHideZero="hideZero; value: kosten.fahrkostenTotal"
      >
      </dv-position>

      <!-- Mehrkosten für auswärtige Verpflegung -->
      <dv-position
        class="tw:border-b tw:border-b-gray-300 tw:py-4"
        [titleSig]="t('mehrkostenVerpflegung')"
        [infoSig]="t('nurElternWohnend.info')"
        [amountSig]="kosten.verpflegungskosten | formatChfPositive"
        *dvHideZero="hideZero; value: kosten.verpflegungskosten"
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

      <!-- Grundbedarf für 0 Personenhaushalt -->
      <dv-position
        class="tw:border-b tw:border-b-gray-300 tw:py-4"
        [titleSig]="
          t('grundbedarfPersonen', {
            anzahl: budget.anzahlPersonenImHaushalt,
          })
        "
        [infoSig]="t('nurEigenerHaushalt.info')"
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

      <!-- Wohnkosten für anz Personenhaushalt -->
      <dv-position
        class="tw:border-b tw:border-b-gray-300 tw:py-4"
        [titleSig]="
          t('wohnkostenPersonen', {
            anzahl: budget.anzahlPersonenImHaushalt,
          })
        "
        [infoSig]="t('nurEigenerHaushalt.info')"
        [amountSig]="kosten.wohnkosten | formatChfPositive"
        *dvHideZero="hideZero; value: kosten.wohnkosten"
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

      <!-- Medizinische Grundversorgung für anz Personenhaushalt -->
      <dv-position
        class="tw:border-b tw:border-b-gray-300 tw:py-4"
        [titleSig]="
          t('medizinischeGrundversorgungPersonen', {
            anzahl: budget.anzahlPersonenImHaushalt,
          })
        "
        [infoSig]="t('nurEigenerHaushalt.info')"
        [personValueItemsSig]="kosten.medizinischeGrundversorgung"
        [amountSig]="
          kosten.medizinischeGrundversorgungTotal | formatChfPositive
        "
        *dvHideZero="hideZero; value: kosten.medizinischeGrundversorgungTotal"
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

      <!-- Fahrkosten Ehepartnerin/Ehepartner -->
      <dv-position
        class="tw:border-b tw:border-b-gray-300 tw:py-4"
        [titleSig]="t('fahrkostenPartner')"
        [infoSig]="
          t('fahrkostenPartner.info', {
            fahrkosten: kosten.fahrkostenPartner | formatChf,
            anzahlPersonenImHaushalt: budget.anzahlPersonenImHaushalt,
          })
        "
        [amountSig]="kosten.fahrkostenPartner | formatChfPositive"
        *dvHideZero="hideZero; value: kosten.fahrkostenPartner"
      >
      </dv-position>

      <!-- Verpflegung Ehepartnerin/Ehepartner -->
      <dv-position
        class="tw:border-b tw:border-b-gray-300 tw:py-4"
        [titleSig]="t('verpflegungPartner')"
        [infoSig]="
          t('verpflegungPartner.info', {
            verpflegungPartner: kosten.verpflegungPartner | formatChf,
            anzahlPersonenImHaushalt: budget.anzahlPersonenImHaushalt,
          })
        "
        [amountSig]="kosten.verpflegungPartner | formatChfPositive"
        *dvHideZero="hideZero; value: kosten.verpflegungPartner"
      >
      </dv-position>

      <!-- Betreuungskosten für Kinder -->
      <dv-position
        class="tw:border-b tw:border-b-gray-300 tw:py-4"
        [titleSig]="t('betreuungskostenKinder')"
        [amountSig]="kosten.betreuungskostenKinder | formatChfPositive"
        *dvHideZero="hideZero; value: kosten.betreuungskostenKinder"
      >
      </dv-position>

      <!-- steuern -->
      <dv-position
        class="tw:border-b tw:border-b-gray-300 tw:py-4"
        [titleSig]="t('steuern')"
        [infoSig]="t('steuern.info')"
        [amountSig]="kosten.steuern | formatChfPositive"
        *dvHideZero="hideZero; value: kosten.steuern"
      >
      </dv-position>

      <!-- Ungedeckter Anteil Lebenshaltungskosten  -->
      <dv-position
        class="tw:border-b tw:border-b-gray-300 tw:py-4"
        [titleSig]="t('anteilLebenshaltungskosten')"
        [infoSig]="t('anteilLebenshaltungskosten.info')"
        [amountSig]="kosten.anteilLebenshaltungskosten | formatChfPositive"
        *dvHideZero="hideZero; value: kosten.anteilLebenshaltungskosten"
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
export class PersoenlicheKostenComponent {
  @HostBinding('class') classes = 'tw:dv-verfuegung-position-list';
  hideZeroSig = input<boolean>(false);
  budgetSig = input.required<PersoenlichesBudgetresultatView>();
}
