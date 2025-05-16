import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { TranslatePipe } from '@ngx-translate/core';

import {
  SharedUiFormatChfPipe,
  SharedUiFormatChfPositivePipe,
} from '@dv/shared/ui/format-chf-pipe';

import { FamilienBerechnung } from '../../../models';

@Component({
    selector: 'dv-familien-kosten',
    imports: [
        CommonModule,
        TranslatePipe,
        SharedUiFormatChfPipe,
        SharedUiFormatChfPositivePipe,
    ],
    template: `
    <!-- Anzahl anrechenbare Personen -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.anzahlPersonen'
          | translate: kostenSig()
      }}
    </div>

    <!-- Grundbedarf  -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.grundbedarf'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ kostenSig().grundbedarf | formatChfPositive }}
      </div>
    </div>

    <!-- Wohnkosten -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.wohnkosten'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ kostenSig().wohnkosten | formatChfPositive }}
      </div>
    </div>

    <!-- Medizinische Grundversorgung -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.medizinischeGrundversorgung'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ kostenSig().medizinischeGrundversorgung | formatChfPositive }}
      </div>
    </div>

    <!-- lntegrationszulage pro in Ausbildung stehendes Kind -->
    <div class="d-flex gap-5">
      <div classs="d-flex flex-column">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.integrationszulage'
            | translate
        }}
        <div class="text-muted fs-7">
          {{
            'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.integrationszulage.info'
              | translate
          }}
        </div>
      </div>
      <div class="text-muted text-end flex-grow-1 text-nowrap ps-5">
        {{ kostenSig().integrationszulage | formatChfPositive }}
      </div>
    </div>

    <!-- Kantons- und Gemeindesteuern -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.kantonsGemeindesteuern'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ kostenSig().kantonsGemeindesteuern | formatChfPositive }}
      </div>
    </div>

    <!-- Bundessteuern -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.bundessteuern'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ kostenSig().bundessteuern | formatChfPositive }}
      </div>
    </div>

    <!-- Fahrkosten -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.fahrkosten'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ kostenSig().fahrkosten | formatChfPositive }}
      </div>
    </div>

    <!-- Fahrkosten Partner:in -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.fahrkostenPartner'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ kostenSig().fahrkostenPartner | formatChfPositive }}
      </div>
    </div>

    <!-- Verpflegung auswärts -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.verpflegung'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ kostenSig().verpflegung | formatChfPositive }}
      </div>
    </div>

    <!-- Verpflegung auswärts Partner:in -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.verpflegungPartner'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ kostenSig().verpflegungPartner | formatChfPositive }}
      </div>
    </div>

    <!-- Total -->
    <div class="d-flex mt-3 gap-2">
      <div class="h4 m-0">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.total'
            | translate
        }}
      </div>
      <div class=" text-end h4 flex-grow-1 text-nowrap">
        {{ kostenSig().total | formatChf }}
      </div>
    </div>
  `,
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class FamilienKostenComponent {
  kostenSig = input.required<FamilienBerechnung['kosten']>();
}
