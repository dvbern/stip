import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { TranslocoPipe } from '@jsverse/transloco';

import {
  SharedUiFormatChfPipe,
  SharedUiFormatChfPositivePipe,
} from '@dv/shared/ui/format-chf-pipe';

import { FamilienBerechnung } from '../../../models';

@Component({
  selector: 'dv-familien-kosten',
  imports: [
    TranslocoPipe,
    SharedUiFormatChfPipe,
    SharedUiFormatChfPositivePipe,
  ],
  template: `
    <!-- Anzahl anrechenbare Personen -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.anzahlPersonen'
          | transloco: kostenSig()
      }}
    </div>

    <!-- Grundbedarf  -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.grundbedarf'
          | transloco
      }}
      <div class="text-muted flex-grow-1 text-end text-nowrap">
        {{ kostenSig().grundbedarf | formatChfPositive }}
      </div>
    </div>

    <!-- Wohnkosten -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.wohnkosten'
          | transloco
      }}
      <div class="text-muted flex-grow-1 text-end text-nowrap">
        {{ kostenSig().wohnkosten | formatChfPositive }}
      </div>
    </div>

    <!-- Medizinische Grundversorgung -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.medizinischeGrundversorgung'
          | transloco
      }}
      <div class="text-muted flex-grow-1 text-end text-nowrap">
        {{ kostenSig().medizinischeGrundversorgung | formatChfPositive }}
      </div>
    </div>

    <!-- lntegrationszulage pro in Ausbildung stehendes Kind -->
    <div class="d-flex gap-5">
      <div classs="d-flex flex-column">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.integrationszulage'
            | transloco
        }}
        <div class="text-muted fs-7">
          {{
            'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.integrationszulage.info'
              | transloco: kostenSig()
          }}
        </div>
      </div>
      <div class="text-muted flex-grow-1 ps-5 text-end text-nowrap">
        {{ kostenSig().integrationszulage | formatChfPositive }}
      </div>
    </div>

    <!-- Kantons- und Gemeindesteuern -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.kantonsGemeindesteuern'
          | transloco
      }}
      <div class="text-muted flex-grow-1 text-end text-nowrap">
        {{ kostenSig().kantonsGemeindesteuern | formatChfPositive }}
      </div>
    </div>

    <!-- Bundessteuern -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.bundessteuern'
          | transloco
      }}
      <div class="text-muted flex-grow-1 text-end text-nowrap">
        {{ kostenSig().bundessteuern | formatChfPositive }}
      </div>
    </div>

    <!-- Fahrkosten -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.fahrkosten'
          | transloco
      }}
      <div class="text-muted flex-grow-1 text-end text-nowrap">
        {{ kostenSig().fahrkosten | formatChfPositive }}
      </div>
    </div>

    <!-- Fahrkosten Partner:in -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.fahrkostenPartner'
          | transloco
      }}
      <div class="text-muted flex-grow-1 text-end text-nowrap">
        {{ kostenSig().fahrkostenPartner | formatChfPositive }}
      </div>
    </div>

    <!-- Verpflegung auswärts -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.verpflegung'
          | transloco
      }}
      <div class="text-muted flex-grow-1 text-end text-nowrap">
        {{ kostenSig().verpflegung | formatChfPositive }}
      </div>
    </div>

    <!-- Verpflegung auswärts Partner:in -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.verpflegungPartner'
          | transloco
      }}
      <div class="text-muted flex-grow-1 text-end text-nowrap">
        {{ kostenSig().verpflegungPartner | formatChfPositive }}
      </div>
    </div>

    <!-- Total -->
    <div class="d-flex mt-3 gap-2">
      <div class="h4 m-0">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.total'
            | transloco
        }}
      </div>
      <div class="h4 flex-grow-1 text-end text-nowrap">
        {{ kostenSig().total | formatChf }}
      </div>
    </div>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class FamilienKostenComponent {
  kostenSig = input.required<FamilienBerechnung['kosten']>();
}
