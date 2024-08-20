import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';

import { SharedUiFormatChfPipe } from '@dv/shared/ui/format-chf-pipe';

import { FamilienBerechnung } from '../../../models';

@Component({
  selector: 'dv-familien-kosten',
  standalone: true,
  imports: [CommonModule, TranslateModule, SharedUiFormatChfPipe],
  template: `
    <!-- Anzahl anrechenbare Personen -->
    <div class="d-flex">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.anzahlPersonen'
          | translate: kostenSig()
      }}
    </div>

    <!-- Grundbedarf  -->
    <div class="d-flex">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.grundbedarf'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1">
        {{ kostenSig().grundbedarf }}
      </div>
    </div>

    <!-- Wohnkosten -->
    <div class="d-flex">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.wohnkosten'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1">
        {{ kostenSig().wohnkosten }}
      </div>
    </div>

    <!-- Medizinische Grundversorgung -->
    <div class="d-flex">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.medizinischeGrundversorgung'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1">
        {{ kostenSig().medizinischeGrundversorgung }}
      </div>
    </div>

    <!-- lntegrationszulage pro in Ausbildung stehendes Kind -->
    <div class="d-flex gap-5">
      <div classs="d-flex flex-column">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.integrationszulage'
            | translate
        }}
        <div class="text-muted">
          {{
            'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.integrationszulage.info'
              | translate
          }}
        </div>
      </div>
      <div class="text-muted text-end flex-grow-1 ps-5">
        {{ kostenSig().integrationszulage }}
      </div>
    </div>

    <!-- Kantons- und Gemeindesteuern -->
    <div class="d-flex">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.kantonsGemeindesteuern'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1">
        {{ kostenSig().kantonsGemeindesteuern }}
      </div>
    </div>

    <!-- Bundessteuern -->
    <div class="d-flex">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.bundessteuern'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1">
        {{ kostenSig().bundessteuern }}
      </div>
    </div>

    <!-- Fahrkosten -->
    <div class="d-flex">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.fahrkosten'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1">
        {{ kostenSig().fahrkosten }}
      </div>
    </div>

    <!-- Fahrkosten Partner:in -->
    <div class="d-flex">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.fahrkostenPartner'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1">
        {{ kostenSig().fahrkostenPartner }}
      </div>
    </div>

    <!-- Verpflegung auswärts -->
    <div class="d-flex">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.verpflegung'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1">
        {{ kostenSig().verpflegung }}
      </div>
    </div>

    <!-- Verpflegung auswärts Partner:in -->
    <div class="d-flex">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.verpflegungPartner'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1">
        {{ kostenSig().verpflegungPartner }}
      </div>
    </div>

    <!-- Total -->
    <div class="d-flex mt-3">
      <div class="h4 m-0">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.familien.kosten.total'
            | translate
        }}
      </div>
      <div class="text-muted text-end flex-grow-1">
        {{ kostenSig().total | formatChf: false }}
      </div>
    </div>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class FamilienKostenComponent {
  kostenSig = input.required<FamilienBerechnung['kosten']>();
}
