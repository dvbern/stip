/* eslint-disable @angular-eslint/no-input-rename */
import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
  input,
} from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Router, RouterLink } from '@angular/router';
import { Store } from '@ngrx/store';

import { BerechnungStore } from '@dv/shared/data-access/berechnung';
import { selectRouteGesuchId } from '@dv/shared/data-access/gesuch';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import { DokumentService, GesuchService } from '@dv/shared/model/gesuch';
import { SharedUiAdvTranslocoDirective } from '@dv/shared/ui/adv-transloco-directive';
import {
  SharedUiFormatChfNegativePipe,
  SharedUiFormatChfPipe,
} from '@dv/shared/ui/format-chf-pipe';
import { SharedUiInfoDialogDirective } from '@dv/shared/ui/info-dialog';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiRdIsPendingWithoutCachePipe } from '@dv/shared/ui/remote-data-pipe';

@Component({
  selector: 'dv-shared-feature-verfuegung-zusammenfassung',
  imports: [
    CommonModule,
    RouterLink,
    MatCardModule,
    MatTooltipModule,
    SharedUiFormatChfPipe,
    SharedUiFormatChfNegativePipe,
    SharedUiRdIsPendingWithoutCachePipe,
    SharedUiLoadingComponent,
    SharedUiInfoDialogDirective,
    SharedUiAdvTranslocoDirective,
  ],
  templateUrl: './shared-feature-verfuegung-zusammenfassung.component.html',
  styleUrl: './shared-feature-verfuegung-zusammenfassung.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureVerfuegungZusammenfassungComponent {
  private router = inject(Router);
  private config = inject(SharedModelCompileTimeConfig);
  berechnungStore = inject(BerechnungStore);
  gesuchService = inject(GesuchService);
  dokumentService = inject(DokumentService);
  store = inject(Store);
  gesuchIdSig = this.store.selectSignal(selectRouteGesuchId);
  // todo-review: @scph wir haben noch eine vermischung von namen berechnungId und verfuegungId in den routes
  verfuegungIdSig = input<string | null>(null, { alias: 'berechnungId' });
  latestVerfuegungIdSig = input<string | null>(null, {
    alias: 'latestVerfuegungId',
  });

  zusammenfassungViewSig = computed(() => {
    const zusammenfassung =
      this.berechnungStore.berechnungZusammenfassungViewSig();

    const berechnungGroup = Object.entries(
      zusammenfassung.berechnungsresultate,
    ).map(([trancheId, tranche]) => {
      return {
        trancheId,
        tranche,
      };
    });

    return {
      ...zusammenfassung,
      berechnungsresultateGroup: berechnungGroup,
    };
  });

  constructor() {
    effect(() => {
      const gesuchId = this.gesuchIdSig();
      const verfuegungId = this.verfuegungIdSig();
      const latestVerfuegungId = this.latestVerfuegungIdSig();

      if (!gesuchId) {
        return;
      }
      this.berechnungStore.getBerechnung$({
        gesuchId,
        verfuegungId,
        latestVerfuegungId,
      });
    });
  }
}
