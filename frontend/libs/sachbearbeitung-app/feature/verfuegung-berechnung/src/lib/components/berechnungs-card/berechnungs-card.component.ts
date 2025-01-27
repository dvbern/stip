import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatExpansionModule } from '@angular/material/expansion';
import { TranslatePipe } from '@ngx-translate/core';

import {
  SharedUiFormatChfNegativePipe,
  SharedUiFormatChfPipe,
} from '@dv/shared/ui/format-chf-pipe';

import { BerechnungsExpansionPanelComponent } from './berechnungs-expansion-panel.component';
import { Berechnung } from '../../../models';

@Component({
  selector: 'dv-berechnungs-card',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    CommonModule,
    MatCardModule,
    MatExpansionModule,
    TranslatePipe,
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
                  '.title' | translate
              }}</span>
              <h3 class="mb-4 fs-4">
                @switch (berechnung.typ) {
                  @case ('persoenlich') {
                    {{ berechnung.name }}
                  }
                  @case ('familien') {
                    {{ berechnung.nameKey | translate: berechnung }}
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
          <div class="d-flex justify-content-between fs-3 h4">
            {{
              'sachbearbeitung-app.verfuegung.berechnung.' +
                berechnung.typ +
                '.total' | translate
            }}
            <span
              class="text-nowrap"
              [attr.data-testid]="'berechnung-' + berechnung.typ + '-total'"
              >{{ berechnung.total | formatChfNegative }}</span
            >
          </div>
          @if (
            berechnung.typ === 'persoenlich' && berechnung.geteilteBerechnung
          ) {
            <div
              class="mat-expansion-panel-header-title text-muted mb-0 me-0 grid justify-content-between fs-4 w-100"
            >
              {{
                'sachbearbeitung-app.verfuegung.berechnung.' +
                  berechnung.typ +
                  '.geteilteBerechnung'
                  | translate: berechnung.geteilteBerechnung
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
