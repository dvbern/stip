import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { TranslatePipe } from '@ngx-translate/core';

import {
  SharedUiFormatChfPipe,
  SharedUiFormatChfPositivePipe,
} from '@dv/shared/ui/format-chf-pipe';

import { PersoenlicheBerechnung } from '../../../models';

@Component({
  selector: 'dv-persoenliche-einnahmen',
  standalone: true,
  imports: [
    CommonModule,
    TranslatePipe,
    SharedUiFormatChfPipe,
    SharedUiFormatChfPositivePipe,
  ],
  template: `
    <!-- Nettoerwerbseinkommen -->
    <div class="d-flex gap-2">
      <div classs="d-flex flex-column">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.nettoerwerbseinkommen'
            | translate
        }}
        <div class="text-muted fs-7">
          {{
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.nettoerwerbseinkommen.info'
              | translate
          }}
        </div>
      </div>
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ einnahmenSig().nettoerwerbseinkommen | formatChfPositive }}
      </div>
    </div>

    <!-- EO -->
    <div class="d-flex gap-2">
      <div classs="d-flex flex-column">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.eoLeistungen'
            | translate
        }}
        <div class="text-muted fs-7">
          {{
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.eoLeistungen.info'
              | translate
          }}
        </div>
      </div>
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ einnahmenSig().eoLeistungen | formatChfPositive }}
      </div>
    </div>

    <!-- Alimente -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.alimente'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ einnahmenSig().alimente | formatChfPositive }}
      </div>
    </div>

    <!-- Unterhaltsbeiträge -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.unterhaltsbeitraege'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ einnahmenSig().unterhaltsbeitraege | formatChfPositive }}
      </div>
    </div>

    <!-- Kinder- und Ausbildungszulagen -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.kinderUndAusbildungszulagen'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ einnahmenSig().kinderUndAusbildungszulagen | formatChfPositive }}
      </div>
    </div>

    <!-- Ergänzungsleistungen -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.ergaenzungsleistungen'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ einnahmenSig().ergaenzungsleistungen | formatChfPositive }}
      </div>
    </div>

    <!-- Beiträge an Gemeindeinstitutionen -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.beitraegeGemeindeInstitution'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ einnahmenSig().beitraegeGemeindeInstitution | formatChfPositive }}
      </div>
    </div>

    <!-- Anrechenbares Vermögen -->
    <div class="d-flex gap-2">
      <div classs="d-flex flex-column">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.anrechenbaresVermoegen'
            | translate
        }}
        <div class="text-muted fs-7">
          {{
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.anrechenbaresVermoegen.info'
              | translate: einnahmenSig()
          }}
        </div>
      </div>
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ einnahmenSig().anrechenbaresVermoegen | formatChfPositive }}
      </div>
    </div>

    <!-- Einkommen Partner -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.einkommenPartner'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ einnahmenSig().einkommenPartner | formatChfPositive }}
      </div>
    </div>

    <!-- Elterliche Leistung -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.elterlicheLeistung'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ einnahmenSig().elterlicheLeistung | formatChfPositive }}
      </div>
    </div>

    <!-- Total -->
    <div class="mt-3 d-flex gap-2">
      <div class="h4">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.total'
            | translate
        }}
      </div>
      <div class="h4 text-end flex-grow-1 text-nowrap">
        {{ einnahmenSig().total | formatChf }}
      </div>
    </div>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PersoenlicheEinnahmenComponent {
  einnahmenSig = input.required<PersoenlicheBerechnung['einnahmen']>();
}
