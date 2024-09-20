import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';

import {
  SharedUiFormatChfPipe,
  SharedUiFormatChfPositivePipe,
} from '@dv/shared/ui/format-chf-pipe';

import { PersoenlicheBerechnung } from '../../../models';

@Component({
  selector: 'dv-persoenliche-kosten',
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    SharedUiFormatChfPipe,
    SharedUiFormatChfPositivePipe,
  ],
  template: `
    <!-- Mehrkosten für auswärtige Verpflegung -->
    <div class="d-flex gap-2">
      <div classs="d-flex flex-column">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.mehrkostenVerpflegung'
            | translate
        }}
        <div class="text-muted fs-7">
          {{
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.nurElternWohnend.info'
              | translate
          }}
        </div>
      </div>
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ kostenSig().mehrkostenVerpflegung | formatChfPositive }}
      </div>
    </div>

    <!-- Fahrkosten der/des Auszubildenden -->
    <div class="d-flex gap-2">
      <div classs="d-flex flex-column">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.fahrkosten'
            | translate
        }}
        <div class="text-muted fs-7">
          {{
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.nurElternWohnend.info'
              | translate
          }}
        </div>
      </div>
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ kostenSig().fahrkosten | formatChfPositive }}
      </div>
    </div>

    <!-- Ausbildungskosten der/des Auszubildenden -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.ausbildungskosten'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ kostenSig().ausbildungskosten | formatChfPositive }}
      </div>
    </div>

    <!-- Grundbedarf für 0 Personenhaushalt -->
    <div class="d-flex gap-2">
      <div classs="d-flex flex-column">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.grundbedarfPersonen'
            | translate: { anzahl: kostenSig().anzahlPersonenImHaushalt }
        }}
        <div class="text-muted fs-7">
          {{
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.nurEigenerHaushalt.info'
              | translate: { anzahl: kostenSig().anzahlPersonenImHaushalt }
          }}
        </div>
      </div>
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ kostenSig().grundbedarfPersonen | formatChfPositive }}
      </div>
    </div>

    <!-- Wohnkosten für 0 Personenhaushalt -->
    <div class="d-flex gap-2">
      <div classs="d-flex flex-column">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.wohnkostenPersonen'
            | translate: { anzahl: kostenSig().anzahlPersonenImHaushalt }
        }}
        <div class="text-muted fs-7">
          {{
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.nurEigenerHaushalt.info'
              | translate: { anzahl: kostenSig().anzahlPersonenImHaushalt }
          }}
        </div>
      </div>
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ kostenSig().wohnkostenPersonen | formatChfPositive }}
      </div>
    </div>

    <!-- Medizinische Grundversorgung für 0 Personenhaushalt -->
    <div class="d-flex gap-2">
      <div classs="d-flex flex-column">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.medizinischeGrundversorgungPersonen'
            | translate: { anzahl: kostenSig().anzahlPersonenImHaushalt }
        }}
        <div class="text-muted fs-7">
          {{
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.nurEigenerHaushalt.info'
              | translate: { anzahl: kostenSig().anzahlPersonenImHaushalt }
          }}
        </div>
      </div>
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{
          kostenSig().medizinischeGrundversorgungPersonen | formatChfPositive
        }}
      </div>
    </div>

    <!-- Fahrkosten Ehepartnerin/Ehepartner -->
    <div class="d-flex gap-2">
      <div classs="d-flex flex-column">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.fahrkostenPartner'
            | translate
        }}
        <div class="text-muted fs-7">
          {{
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.nurErwerbstaetig.info'
              | translate
          }}
        </div>
      </div>
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ kostenSig().fahrkostenPartner | formatChfPositive }}
      </div>
    </div>

    <!-- Verpflegung Ehepartnerin/Ehepartner -->
    <div class="d-flex gap-2">
      <div classs="d-flex flex-column">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.verpflegungPartner'
            | translate
        }}
        <div class="text-muted fs-7">
          {{
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.nurErwerbstaetig.info'
              | translate
          }}
        </div>
      </div>
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ kostenSig().verpflegungPartner | formatChfPositive }}
      </div>
    </div>

    <!-- Betreuungskosten für Kinder -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.betreuungskostenKinder'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ kostenSig().betreuungskostenKinder | formatChfPositive }}
      </div>
    </div>

    <!-- Kantons- und Gemeindesteuern -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.kantonsGemeindesteuern'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ kostenSig().kantonsGemeindesteuern | formatChfPositive }}
      </div>
    </div>

    <!-- Bundessteuern -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.bundessteuern'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ kostenSig().bundessteuern | formatChfPositive }}
      </div>
    </div>

    <!-- Ungedeckter Anteil Lebenshaltungskosten  -->
    <div class="d-flex gap-2">
      <div classs="d-flex flex-column">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.anteilLebenshaltungskosten'
            | translate
        }}
        <div class="text-muted fs-7">
          {{
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.anteilLebenshaltungskosten.info'
              | translate
          }}
        </div>
      </div>
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ kostenSig().anteilLebenshaltungskosten | formatChfPositive }}
      </div>
    </div>

    <!-- Total -->
    <div class="mt-3 d-flex gap-2">
      <div class="h4 ">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.persoenlich.kosten.total'
            | translate
        }}
      </div>
      <div class="h4 text-end flex-grow-1 text-nowrap">
        {{ kostenSig().total | formatChf }}
      </div>
    </div>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PersoenlicheKostenComponent {
  kostenSig = input.required<PersoenlicheBerechnung['kosten']>();
}
