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
import { MatSidenavModule } from '@angular/material/sidenav';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslatePipe } from '@ngx-translate/core';

import { GesuchStore } from '@dv/sachbearbeitung-app/data-access/gesuch';
import {
  VERFUEGUNG_OPTIONS,
  VerfuegungOption,
  createBerechnungOption,
} from '@dv/sachbearbeitung-app/model/verfuegung';
import { SachbearbeitungAppPatternGesuchHeaderComponent } from '@dv/sachbearbeitung-app/pattern/gesuch-header';
import { BerechnungStore } from '@dv/shared/data-access/berechnung';
import { selectRouteId } from '@dv/shared/data-access/gesuch';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import { getGesuchPermissions } from '@dv/shared/model/permission-state';
import { SharedPatternAppHeaderComponent } from '@dv/shared/pattern/app-header';
import { SharedPatternMobileSidenavComponent } from '@dv/shared/pattern/mobile-sidenav';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { isPending } from '@dv/shared/util/remote-data';

@Component({
  selector: 'dv-sachbearbeitung-app-pattern-verfuegung-layout',
  standalone: true,
  imports: [
    CommonModule,
    TranslatePipe,
    RouterLink,
    RouterLinkActive,
    MatSidenavModule,
    SharedPatternMobileSidenavComponent,
    SharedPatternAppHeaderComponent,
    SharedUiIconChipComponent,
    SachbearbeitungAppPatternGesuchHeaderComponent,
  ],
  styleUrl: './sachbearbeitung-app-pattern-verfuegung-layout.component.scss',
  templateUrl: './sachbearbeitung-app-pattern-verfuegung-layout.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [GesuchStore, BerechnungStore],
})
export class SachbearbeitungAppPatternVerfuegungLayoutComponent {
  @Input() option?: VerfuegungOption;
  navClicked$ = new EventEmitter();
  route = inject(Router);
  verfuegungOptions = VERFUEGUNG_OPTIONS;

  private store = inject(Store);
  private gesuchStore = inject(GesuchStore);
  private berechnungStore = inject(BerechnungStore);
  private config = inject(SharedModelCompileTimeConfig);

  gesuchIdSig = this.store.selectSignal(selectRouteId);
  gesuchPermissionsSig = computed(() => {
    const gesuchStatus = this.gesuchStore.gesuchInfo().data?.gesuchStatus;
    console.log('gesuchStatus', gesuchStatus);
    if (!gesuchStatus) {
      return {};
    }
    return getGesuchPermissions({ gesuchStatus }, this.config.appType);
  });
  isLoadingSig = computed(() => {
    return isPending(this.gesuchStore.gesuchInfo());
  });

  berechnungenSig = computed(() => {
    const gesuchId = this.gesuchIdSig();
    const berechnungenOptions: VerfuegungOption[] = [];

    const berechnung = this.berechnungStore.berechnungZusammenfassungViewSig();
    berechnung.berechnungsresultate.forEach((berechnungen, berechnungIndex) => {
      berechnungen.forEach((berechnung) => {
        berechnungenOptions.push(
          createBerechnungOption(berechnungIndex, berechnung.type),
        );
      });
    });

    return berechnungenOptions.map((option) => ({
      ...option,
      fullRoute: ['/', 'verfuegung', gesuchId, ...option.route.split('/')],
    }));
  });

  constructor() {
    effect(
      () => {
        const gesuchId = this.gesuchIdSig();
        if (gesuchId) {
          this.gesuchStore.loadGesuchInfo$({ gesuchId });
        }
      },
      { allowSignalWrites: true },
    );
  }
}
