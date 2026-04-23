import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  effect,
  inject,
  input,
} from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatTooltipModule } from '@angular/material/tooltip';
import { RouterLink } from '@angular/router';
import { TranslocoDirective } from '@jsverse/transloco';
import { Store } from '@ngrx/store';

import { BerechnungStore } from '@dv/shared/data-access/berechnung';
import { selectRouteGesuchId } from '@dv/shared/data-access/gesuch';
import { DokumentService, GesuchService } from '@dv/shared/model/gesuch';
import { SharedUiDownloadButtonDirective } from '@dv/shared/ui/download-button';
import { SharedUiFormatChfPipe } from '@dv/shared/ui/format-chf-pipe';
import { SharedUiInfoDialogDirective } from '@dv/shared/ui/info-dialog';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiRdIsPendingWithoutCachePipe } from '@dv/shared/ui/remote-data-pipe';

@Component({
  selector: 'dv-shared-feature-verfuegung-zusammenfassung',
  imports: [
    CommonModule,
    MatCardModule,
    TranslocoDirective,
    RouterLink,
    SharedUiFormatChfPipe,
    SharedUiRdIsPendingWithoutCachePipe,
    SharedUiDownloadButtonDirective,
    SharedUiLoadingComponent,
    SharedUiInfoDialogDirective,
    MatTooltipModule,
  ],
  templateUrl: './shared-feature-verfuegung-zusammenfassung.component.html',
  styleUrl: './shared-feature-verfuegung-zusammenfassung.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureVerfuegungZusammenfassungComponent {
  berechnungStore = inject(BerechnungStore);
  gesuchService = inject(GesuchService);
  dokumentService = inject(DokumentService);
  store = inject(Store);
  gesuchIdSig = this.store.selectSignal(selectRouteGesuchId);
  // todo-review: @scph wir haben noch eine vermischung von namen berechnungId und verfuegungId in den routes
  // eslint-disable-next-line @angular-eslint/no-input-rename
  verfuegungIdSig = input<string | null>(null, { alias: 'berechnungId' });

  constructor() {
    effect(() => {
      const gesuchId = this.gesuchIdSig();
      const verfuegungId = this.verfuegungIdSig();

      if (!gesuchId) {
        return;
      }

      if (verfuegungId) {
        this.berechnungStore.getBerechnungForVerfuegung$({ verfuegungId });
      } else {
        this.berechnungStore.getBerechnungForGesuch$({ gesuchId });
      }
    });
  }
}
