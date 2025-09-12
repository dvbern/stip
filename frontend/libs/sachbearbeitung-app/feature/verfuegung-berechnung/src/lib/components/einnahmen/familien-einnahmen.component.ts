import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { TranslocoPipe } from '@jsverse/transloco';

import {
  SharedUiFormatChfNegativePipe,
  SharedUiFormatChfPipe,
  SharedUiFormatChfPositivePipe,
} from '@dv/shared/ui/format-chf-pipe';

import { FamilienBerechnung } from '../../../models';

@Component({
  selector: 'dv-familien-einnahmen',
  imports: [
    TranslocoPipe,
    SharedUiFormatChfPipe,
    SharedUiFormatChfNegativePipe,
    SharedUiFormatChfPositivePipe,
  ],
  template: `
    <!-- Total Einkünfte -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.totalEinkuenfte'
          | transloco
      }}
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ einnahmenSig().totalEinkuenfte | formatChfPositive }}
      </div>
    </div>

    <!-- Ergänzungsleistungen -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.ergaenzungsleistungen'
          | transloco
      }}
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ einnahmenSig().ergaenzungsleistungen | formatChfPositive }}
      </div>
    </div>

    <!-- Abzüglich Mietwert -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.mietwert'
          | transloco
      }}
      <div class="text-muted text-end flex-grow-1 text-nowrap">
        {{ einnahmenSig().mietwert | formatChfNegative: true }}
      </div>
    </div>

    <!-- Abzüglich Alimente/Renten für in Ausbildung stehende Person -->
    <div class="d-flex gap-2">
      {{
        'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.kinderalimente'
          | transloco
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
            | transloco
        }}
        <div class="text-muted fs-7">
          {{
            'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.beitraegeSaule3a.info'
              | transloco: einnahmenSig()
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
            | transloco
        }}
        <div class="text-muted fs-7">
          {{
            'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.beitraegeSaule2.info'
              | transloco
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
            | transloco
        }}
        <div class="text-muted fs-7">
          {{
            'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.einkommensfreibeitrag.info'
              | transloco
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
            | transloco
        }}
        <div class="text-muted fs-7">
          {{
            'sachbearbeitung-app.verfuegung.berechnung.familien.einnahmen.anrechenbaresVermoegen.info'
              | transloco: einnahmenSig()
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
            | transloco
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
