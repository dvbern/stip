import { CdkPortal, PortalModule } from '@angular/cdk/portal';
import { CommonModule } from '@angular/common';
import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  HostBinding,
  OnDestroy,
  ViewChild,
  computed,
  effect,
  inject,
} from '@angular/core';
import { MatSidenavModule } from '@angular/material/sidenav';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { TranslocoDirective } from '@jsverse/transloco';
import { Store } from '@ngrx/store';

import {
  VERFUEGUNG_OPTIONS,
  VerfuegungOption,
  createBerechnungOption,
} from '@dv/sachbearbeitung-app/model/verfuegung';
import { BerechnungStore } from '@dv/shared/data-access/berechnung';
import {
  selectRouteGesuchId,
  selectRouteTrancheId,
  selectTrancheTyp,
} from '@dv/shared/data-access/gesuch';
import { GesuchInfoStore } from '@dv/shared/data-access/gesuch-info';
import { NavigationStore } from '@dv/shared/data-access/navigation';
import { PermissionStore } from '@dv/shared/global/permission';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import { getGesuchPermissions } from '@dv/shared/model/permission-state';
import { lowercased } from '@dv/shared/model/type-util';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiRouterOutletWrapperComponent } from '@dv/shared/ui/router-outlet-wrapper';
import { isPending } from '@dv/shared/util/remote-data';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-verfuegung',
  imports: [
    SharedUiRouterOutletWrapperComponent,
    CommonModule,
    RouterLink,
    RouterLinkActive,
    MatSidenavModule,
    SharedUiIconChipComponent,
    TranslocoDirective,
    PortalModule,
  ],
  templateUrl: './sachbearbeitung-app-feature-verfuegung.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureVerfuegungComponent
  implements AfterViewInit, OnDestroy
{
  @HostBinding('class') klass = 'tw:dv-pass-height';
  @ViewChild(CdkPortal)
  portalContent: CdkPortal | null = null;

  option?: VerfuegungOption;

  navClicked$ = new EventEmitter();
  route = inject(Router);
  verfuegungOptions = VERFUEGUNG_OPTIONS;

  private store = inject(Store);
  private gesuchInfoStore = inject(GesuchInfoStore);
  private berechnungStore = inject(BerechnungStore);
  private permissionStore = inject(PermissionStore);
  private navigationStore = inject(NavigationStore);
  private config = inject(SharedModelCompileTimeConfig);

  gesuchIdSig = this.store.selectSignal(selectRouteGesuchId);
  trancheIdSig = this.store.selectSignal(selectRouteTrancheId);
  private trancheTypRawSig = this.store.selectSignal(selectTrancheTyp);
  trancheTypSig = computed(() => {
    const typ = this.trancheTypRawSig();

    return lowercased(typ ?? 'tranche');
  });
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
    const trancheId = this.trancheIdSig();
    const trancheTyp = this.trancheTypSig();
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
      fullRoute: [
        '/gesuch',
        'verfuegung',
        gesuchId,
        trancheTyp,
        trancheId,
        ...option.route.split('/'),
      ],
    }));
  });

  ngAfterViewInit(): void {
    this.navigationStore.setPortal(this.portalContent);
  }
  ngOnDestroy() {
    if (this.portalContent?.isAttached) {
      this.portalContent.detach();
    }
  }

  constructor() {
    effect(() => {
      const gesuchId = this.gesuchIdSig();
      if (gesuchId) {
        this.gesuchInfoStore.loadGesuchInfo$({ gesuchId });
      }
    });
  }
}
