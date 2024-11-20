import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { TranslatePipe } from '@ngx-translate/core';

import {
  SharedUiFormatChfNegativePipe,
  SharedUiFormatChfPipe,
  SharedUiFormatChfPositivePipe,
} from '@dv/shared/ui/format-chf-pipe';

import { FamilienBerechnung } from '../../../models';

@Component({
  selector: 'dv-familien-einnahmen',
  standalone: true,
  imports: [
    CommonModule,
    TranslatePipe,
    SharedUiFormatChfPipe,
    SharedUiFormatChfNegativePipe,
    SharedUiFormatChfPositivePipe,
  ],
  template: `
    <!-- Total Einkünfte -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.totalEinkuenfte'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ einnahmenSig().totalEinkuenfte | formatChfPositive }}
      </div>
    </div>

    <!-- Ergänzungsleistungen -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.ergaenzungsleistungen'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ einnahmenSig().ergaenzungsleistungen | formatChfPositive }}
      </div>
    </div>

    <!-- Abzüglich Mietwert -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.mietwert'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ einnahmenSig().mietwert | formatChfNegative: true }}
      </div>
    </div>

    <!-- Abzüglich Alimente/Renten für in Ausbildung stehende Person -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.kinderalimente'
          | translate
      }}
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ einnahmenSig().kinderalimente | formatChfNegative: true }}
      </div>
    </div>

    <!-- Beiträge von Selbständigerwerbenden in die Säule 3a -->
    <div class="d-flex gap-2">
      <div classs="d-flex flex-column">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.beitraegeSaule3a'
            | translate
        }}
        <div class="text-muted fs-7">
          {{
            'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.beitraegeSaule3a.info'
              | translate: einnahmenSig()
          }}
        </div>
      </div>
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ einnahmenSig().sauele3 | formatChfNegative: true }}
      </div>
    </div>

    <!-- Beiträge von Selbständigerwerbenden in die 2.Säule -->
    <div class="d-flex gap-2">
      <div classs="d-flex flex-column">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.beitraegeSaule2'
            | translate
        }}
        <div class="text-muted fs-7">
          {{
            'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.beitraegeSaule2.info'
              | translate
          }}
        </div>
      </div>
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ einnahmenSig().sauele2 | formatChfNegative: true }}
      </div>
    </div>

    <!-- Einkommensfreibetrag -->
    <div class="d-flex gap-2">
      <div classs="d-flex flex-column">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.einkommensfreibeitrag'
            | translate
        }}
        <div class="text-muted fs-7">
          {{
            'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.einkommensfreibeitrag.info'
              | translate
          }}
        </div>
      </div>
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ einnahmenSig().einkommensfreibeitrag | formatChfNegative: true }}
      </div>
    </div>

    <!-- Anrechenbares Vermögen -->
    <div class="d-flex gap-2">
      <div classs="d-flex flex-column">
        {{
          'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.anrechenbaresVermoegen'
            | translate
        }}
        <div class="text-muted fs-7">
          {{
            'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.anrechenbaresVermoegen.info'
              | translate: einnahmenSig()
          }}
        </div>
      </div>
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ einnahmenSig().anrechenbaresVermoegen | formatChfPositive }}
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
      <div class="text-end h4 flex-grow-1 text-nowrap">
        {{ einnahmenSig().total | formatChf }}
      </div>
    </div>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class FamilienEinnahmenComponent {
  einnahmenSig = input.required<FamilienBerechnung['einnahmen']>();
}
