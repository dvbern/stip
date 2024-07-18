import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatExpansionModule } from '@angular/material/expansion';
import { TranslateModule } from '@ngx-translate/core';

import { SharedUiFormatChfPipe } from '@dv/shared/ui/format-chf-pipe';

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
    BerechnungsExpansionPanelComponent,
  ],
  templateUrl: './berechnungs-card.component.html',
})
export class BerechnungsCardComponent {
  berechnungSig = input.required<Berechnung>();
}
