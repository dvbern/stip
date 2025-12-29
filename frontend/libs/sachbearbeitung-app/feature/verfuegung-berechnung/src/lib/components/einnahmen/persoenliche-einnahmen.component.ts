import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { TranslocoDirective } from '@jsverse/transloco';

import { BerechnungsStammdaten } from '@dv/shared/model/gesuch';
import { PersoenlichesBudgetresultatView } from '@dv/shared/model/verfuegung';
import {
  SharedUiFormatChfPipe,
  SharedUiFormatChfPositivePipe,
} from '@dv/shared/ui/format-chf-pipe';

@Component({
  selector: 'dv-persoenliche-einnahmen',
  imports: [
    TranslocoDirective,
    SharedUiFormatChfPipe,
    SharedUiFormatChfPositivePipe,
  ],
  template: `
    <ng-container *transloco="let t">
      <!-- Nettoerwerbseinkommen -->
      <div class="tw:flex tw:gap-2">
        <div classs="tw:flex flex-column">
          {{
            t(
              'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.nettoerwerbseinkommen'
            )
          }}
          <div class="tw:text-gray-500 tw:text-sm">
            <div>
              {{
                t(
                  'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.nettoerwerbseinkommen.info',
                  {
                    freibetragErwerbseinkommen:
                      stammdatenSig().freibetragErwerbseinkommen | formatChf,
                  }
                )
              }}
            </div>
            <!-- Type missmatch! -->
            <!-- <div class="tw:flex">
              @for (e of einnahmenSig().nettoerwerbseinkommen; track $index) {
                <span>{{ e.vorname }}</span>
                <span>{{ e.value | formatChf }}</span>
              }
            </div> -->
          </div>
        </div>
        <div class="tw:text-gray-500 flex-grow-1 text-end text-nowrap">
          {{ einnahmenSig().nettoerwerbseinkommenTotal | formatChfPositive }}
        </div>
      </div>

      <!-- BGSA -->
      <div class="tw:flex tw:gap-2">
        <div classs="tw:flex flex-column">
          {{
            t(
              'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.einnahmenBGSA'
            )
          }}
          <!-- Todo: is PersonalValueItem -->
          <a href="#1" class="tw:text-xs tw:align-text-top tw:no-underline!">
            1
          </a>
        </div>
        <!-- <div class="tw:text-gray-500 flex-grow-1 text-end text-nowrap">
          {{ einnahmenSig().einnahmenBGSA | formatChfPositive }}
        </div> -->
      </div>

      <!-- Kinder- und Ausbildungszulagen -->
      <div class="tw:flex tw:gap-2">
        <div>
          {{
            t(
              'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.kinderUndAusbildungszulagen'
            )
          }}
          <div class="tw:text-gray-500 tw:text-sm">
            <!-- TODO: Pia, Partner, Kinder  and other fields too-->
            <!-- {{ einnahmenSig().kinderUndAusbildungszulagen }} -->
          </div>
        </div>
        <!-- Todo: is PersonalValueItem -->
        <!-- <div class="tw:text-gray-500 flex-grow-1 text-end text-nowrap">
          {{
            einnahmenSig().kinderUndAusbildungszulagenTotal | formatChfPositive
          }}
        </div> -->
      </div>

      <!-- EO -->
      <div class="tw:flex tw:gap-2">
        <div classs="tw:flex flex-column">
          {{
            t(
              'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.eoLeistungen'
            )
          }}
          <div class="tw:text-gray-500 tw:text-sm">
            {{
              t(
                'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.eoLeistungen.info'
              )
            }}
          </div>
        </div>
        <!-- Todo: is PersonalValueItem -->
        <!-- <div class="tw:text-gray-500 flex-grow-1 text-end text-nowrap">
          {{ einnahmenSig().eoLeistungen | formatChfPositive }}
        </div> -->
      </div>

      <!-- Alimente/Renten -->
      <div class="tw:flex tw:gap-2">
        {{
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.alimente'
          )
        }}
        <!-- Todo: is PersonalValueItem -->
        <!-- <div class="tw:text-gray-500 flex-grow-1 text-end text-nowrap">
          {{ einnahmenSig().alimente | formatChfPositive }}
        </div> -->
      </div>

      <!-- Unterhaltsbeiträge -->
      <div class="tw:flex tw:gap-2">
        {{
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.unterhaltsbeitraege'
          )
        }}
        <!-- Todo: is PersonalValueItem -->
        <!-- <div class="tw:text-gray-500 flex-grow-1 text-end text-nowrap">
          {{ einnahmenSig().unterhaltsbeitraege | formatChfPositive }}
        </div> -->
      </div>

      <!-- Ergänzungsleistungen -->
      <div class="tw:flex tw:gap-2">
        {{
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.ergaenzungsleistungen'
          )
        }}
        <!-- Todo: is PersonalValueItem -->
        <!-- <div class="tw:text-gray-500 flex-grow-1 text-end text-nowrap">
          {{ einnahmenSig().ergaenzungsleistungen | formatChfPositive }}
        </div> -->
      </div>

      <!-- Beiträge an Gemeindeinstitutionen -->
      <div class="tw:flex tw:gap-2">
        {{
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.beitraegeGemeindeInstitution'
          )
        }}
        <!-- <div class="tw:text-gray-500 flex-grow-1 text-end text-nowrap">
          {{ einnahmenSig().beitraegeGemeindeInstitution | formatChfPositive }}
        </div> -->
      </div>

      <!-- Anrechenbares Vermögen -->
      <div class="tw:flex tw:gap-2">
        <div classs="tw:flex flex-column">
          {{
            t(
              'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.anrechenbaresVermoegen'
            )
          }}
          <div class="tw:text-gray-500 tw:text-sm">
            {{
              t(
                'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.anrechenbaresVermoegen.info',
                einnahmenSig()
              )
            }}
          </div>
        </div>
        <div class="tw:text-gray-500 flex-grow-1 text-end text-nowrap">
          {{ einnahmenSig().anrechenbaresVermoegen | formatChfPositive }}
        </div>
      </div>

      <!-- Todo: is PersonalValueItem  and moves up? -->
      <!-- Einkommen Partner -->
      <div class="tw:flex tw:gap-2">
        {{
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.einkommenPartner'
          )
        }}
        <!-- <div class="tw:text-gray-500 flex-grow-1 text-end text-nowrap">
          {{ einnahmenSig().einkommenPartner | formatChfPositive }}
        </div> -->
      </div>

      <!-- Todo: Andere einnahmen ? -->

      <!-- Elterliche Leistung -->
      <div class="tw:flex tw:gap-2">
        {{
          t(
            'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.elterlicheLeistung'
          )
        }}
        <div class="tw:text-gray-500 flex-grow-1 text-end text-nowrap">
          {{ einnahmenSig().elterlicheLeistung | formatChfPositive }}
        </div>
      </div>

      <!-- Total -->
      <div class="tw:flex mt-3 tw:gap-2">
        <div class="h4">
          {{
            t(
              'sachbearbeitung-app.verfuegung.berechnung.persoenlich.einnahmen.total'
            )
          }}
        </div>
        <div class="h4 flex-grow-1 text-end text-nowrap">
          {{ einnahmenSig().total | formatChf }}
        </div>
      </div>
    </ng-container>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PersoenlicheEinnahmenComponent {
  einnahmenSig = input.required<PersoenlichesBudgetresultatView['einnahmen']>();
  stammdatenSig = input.required<BerechnungsStammdaten>();
}
