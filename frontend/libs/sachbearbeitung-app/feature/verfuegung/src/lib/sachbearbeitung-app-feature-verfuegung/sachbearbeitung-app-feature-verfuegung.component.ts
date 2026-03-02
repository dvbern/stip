import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  computed,
  effect,
  inject,
} from '@angular/core';
import { MatSidenavModule } from '@angular/material/sidenav';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { TranslocoPipe } from '@jsverse/transloco';
import { Store } from '@ngrx/store';

import {
  VERFUEGUNG_OPTIONS,
  VerfuegungOption,
  createBerechnungOption,
} from '@dv/sachbearbeitung-app/model/verfuegung';
import { SachbearbeitungAppUiAdvTranslocoDirective } from '@dv/sachbearbeitung-app/ui/adv-transloco-directive';
import { BerechnungStore } from '@dv/shared/data-access/berechnung';
import { selectRouteId } from '@dv/shared/data-access/gesuch';
import { GesuchInfoStore } from '@dv/shared/data-access/gesuch-info';
import { PermissionStore } from '@dv/shared/global/permission';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import { getGesuchPermissions } from '@dv/shared/model/permission-state';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiRouterOutletWrapperComponent } from '@dv/shared/ui/router-outlet-wrapper';
import { isPending } from '@dv/shared/util/remote-data';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-verfuegung',
  imports: [
    SharedUiRouterOutletWrapperComponent,
    CommonModule,
    TranslocoPipe,
    RouterLink,
    RouterLinkActive,
    MatSidenavModule,
    SharedUiIconChipComponent,
    SachbearbeitungAppUiAdvTranslocoDirective,
  ],
  templateUrl: './sachbearbeitung-app-feature-verfuegung.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureVerfuegungComponent {
  option?: VerfuegungOption;

  navClicked$ = new EventEmitter();
  route = inject(Router);
  verfuegungOptions = VERFUEGUNG_OPTIONS;

  private store = inject(Store);
  private gesuchInfoStore = inject(GesuchInfoStore);
  private berechnungStore = inject(BerechnungStore);
  private permissionStore = inject(PermissionStore);
  private config = inject(SharedModelCompileTimeConfig);

  gesuchIdSig = this.store.selectSignal(selectRouteId);
  gesuchPermissionsSig = computed(() => {
    const gesuchStatus =
      this.gesuchInfoStore.gesuchInfo().data?.state.gesuchStatus;
    const rolesMap = this.permissionStore.rolesMapSig();
    if (!gesuchStatus) {
      return {};
    }
    return getGesuchPermissions(
      { gesuchStatus },
      this.config.appType,
      rolesMap,
    );
  });
  isLoadingSig = computed(() => {
    return isPending(this.gesuchInfoStore.gesuchInfo());
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
      active: this.route.isActive(option.route, {
        paths: 'subset',
        queryParams: 'subset',
        fragment: 'ignored',
        matrixParams: 'ignored',
      }),
      fullRoute: ['/', 'verfuegung', gesuchId, ...option.route.split('/')],
    }));
  });

  constructor() {
    effect(() => {
      const gesuchId = this.gesuchIdSig();
      if (gesuchId) {
        this.gesuchInfoStore.loadGesuchInfo$({ gesuchId });
      }
    });
  }
}
