import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { TranslateModule } from '@ngx-translate/core';

import { BerechnungStore } from '@dv/sachbearbeitung-app/data-access/berechnung';

@Component({
  selector: 'lib-shared-feature-verfuegung-zusammenfassung',
  standalone: true,
  imports: [CommonModule, MatCardModule, TranslateModule],
  templateUrl: './shared-feature-verfuegung-zusammenfassung.component.html',
  styleUrl: './shared-feature-verfuegung-zusammenfassung.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureVerfuegungZusammenfassungComponent {
  berechnungStore = inject(BerechnungStore);
}
