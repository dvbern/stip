import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  computed,
  effect,
  inject,
} from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';

import { BerechnungStore } from '@dv/shared/data-access/berechnung';
import {
  VERFUEGUNG_OPTIONS,
  VerfuegungOption,
  createBerechnungOption,
} from '@dv/sachbearbeitung-app/model/verfuegung';
import { SachbearbeitungAppPatternGesuchHeaderComponent } from '@dv/sachbearbeitung-app/pattern/gesuch-header';
import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import { SharedPatternAppHeaderComponent } from '@dv/shared/pattern/app-header';
import { GlobalNotificationsComponent } from '@dv/shared/pattern/global-notification';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';

@Component({
  selector: 'dv-sachbearbeitung-app-pattern-verfuegung-layout',
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    RouterLink,
    RouterLinkActive,
    SharedPatternAppHeaderComponent,
    GlobalNotificationsComponent,
    SharedUiIconChipComponent,
    SachbearbeitungAppPatternGesuchHeaderComponent,
  ],
  styleUrl: './sachbearbeitung-app-pattern-verfuegung-layout.component.scss',
  templateUrl: './sachbearbeitung-app-pattern-verfuegung-layout.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [BerechnungStore],
})
export class SachbearbeitungAppPatternVerfuegungLayoutComponent {
  @Input() option?: VerfuegungOption;
  navClicked$ = new EventEmitter();
  route = inject(Router);
  store = inject(Store);
  gesuchViewSig = this.store.selectSignal(selectSharedDataAccessGesuchsView);
  verfuegungOptions = VERFUEGUNG_OPTIONS;

  berechnungStore = inject(BerechnungStore);

  berechnungenSig = computed(() => {
    const gesuchId = this.gesuchViewSig().gesuchId;
    // berechnungenOptions will be fetched dynamically in the future
    const berechnungenOptions = [];

    const berechnungenRd = this.berechnungStore.berechnungen();
    if (berechnungenRd.data) {
      for (
        let berechnungIndex = 0;
        berechnungIndex < berechnungenRd.data.length;
        berechnungIndex++
      ) {
        berechnungenOptions.push(createBerechnungOption(berechnungIndex));
      }
    }

    return berechnungenOptions.map((option) => ({
      ...option,
      fullRoute: ['/', 'verfuegung', gesuchId, ...option.route.split('/')],
    }));
  });

  constructor() {
    effect(
      () => {
        const { gesuchId } = this.gesuchViewSig();

        if (!gesuchId) {
          return;
        }
        this.berechnungStore.calculateBerechnung$({ gesuchId });
      },
      { allowSignalWrites: true },
    );
  }
}
