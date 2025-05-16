import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  effect,
  inject,
} from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { RouterLink } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslateDirective, TranslatePipe } from '@ngx-translate/core';

import { BerechnungStore } from '@dv/shared/data-access/berechnung';
import { selectRouteId } from '@dv/shared/data-access/gesuch';
import { DokumentService, GesuchService } from '@dv/shared/model/gesuch';
import { SharedUiDownloadButtonDirective } from '@dv/shared/ui/download-button';
import {
  SharedUiFormatChfNegativePipe,
  SharedUiFormatChfPipe,
} from '@dv/shared/ui/format-chf-pipe';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiRdIsPendingWithoutCachePipe } from '@dv/shared/ui/remote-data-pipe';

@Component({
  selector: 'lib-shared-feature-verfuegung-zusammenfassung',
  imports: [
    CommonModule,
    MatCardModule,
    TranslateDirective,
    TranslatePipe,
    RouterLink,
    SharedUiIconChipComponent,
    SharedUiFormatChfPipe,
    SharedUiFormatChfNegativePipe,
    SharedUiRdIsPendingWithoutCachePipe,
    SharedUiDownloadButtonDirective,
    SharedUiLoadingComponent,
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
  gesuchIdSig = this.store.selectSignal(selectRouteId);

  constructor() {
    effect(
      () => {
        const gesuchId = this.gesuchIdSig();

        if (!gesuchId) {
          return;
        }
        this.berechnungStore.getBerechnungForGesuch$({ gesuchId });
      },
      { allowSignalWrites: true },
    );
  }
}
