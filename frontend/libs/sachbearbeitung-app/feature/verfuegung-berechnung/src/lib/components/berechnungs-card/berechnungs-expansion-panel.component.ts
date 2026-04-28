import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  input,
} from '@angular/core';
import { MatExpansionModule } from '@angular/material/expansion';
import { TranslocoDirective } from '@jsverse/transloco';

import {
  BerechnungPersonalOrFam,
  BerechnungsValue,
} from '@dv/shared/model/verfuegung';
import { SharedUiFormatChfPipe } from '@dv/shared/ui/format-chf-pipe';

@Component({
  selector: 'dv-berechnungs-expansion-panel',
  imports: [
    MatExpansionModule,
    SharedUiFormatChfPipe,
    TranslocoDirective,
    CommonModule,
  ],
  template: `
    @if (viewSig(); as view) {
      <mat-expansion-panel
        #panel
        class="mat-elevation-z0 tw:bg-gray-100! tw:rounded-lg! dv-large"
        [hideToggle]="true"
        *transloco="let t"
      >
        <mat-expansion-panel-header
          [ngClass]="{
            'tw:border-b tw:border-b-gray-500 tw:rounded-b-none!':
              panel.expanded,
          }"
        >
          <mat-panel-title class="tw:block! tw:m-0!">
            <div class="tw:flex tw:font-semibold tw:flex-1 tw:justify-between">
              <div class="tw:flex tw:items-center tw:gap-2">
                <span>
                  {{ t(view.titleKey) }}
                </span>
                @if (panel.expanded) {
                  <i class="material-symbols-rounded">keyboard_arrow_up</i>
                } @else {
                  <i class="material-symbols-rounded">keyboard_arrow_down</i>
                }
              </div>
              <span class="text-end align-self-start text-nowrap">
                {{ view.total | formatChf }}
              </span>
            </div>
            <span class="tw:italic tw:text-sm">
              {{ t(view.infoKey) }}
            </span>
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
