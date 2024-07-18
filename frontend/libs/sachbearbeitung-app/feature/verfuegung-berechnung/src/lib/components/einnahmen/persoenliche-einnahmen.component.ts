import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';

import { SharedUiFormatChfPipe } from '@dv/shared/ui/format-chf-pipe';

import { PersoenlicheBerechnung } from '../../../models';

@Component({
  selector: 'dv-persoenliche-einnahmen',
  standalone: true,
  imports: [CommonModule, TranslateModule, SharedUiFormatChfPipe],
  template: `
    <!-- Nettoerwerbseinkommen -->
    <div class="d-flex">
      <div classs="d-flex flex-column">
        <div class="fs-4">
          {{
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.nettoerwerbseinkommen'
              | translate
          }}
        </div>
        <div class="text-muted">
          {{
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.nettoerwerbseinkommen.info'
              | translate
          }}
        </div>
      </div>
      <div class="text-muted text-end flex-grow-1">
        {{ einnahmenSig().nettoerwerbseinkommen }}
      </div>
    </div>

    <!-- EO -->
    <div class="d-flex">
      <div classs="d-flex flex-column">
        <div class="fs-4">
          {{
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.eoLeistungen'
              | translate
          }}
        </div>
        <div class="text-muted">
          {{
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.eoLeistungen.info'
              | translate
          }}
        </div>
      </div>
      <div class="text-muted text-end flex-grow-1">
        {{ einnahmenSig().eoLeistungen }}
      </div>
    </div>

    <!-- Unterhaltsbeiträge -->
    <div class="d-flex">
      <div class="fs-4">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.unterhaltsbeitraege'
            | translate
        }}
      </div>
      <div class="text-muted text-end flex-grow-1">
        {{ einnahmenSig().unterhaltsbeitraege }}
      </div>
    </div>

    <!-- Kinder- und Ausbildungszulagen -->
    <div class="d-flex">
      <div class="fs-4">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.kinderUndAusbildungszulagen'
            | translate
        }}
      </div>
      <div class="text-muted text-end flex-grow-1">
        {{ einnahmenSig().kinderUndAusbildungszulagen }}
      </div>
    </div>

    <!-- Ergänzungsleistungen -->
    <div class="d-flex">
      <div class="fs-4">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.ergaenzungsleistungen'
            | translate
        }}
      </div>
      <div class="text-muted text-end flex-grow-1">
        {{ einnahmenSig().ergaenzungsleistungen }}
      </div>
    </div>

    <!-- Beiträge an Gemeindeinstitutionen -->
    <div class="d-flex">
      <div class="fs-4">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.beitraegeGemeindeInstitution'
            | translate
        }}
      </div>
      <div class="text-muted text-end flex-grow-1">
        {{ einnahmenSig().beitraegeGemeindeInstitution }}
      </div>
    </div>

    <!-- Steuerbares Vermögen -->
    <div class="d-flex">
      <div class="fs-4">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.steuerbaresVermoegen'
            | translate
        }}
      </div>
      <div class="text-muted text-end flex-grow-1">
        {{ einnahmenSig().steuerbaresVermoegen }}
      </div>
    </div>

    <!-- Elterliche Leistung -->
    <div class="d-flex">
      <div class="fs-4">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.elterlicheLeistung'
            | translate
        }}
      </div>
      <div class="text-muted text-end flex-grow-1">
        {{ einnahmenSig().elterlicheLeistung }}
      </div>
    </div>

    <!-- Einkommen Partner -->
    <div class="d-flex">
      <div class="fs-4">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.einkommenPartner'
            | translate
        }}
      </div>
      <div class="text-muted text-end flex-grow-1">
        {{ einnahmenSig().einkommenPartner }}
      </div>
    </div>

    <!-- Total -->
    <div class="d-flex">
      <div class="h4 mt-3">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.total'
            | translate
        }}
      </div>
      <div class="text-muted text-end flex-grow-1">
        {{ einnahmenSig().total | formatChf }}
      </div>
    </div>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PersoenlicheEinnahmenComponent {
  einnahmenSig = input.required<PersoenlicheBerechnung['einnahmen']>();
}
