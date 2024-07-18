import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  input,
} from '@angular/core';
import { MatExpansionModule } from '@angular/material/expansion';
import { TranslateModule } from '@ngx-translate/core';

import { SharedUiFormatChfPipe } from '@dv/shared/ui/format-chf-pipe';
import { capitalized } from '@dv/shared/util-fn/string-helper';

import { Berechnung, BerechnungsValue } from '../../../models';

@Component({
  selector: 'dv-berechnungs-expansion-panel',
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
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
          <mat-panel-title
            class="mb-0 d-flex py-4"
            [ngClass]="panel.expanded && view.shouldChangeSize ? 'h3' : 'h4'"
          >
            <div class="d-flex flex-column">
              {{ view.titleKey | translate }}
              @if (!panel.expanded) {
                @if (view.shouldChangeSize) {
                  <span class="fw-normal mt-2">{{
                    view.infoKey | translate
                  }}</span>
                } @else {
                  <div class="row mt-2">
                    <span class="col-7 fs-5 fw-normal">{{
                      view.infoKey | translate
                    }}</span>
                    <span class="col-5 text-end text-muted text-nowrap">
                      {{ view.total | formatChf: false }}
                    </span>
                  </div>
                }
              }
            </div>
            @if (!panel.expanded && view.shouldChangeSize) {
              <span class="text-end flex-grow-1 align-self-start text-nowrap">
                {{ view.total | formatChf: false }}
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
  berechnungSig = input.required<Berechnung>();
  variantSig = input.required<BerechnungsValue>();

  viewSig = computed(() => {
    const berechnung = this.berechnungSig();
    const variant = this.variantSig();
    return {
      titleKey: `sachbearbeitung-app.verfuegung.berechnung.${berechnung.typ}.${variant}.title`,
      infoKey: `sachbearbeitung-app.verfuegung.berechnung.${berechnung.typ}.${variant}.info`,
      shouldChangeSize: berechnung.typ === 'persoenlich',
      total: berechnung[`total${capitalized(variant)}`],
    };
  });
}
