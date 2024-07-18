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
    <div class="d-flex">
      <div class="fs-4">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.totalEinkuenfte'
            | translate
        }}
      </div>
      <div class="text-muted text-end flex-grow-1">
        {{ einnahmenSig().totalEinkuenfte }}
      </div>
    </div>

    <!-- Ergänzungsleistungen -->
    <div class="d-flex">
      <div class="fs-4">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.ergaenzungsleistungen'
            | translate
        }}
      </div>
      <div class="text-muted text-end flex-grow-1">
        {{ einnahmenSig().ergaenzungsleistungen }}
      </div>
    </div>

    <!-- Steuerbares Vermögen -->
    <div class="d-flex">
      <div class="fs-4">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.steuerbaresVermoegen'
            | translate
        }}
      </div>
      <div class="text-muted text-end flex-grow-1">
        {{ einnahmenSig().steuerbaresVermoegen }}
      </div>
    </div>

    <!-- 15 % Vermögensaufrechnung -->
    <div class="d-flex">
      <div classs="d-flex flex-column">
        <div class="fs-4">
          {{
            'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.vermoegensaufrechnung'
              | translate
          }}
        </div>
        <div class="text-muted">
          {{
            'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.vermoegensaufrechnung.info'
              | translate
          }}
        </div>
      </div>
      <div class="text-muted text-end flex-grow-1">
        {{ einnahmenSig().vermoegensaufrechnung }}
      </div>
    </div>

    <!-- Abzüge -->
    <div class="d-flex">
      <div class="fs-4">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.ergaenzungsleistungen'
            | translate
        }}
      </div>
      <div class="text-muted text-end flex-grow-1"></div>
    </div>

    <!-- Beiträge von Selbständigerwerbenden in die 2.Säule /Säule 3a -->
    <div class="d-flex">
      <div class="fs-4">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.beitraegeSaule'
            | translate
        }}
      </div>
      <div class="text-muted text-end flex-grow-1">
        {{ einnahmenSig().beitraegeSaule }}
      </div>
    </div>

    <!-- Abzüglich Mietwert -->
    <div class="d-flex">
      <div class="fs-4">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.mietwert'
            | translate
        }}
      </div>
      <div class="text-muted text-end flex-grow-1">
        {{ einnahmenSig().mietwert }}
      </div>
    </div>

    <!-- Abzüglich Alimente/Renten für in Ausbildung stehende Person -->
    <div class="d-flex">
      <div class="fs-4">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.alimenteOderRenten'
            | translate
        }}
      </div>
      <div class="text-muted text-end flex-grow-1">
        {{ einnahmenSig().alimenteOderRenten }}
      </div>
    </div>

    <!-- Einkommensfreibetrag -->
    <div class="d-flex">
      <div class="fs-4">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.einkommensfreibeitrag'
            | translate
        }}
      </div>
      <div class="text-muted text-end flex-grow-1">
        {{ einnahmenSig().einkommensfreibeitrag }}
      </div>
    </div>

    <!-- Total -->
    <div class="d-flex">
      <div class="h4 mt-3">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.total'
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
export class FamilienEinnahmenComponent {
  einnahmenSig = input.required<FamilienBerechnung['einnahmen']>();
}
