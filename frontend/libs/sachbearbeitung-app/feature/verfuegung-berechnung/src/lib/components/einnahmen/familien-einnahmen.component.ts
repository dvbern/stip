import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';

import { SharedUiFormatChfPipe } from '@dv/shared/ui/format-chf-pipe';

import { FamilienBerechnung } from '../../../models';

@Component({
  selector: 'dv-familien-einnahmen',
  standalone: true,
  imports: [CommonModule, TranslateModule, SharedUiFormatChfPipe],
  template: `
    <!-- Total Einkünfte -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.totalEinkuenfte'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1">
        {{ einnahmenSig().totalEinkuenfte }}
      </div>
    </div>

    <!-- Ergänzungsleistungen -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.ergaenzungsleistungen'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1">
        {{ einnahmenSig().ergaenzungsleistungen }}
      </div>
    </div>

    <!-- Anrechenbares Vermögen -->
    <div class="d-flex gap-2">
      <div classs="d-flex flex-column">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.anrechenbaresVermoegen'
            | translate
        }}
        <div class="text-muted">
          {{
            'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.anrechenbaresVermoegen.info'
              | translate: einnahmenSig()
          }}
        </div>
      </div>
      <div class="text-muted text-end flex-grow-1">
        {{ einnahmenSig().anrechenbaresVermoegen }}
      </div>
    </div>

    <!-- Abzüge -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.ergaenzungsleistungen'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1"></div>
    </div>

    <!-- Beiträge von Selbständigerwerbenden in die 2.Säule -->
    <div class="d-flex gap-2">
      <div classs="d-flex flex-column">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.beitraegeSaule2'
            | translate
        }}
        <div class="text-muted">
          {{
            'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.beitraegeSaule2.info'
              | translate
          }}
        </div>
      </div>
      <div class="text-muted text-end flex-grow-1">
        {{ einnahmenSig().sauele2 }}
      </div>
    </div>

    <!-- Beiträge von Selbständigerwerbenden in die Säule 3a -->
    <div class="d-flex gap-2">
      <div classs="d-flex flex-column">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.beitraegeSaule3a'
            | translate
        }}
        <div class="text-muted">
          {{
            'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.beitraegeSaule3a.info'
              | translate: { maxSaeule3a: 7000 }
          }}
        </div>
      </div>
      <div class="text-muted text-end flex-grow-1">
        {{ einnahmenSig().sauele3 }}
      </div>
    </div>

    <!-- Abzüglich Mietwert -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.mietwert'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1">
        {{ einnahmenSig().mietwert }}
      </div>
    </div>

    <!-- Abzüglich Alimente/Renten für in Ausbildung stehende Person -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.alimenteOderRenten'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1">
        {{ einnahmenSig().alimenteOderRenten }}
      </div>
    </div>

    <!-- Einkommensfreibetrag -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.einkommensfreibeitrag'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1">
        {{ einnahmenSig().einkommensfreibeitrag }}
      </div>
    </div>

    <!-- Total -->
    <div class="d-flex mt-3 gap-2">
      <div class="h4 m-0">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.total'
            | translate
        }}
      </div>
      <div class="text-muted text-end flex-grow-1">
        {{ einnahmenSig().total | formatChf: false }}
      </div>
    </div>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class FamilienEinnahmenComponent {
  einnahmenSig = input.required<FamilienBerechnung['einnahmen']>();
}
