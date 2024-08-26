import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatExpansionModule } from '@angular/material/expansion';
import { TranslateModule } from '@ngx-translate/core';

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
    TranslateModule,
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
          <div class="mat-accordion full-width">
            <div class="mat-expansion-panel-header h-auto pe-none">
              <div
                class="mat-expansion-panel-header-title h4 py-4 mb-0 me-0 grid ps-4 justify-content-between fs-3"
              >
                {{
                  'sachbearbeitung-app.verfuegung.berechnung.' +
                    berechnung.typ +
                    '.total' | translate
                }}
                <span class="text-nowrap">{{
                  berechnung.total | formatChfNegative
                }}</span>
              </div>
            </div>
          </div>
        </mat-card-content>
      </mat-card>
    }
  `,
})
export class BerechnungsCardComponent {
  berechnungSig = input.required<Berechnung>();
}
