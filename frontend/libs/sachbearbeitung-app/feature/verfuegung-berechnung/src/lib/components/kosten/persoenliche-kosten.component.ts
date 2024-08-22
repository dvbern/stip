import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';

import { SharedUiFormatChfPipe } from '@dv/shared/ui/format-chf-pipe';

import { PersoenlicheBerechnung } from '../../../models';

@Component({
  selector: 'dv-persoenliche-kosten',
  standalone: true,
  imports: [CommonModule, TranslateModule, SharedUiFormatChfPipe],
  template: `
    <!-- Section Eltern Wohnend -->
    <div class="d-flex gap-2">
      <div classs="d-flex flex-column">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.title'
            | translate
        }}
        <div class="text-muted">
          {{
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.infoElternWohnend'
              | translate
          }}
        </div>
      </div>
      <div class="text-muted text-end flex-grow-1"></div>
    </div>

    <!-- Ungedeckter Anteil Lebenshaltungskosten  -->
    <div class="d-flex gap-2">
      <div classs="d-flex flex-column">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.anteilLebenshaltungskosten'
            | translate
        }}
        <div class="text-muted">
          {{
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.anteilLebenshaltungskosten.info'
              | translate
          }}
        </div>
      </div>
      <div class="text-muted text-end flex-grow-1">
        {{ kostenSig().anteilLebenshaltungskosten }}
      </div>
    </div>

    <!-- Mehrkosten für auswärtige Verpflegung -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.mehrkostenVerpflegung'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1">
        {{ kostenSig().mehrkostenVerpflegung }}
      </div>
    </div>

    <!-- Section eigener Wohnsitz -->
    <div class="d-flex gap-2">
      <div classs="d-flex flex-column">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.title'
            | translate
        }}
        <div class="text-muted">
          {{
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.infoEigenerWohnsitz'
              | translate
          }}
        </div>
      </div>
      <div class="text-muted text-end flex-grow-1"></div>
    </div>

    <!-- Grundbedarf für 0 Personenhaushalt -->
    <div class="d-flex gap-2">
      <div classs="d-flex flex-column">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.grundbedarfPersonen'
            | translate: { anzahl: kostenSig().anzahlPersonenImHaushalt }
        }}
        <div class="text-muted">
          {{
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.grundbedarfPersonen.info'
              | translate: { anzahl: kostenSig().anzahlPersonenImHaushalt }
          }}
        </div>
      </div>
      <div class="text-muted text-end flex-grow-1">
        {{ kostenSig().grundbedarfPersonen }}
      </div>
    </div>

    <!-- Wohnkosten für 0 Personenhaushalt -->
    <div class="d-flex gap-2">
      <div classs="d-flex flex-column">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.wohnkostenPersonen'
            | translate: { anzahl: kostenSig().anzahlPersonenImHaushalt }
        }}
        <div class="text-muted">
          {{
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.wohnkostenPersonen.info'
              | translate: { anzahl: kostenSig().anzahlPersonenImHaushalt }
          }}
        </div>
      </div>
      <div class="text-muted text-end flex-grow-1">
        {{ kostenSig().wohnkostenPersonen }}
      </div>
    </div>

    <!-- Medizinische Grundversorgung für 0 Personenhaushalt -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.medizinischeGrundversorgungPersonen'
          | translate: { anzahl: kostenSig().anzahlPersonenImHaushalt }
      }}
      <div class="text-muted text-end flex-grow-1">
        {{ kostenSig().medizinischeGrundversorgungPersonen }}
      </div>
    </div>

    <!-- Kantons- und Gemeindesteuern -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.kantonsGemeindesteuern'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1">
        {{ kostenSig().kantonsGemeindesteuern }}
      </div>
    </div>

    <!-- Bundessteuern -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.bundessteuern'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1">
        {{ kostenSig().bundessteuern }}
      </div>
    </div>

    <!-- Fahrkosten Ehepartnerin/Ehepartner -->
    <div class="d-flex gap-2">
      <div classs="d-flex flex-column">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.fahrkostenPartner'
            | translate
        }}
        <div class="text-muted">
          {{
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.fahrkostenPartner.info'
              | translate
          }}
        </div>
      </div>
      <div class="text-muted text-end flex-grow-1">
        {{ kostenSig().fahrkostenPartner }}
      </div>
    </div>

    <!-- Verpflegung Ehepartnerin/Ehepartner -->
    <div class="d-flex gap-2">
      <div classs="d-flex flex-column">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.verpflegungPartner'
            | translate
        }}
        <div class="text-muted">
          {{
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.verpflegungPartner.info'
              | translate
          }}
        </div>
      </div>
      <div class="text-muted text-end flex-grow-1">
        {{ kostenSig().verpflegungPartner }}
      </div>
    </div>

    <!-- Betreuungskosten für Kinder -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.betreuungskostenKinder'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1">
        {{ kostenSig().betreuungskostenKinder }}
      </div>
    </div>

    <!-- Ausbildungskosten der/des Auszubildenden -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.ausbildungskosten'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1">
        {{ kostenSig().ausbildungskosten }}
      </div>
    </div>

    <!-- Fahrkosten der/des Auszubildenden -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.fahrkosten'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1">
        {{ kostenSig().fahrkosten }}
      </div>
    </div>

    <!-- Total -->
    <div class="d-flex gap-2">
      <div class="h4 mt-3">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.total'
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
export class PersoenlicheKostenComponent {
  kostenSig = input.required<PersoenlicheBerechnung['kosten']>();
}
