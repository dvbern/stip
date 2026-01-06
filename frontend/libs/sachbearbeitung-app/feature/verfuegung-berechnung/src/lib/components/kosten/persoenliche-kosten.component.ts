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
    <ng-container *transloco="let t">
      <!-- Ausbildungskosten der/des Auszubildenden -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.ausbildungskosten'
          )
        "
        [amountSig]="kostenSig().ausbildungskosten | formatChfPositive"
      >
      </dv-position>

      <!-- Fahrkosten der/des Auszubildenden -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.fahrkosten'
          )
        "
        [infoSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.nurElternWohnend.info'
          )
        "
        [amountSig]="kostenSig().fahrkosten | formatChfPositive"
      >
      </dv-position>

      <!-- Mehrkosten für auswärtige Verpflegung -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.mehrkostenVerpflegung'
          )
        "
        [infoSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.nurElternWohnend.info'
          )
        "
        [amountSig]="kostenSig().verpflegungskosten | formatChfPositive"
      >
      </dv-position>

      <!-- Grundbedarf für 0 Personenhaushalt -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.grundbedarfPersonen',
            { anzahl: kostenSig().anzahlPersonenImHaushalt }
          )
        "
        [infoSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.nurEigenerHaushalt.info',
            { anzahl: kostenSig().anzahlPersonenImHaushalt }
          )
        "
        [amountSig]="kostenSig().grundbedarf | formatChfPositive"
      >
      </dv-position>

      <!-- Wohnkosten für anz Personenhaushalt -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.wohnkostenPersonen',
            { anzahl: kostenSig().anzahlPersonenImHaushalt }
          )
        "
        [infoSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.nurEigenerHaushalt.info',
            { anzahl: kostenSig().anzahlPersonenImHaushalt }
          )
        "
        [amountSig]="kostenSig().wohnkosten | formatChfPositive"
      >
      </dv-position>

      <!-- Medizinische Grundversorgung für anz Personenhaushalt -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.medizinischeGrundversorgungPersonen',
            { anzahl: kostenSig().anzahlPersonenImHaushalt }
          )
        "
        [infoSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.nurEigenerHaushalt.info',
            { anzahl: kostenSig().anzahlPersonenImHaushalt }
          )
        "
        [personValueItemsSig]="kostenSig().medizinischeGrundversorgung"
        [amountSig]="
          kostenSig().medizinischeGrundversorgungTotal | formatChfPositive
        "
      >
      </dv-position>

      <!-- Fahrkosten Ehepartnerin/Ehepartner -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.fahrkostenPartner'
          )
        "
        [infoSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.nurErwerbstaetig.info'
          )
        "
        [amountSig]="kostenSig().fahrkostenPartner | formatChfPositive"
      >
      </dv-position>

      <!-- Verpflegung Ehepartnerin/Ehepartner -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.verpflegungPartner'
          )
        "
        [infoSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.nurErwerbstaetig.info'
          )
        "
        [amountSig]="kostenSig().verpflegungPartner | formatChfPositive"
      >
      </dv-position>

      <!-- Betreuungskosten für Kinder -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.betreuungskostenKinder'
          )
        "
        [amountSig]="kostenSig().betreuungskostenKinder | formatChfPositive"
      >
      </dv-position>

      <!-- Kantons- und Gemeindesteuern -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.kantonsGemeindesteuern'
          )
        "
        [amountSig]="kostenSig().kantonsGemeindesteuern | formatChfPositive"
      >
      </dv-position>

      <!-- todo: still here? Kantons- und Gemeindesteuern Ehepartnerin/Ehepartner -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.kantonsGemeindesteuernPartner'
          )
        "
        [amountSig]="kostenSig().kantonsGemeindesteuern | formatChfPositive"
      >
      </dv-position>

      <!-- Bundessteuern -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.bundessteuern'
          )
        "
        [amountSig]="kostenSig().bundessteuern | formatChfPositive"
      >
      </dv-position>

      <!-- Ungedeckter Anteil Lebenshaltungskosten  -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.anteilLebenshaltungskosten'
          )
        "
        [infoSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.anteilLebenshaltungskosten.info'
          )
        "
        [amountSig]="kostenSig().anteilLebenshaltungskosten | formatChfPositive"
      >
      </dv-position>

      <!-- Total -->
      <div class="tw:flex mt-3 tw:gap-2">
        <div class="h4">
          {{
            t(
              'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.total'
            )
          }}
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
