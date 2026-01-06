import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { TranslocoDirective } from '@jsverse/transloco';

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
    <ng-container *transloco="let t">
      <!-- todo: add anz. personen Grundbedarf  -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.grundbedarf'
          )
        "
        [amountSig]="kostenSig().grundbedarf | formatChfPositive"
      >
      </dv-position>

      <!-- todo: add anz. personen Wohnkosten -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.wohnkosten'
          )
        "
        [amountSig]="kostenSig().wohnkosten | formatChfPositive"
      >
      </dv-position>

      <!-- todo: add anz. personen Medizinische Grundversorgung -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.medizinischeGrundversorgung'
          )
        "
        [amountSig]="
          kostenSig().medizinischeGrundversorgung | formatChfPositive
        "
      >
      </dv-position>

      <!-- lntegrationszulage pro in Ausbildung stehendes Kind -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.integrationszulage'
          )
        "
        [infoSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.integrationszulage.info',
            kostenSig()
          )
        "
        [amountSig]="kostenSig().integrationszulage | formatChfPositive"
      >
      </dv-position>

      <!-- Kantons- und Gemeindesteuern -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.kantonsGemeindesteuern'
          )
        "
        [amountSig]="kostenSig().kantonsGemeindesteuern | formatChfPositive"
      >
      </dv-position>

      <!-- Bundessteuern -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.bundessteuern'
          )
        "
        [amountSig]="kostenSig().bundessteuern | formatChfPositive"
      >
      </dv-position>

      <!-- todo: @fabrice, besser Array? - Fahrkosten -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.fahrkosten'
          )
        "
        [amountSig]="kostenSig().fahrkosten | formatChfPositive"
      >
      </dv-position>

      <!-- todo: @fabrice, besser Array? - Fahrkosten Partner:in -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.fahrkostenPartner'
          )
        "
        [amountSig]="kostenSig().fahrkostenPartner | formatChfPositive"
      >
      </dv-position>

      <!-- todo: @fabrice, besser Array? - Verpflegung auswärts -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.verpflegung'
          )
        "
        [amountSig]="kostenSig().verpflegung | formatChfPositive"
      >
      </dv-position>

      <!-- todo: @fabrice, besser Array? - Verpflegung auswärts Partner:in -->
      <dv-position
        [titleSig]="
          t(
            'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.verpflegungPartner'
          )
        "
        [amountSig]="kostenSig().verpflegungPartner | formatChfPositive"
      >
      </dv-position>

      <!-- Total -->
      <div class="tw:flex mt-3 tw:gap-2">
        <div class="h4 m-0">
          {{
            t('sachbearbeitung-app.verfuegung.berechnung.familien.kosten.total')
          }}
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
}
