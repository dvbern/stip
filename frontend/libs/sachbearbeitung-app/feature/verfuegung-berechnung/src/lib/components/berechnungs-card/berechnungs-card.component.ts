import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatExpansionModule } from '@angular/material/expansion';
import { TranslocoPipe } from '@jsverse/transloco';

import {
  SharedUiFormatChfNegativePipe,
  SharedUiFormatChfPipe,
} from '@dv/shared/ui/format-chf-pipe';

import { BerechnungsExpansionPanelComponent } from './berechnungs-expansion-panel.component';
import { Berechnung } from '../../../models';

@Component({
  selector: 'dv-berechnungs-card',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    MatCardModule,
    MatExpansionModule,
    TranslocoPipe,
    SharedUiFormatChfPipe,
    SharedUiFormatChfNegativePipe,
    BerechnungsExpansionPanelComponent,
  ],
  template: `
    @if (berechnungSig(); as berechnung) {
      <mat-card class="rounded-4">
        <mat-card-header>
          <mat-card-title>
            <div class="d-flex flex-column ps-1">
              <span class="text-muted mb-1">{{
                'sachbearbeitung-app.verfuegung.berechnung.' +
                  berechnung.typ +
                  '.title' | transloco
              }}</span>
              <h3 class="fs-4 mb-4">
                @switch (berechnung.typ) {
                  @case ('persoenlich') {
                    {{ berechnung.name }}
                  }
                  @case ('familien') {
                    {{ berechnung.nameKey | transloco: berechnung }}
                  }
                }
              </h3>
            </div>
          </mat-card-title>
        </mat-card-header>
        <mat-card-content>
          <mat-accordion class="full-width large" multi>
            <dv-berechnungs-expansion-panel
              [berechnungSig]="berechnung"
              [variantSig]="'einnahmen'"
            >
              <ng-content
                select="dv-persoenliche-einnahmen,dv-familien-einnahmen"
              ></ng-content>
            </dv-berechnungs-expansion-panel>
            <dv-berechnungs-expansion-panel
              [berechnungSig]="berechnung"
              [variantSig]="'kosten'"
            >
              <ng-content
                select="dv-persoenliche-kosten,dv-familien-kosten"
              ></ng-content>
            </dv-berechnungs-expansion-panel>
          </mat-accordion>
        </mat-card-content>
        <mat-card-footer class="px-3 py-4">
          @if (berechnung.typ === 'persoenlich' && berechnung.totalVorTeilung) {
            <div class="d-flex justify-content-between fs-5 h4">
              <div>
                {{
                  'sachbearbeitung-app.verfuegung.berechnung.' +
                    berechnung.typ +
                    '.total' | transloco
                }}
              </div>
              <div>
                {{ berechnung.totalVorTeilung | formatChfNegative }}
              </div>
            </div>
            <div class="d-flex text-muted justify-content-between fs-5 tw:mb-2">
              <div>
                {{
                  'sachbearbeitung-app.verfuegung.berechnung.' +
                    berechnung.typ +
                    '.anzahlPersonen' | transloco
                }}
              </div>
              <div>
                {{ berechnung.einnahmen.anzahlPersonenImHaushalt }}
              </div>
            </div>
            <div class="d-flex justify-content-between fs-4 h4">
              <div>
                {{
                  'sachbearbeitung-app.verfuegung.berechnung.' +
                    berechnung.typ +
                    '.totalVorTeilung' | transloco
                }}
              </div>
              <div>
                {{ berechnung.total | formatChf }}
              </div>
            </div>
          } @else {
            <div class="d-flex justify-content-between fs-3 h4">
              {{
                'sachbearbeitung-app.verfuegung.berechnung.' +
                  berechnung.typ +
                  '.total' | transloco
              }}
              <span
                class="text-nowrap"
                [attr.data-testid]="'berechnung-' + berechnung.typ + '-total'"
                >{{ berechnung.total | formatChfNegative }}</span
              >
            </div>
          }
          @if (
            berechnung.typ === 'persoenlich' && berechnung.geteilteBerechnung
          ) {
            <div
              class="mat-expansion-panel-header-title text-muted justify-content-between fs-4 me-0 mb-0 grid w-100"
            >
              {{
                'sachbearbeitung-app.verfuegung.berechnung.' +
                  berechnung.typ +
                  '.geteilteBerechnung'
                  | transloco: berechnung.geteilteBerechnung
              }}
              <span
                class="text-nowrap"
                [attr.data-testid]="
                  'berechnung-' + berechnung.typ + '-geteilteBerechnung'
                "
                >{{
                  berechnung.geteilteBerechnung.anteil | formatChfNegative
                }}</span
              >
            </div>
          }
        </mat-card-footer>
      </mat-card>
    }
  `,
})
export class BerechnungsCardComponent {
  berechnungSig = input.required<Berechnung>();
}
