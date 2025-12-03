import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatExpansionModule } from '@angular/material/expansion';
import { TranslocoDirective } from '@jsverse/transloco';

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
    CommonModule,
    MatCardModule,
    MatExpansionModule,
    SharedUiFormatChfPipe,
    SharedUiFormatChfNegativePipe,
    BerechnungsExpansionPanelComponent,
    TranslocoDirective,
  ],
  templateUrl: './berechnungs-card.component.html',
})
export class BerechnungsCardComponent {
  berechnungSig = input.required<Berechnung>();

  Math = Math;
}
