import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { TranslocoDirective } from '@jsverse/transloco';

import { PersoenlichesBudgetresultatView } from '@dv/shared/model/verfuegung';
import {
  SharedUiFormatChfPipe,
  SharedUiFormatChfPositivePipe,
} from '@dv/shared/ui/format-chf-pipe';

import { PositionComponent } from '../position/position.component';

@Component({
  selector: 'dv-persoenliche-kosten',
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
        prefix: 'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten'
      "
    >
      <!-- Ausbildungskosten der/des Auszubildenden -->
      <dv-position
        [titleSig]="t('ausbildungskosten')"
        [amountSig]="kostenSig().ausbildungskosten | formatChfPositive"
      >
      </dv-position>

      <!-- Fahrkosten der/des Auszubildenden -->
      <dv-position
        [titleSig]="t('fahrkosten')"
        [infoSig]="t('nurElternWohnend.info')"
        [amountSig]="kostenSig().fahrkosten | formatChfPositive"
      >
      </dv-position>

      <!-- Mehrkosten für auswärtige Verpflegung -->
      <dv-position
        [titleSig]="t('mehrkostenVerpflegung')"
        [infoSig]="t('nurElternWohnend.info')"
        [amountSig]="kostenSig().verpflegungskosten | formatChfPositive"
      >
      </dv-position>

      <!-- Grundbedarf für 0 Personenhaushalt -->
      <dv-position
        [titleSig]="
          t('grundbedarfPersonen', {
            anzahl: kostenSig().anzahlPersonenImHaushalt,
          })
        "
        [infoSig]="
          t('nurEigenerHaushalt.info', {
            anzahl: kostenSig().anzahlPersonenImHaushalt,
          })
        "
        [amountSig]="kostenSig().grundbedarf | formatChfPositive"
      >
      </dv-position>

      <!-- Wohnkosten für anz Personenhaushalt -->
      <dv-position
        [titleSig]="
          t('wohnkostenPersonen', {
            anzahl: kostenSig().anzahlPersonenImHaushalt,
          })
        "
        [infoSig]="
          t('nurEigenerHaushalt.info', {
            anzahl: kostenSig().anzahlPersonenImHaushalt,
          })
        "
        [amountSig]="kostenSig().wohnkosten | formatChfPositive"
      >
      </dv-position>

      <!-- Medizinische Grundversorgung für anz Personenhaushalt -->
      <dv-position
        [titleSig]="
          t('medizinischeGrundversorgungPersonen', {
            anzahl: kostenSig().anzahlPersonenImHaushalt,
          })
        "
        [infoSig]="
          t('nurEigenerHaushalt.info', {
            anzahl: kostenSig().anzahlPersonenImHaushalt,
          })
        "
        [personValueItemsSig]="kostenSig().medizinischeGrundversorgung"
        [amountSig]="
          kostenSig().medizinischeGrundversorgungTotal | formatChfPositive
        "
      >
      </dv-position>

      <!-- Fahrkosten Ehepartnerin/Ehepartner -->
      <dv-position
        [titleSig]="t('fahrkostenPartner')"
        [infoSig]="t('nurErwerbstaetig.info')"
        [amountSig]="kostenSig().fahrkostenPartner | formatChfPositive"
      >
      </dv-position>

      <!-- Verpflegung Ehepartnerin/Ehepartner -->
      <dv-position
        [titleSig]="t('verpflegungPartner')"
        [infoSig]="t('nurErwerbstaetig.info')"
        [amountSig]="kostenSig().verpflegungPartner | formatChfPositive"
      >
      </dv-position>

      <!-- Betreuungskosten für Kinder -->
      <dv-position
        [titleSig]="t('betreuungskostenKinder')"
        [amountSig]="kostenSig().betreuungskostenKinder | formatChfPositive"
      >
      </dv-position>

      <!-- Kantons- und Gemeindesteuern -->
      <dv-position
        [titleSig]="t('kantonsGemeindesteuern')"
        [amountSig]="kostenSig().kantonsGemeindesteuern | formatChfPositive"
      >
      </dv-position>

      <!-- todo: still here? Kantons- und Gemeindesteuern Ehepartnerin/Ehepartner -->
      <dv-position
        [titleSig]="t('kantonsGemeindesteuernPartner')"
        [amountSig]="kostenSig().kantonsGemeindesteuern | formatChfPositive"
      >
      </dv-position>

      <!-- Bundessteuern -->
      <dv-position
        [titleSig]="t('bundessteuern')"
        [amountSig]="kostenSig().bundessteuern | formatChfPositive"
      >
      </dv-position>

      <!-- Ungedeckter Anteil Lebenshaltungskosten  -->
      <dv-position
        [titleSig]="t('anteilLebenshaltungskosten')"
        [infoSig]="t('anteilLebenshaltungskosten.info')"
        [amountSig]="kostenSig().anteilLebenshaltungskosten | formatChfPositive"
      >
      </dv-position>

      <!-- Total -->
      <div class="tw:flex mt-3 tw:gap-2">
        <div class="h4">
          {{ t('total') }}
        </div>
        <div class="h4 flex-grow-1 text-end text-nowrap">
          {{ kostenSig().total | formatChf }}
        </div>
      </div>

      <!-- Todo: Anpassungen an der Zusammenfassung, siehe Fuss teil excel?  -->
    </ng-container>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PersoenlicheKostenComponent {
  kostenSig = input.required<PersoenlichesBudgetresultatView['kosten']>();
}
