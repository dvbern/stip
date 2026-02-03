import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  input,
} from '@angular/core';
import { MatExpansionModule } from '@angular/material/expansion';
import { TranslocoPipe } from '@jsverse/transloco';

import {
  BerechnungPersonalOrFam,
  BerechnungsValue,
} from '@dv/shared/model/verfuegung';
import { SharedUiFormatChfPipe } from '@dv/shared/ui/format-chf-pipe';

@Component({
  selector: 'dv-berechnungs-expansion-panel',
  imports: [
    CommonModule,
    TranslocoPipe,
    MatExpansionModule,
    SharedUiFormatChfPipe,
  ],
  template: `
    @if (viewSig(); as view) {
      <mat-expansion-panel
        #panel
        class="mat-elevation-z0 border-top border-bottom"
        [togglePosition]="'before'"
      >
        <mat-expansion-panel-header>
          <mat-panel-title class="mb-0 me-0 d-flex py-3 h5">
            <div class="d-flex flex-column flex-grow-1 h5">
              {{ view.titleKey | transloco }}
              @if (!panel.expanded) {
                <span class="row fw-normal mt-1 fs-6 text-muted">
                  <div class="col-12">
                    {{ view.infoKey | transloco }}
                  </div>
                </span>
              }
            </div>
            @if (!panel.expanded) {
              <span class="text-end flex-grow-1 align-self-start text-nowrap">
                {{ view.total | formatChf }}
              </span>
            }
          </mat-panel-title>
        </mat-expansion-panel-header>
        <ng-content></ng-content>
      </mat-expansion-panel>
    }
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class BerechnungsExpansionPanelComponent {
  berechnungSig = input.required<BerechnungPersonalOrFam>();
  variantSig = input.required<BerechnungsValue>();

  viewSig = computed(() => {
    const berechnung = this.berechnungSig();
    const variant = this.variantSig();
    return {
      titleKey: `sachbearbeitung-app.verfuegung.berechnung.${berechnung.typ}.${variant}.title`,
      infoKey: `sachbearbeitung-app.verfuegung.berechnung.${berechnung.typ}.${variant}.total`,
      total: berechnung[variant].total,
    };
  });
}
