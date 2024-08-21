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
            class="mb-0 me-0 d-flex py-3"
            [ngClass]="panel.expanded && view.shouldChangeSize ? 'h3' : 'h4'"
          >
            <div class="d-flex flex-column flex-grow-1 fs-5">
              {{ view.titleKey | translate }}
              @if (!panel.expanded) {
                <span class="row fw-normal mt-1 fs-6 text-muted">
                  @if (view.shouldChangeSize) {
                    <div class="col-12">
                      {{ view.infoKey | translate }}
                    </div>
                  } @else {
                    <span class="col-7 fw-normal">{{
                      view.infoKey | translate
                    }}</span>
                    <span class="col-5 text-end text-muted text-nowrap">
                      {{ view.total | formatChf: false }}
                    </span>
                  }
                </span>
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
      infoKey: `sachbearbeitung-app.verfuegung.berechnung.${berechnung.typ}.${variant}.total`,
      shouldChangeSize: berechnung.typ === 'persoenlich',
      total: berechnung[`total${capitalized(variant)}`],
    };
  });
}
