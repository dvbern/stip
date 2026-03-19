import { CdkPortal, PortalModule } from '@angular/cdk/portal';
import { CommonModule } from '@angular/common';
import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  OnDestroy,
  Signal,
  ViewChild,
  computed,
  inject,
  signal,
} from '@angular/core';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { MatMenuModule } from '@angular/material/menu';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { TranslocoDirective } from '@jsverse/transloco';
import { Store } from '@ngrx/store';
import { filter, map, startWith } from 'rxjs';

import { SteuerdatenStore } from '@dv/sachbearbeitung-app/data-access/steuerdaten';
import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import {
  selectRevision,
  selectRouteGesuchId,
  selectRouteTrancheId,
  selectSharedDataAccessGesuchCacheView,
  selectSharedDataAccessGesuchStepsView,
  selectSharedDataAccessGesuchsView,
} from '@dv/shared/data-access/gesuch';
import { GesuchAenderungStore } from '@dv/shared/data-access/gesuch-aenderung';
import { GesuchHeaderStore } from '@dv/shared/data-access/gesuch-header';
import { NavigationStore } from '@dv/shared/data-access/navigation';
import { PermissionStore } from '@dv/shared/global/permission';
import { GesuchHeader, GesuchUrlType } from '@dv/shared/model/gesuch';
import { GesuchFormStep } from '@dv/shared/model/gesuch-form';
import { urlAfterNavigationEnd } from '@dv/shared/model/router';
import { isDefined } from '@dv/shared/model/type-util';
import { SharedPatternGesuchStepNavComponent } from '@dv/shared/pattern/gesuch-step-nav';
import { SharedUiAenderungenMenuComponent } from '@dv/shared/ui/aenderungen-menu';
import { SharedUiDarlehenMenuComponent } from '@dv/shared/ui/darlehen-menu';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiProgressBarComponent } from '@dv/shared/ui/progress-bar';
import { SharedUiRouterOutletWrapperComponent } from '@dv/shared/ui/router-outlet-wrapper';
import { getLatestTrancheIdFromGesuchOnUpdate$ } from '@dv/shared/util/gesuch';
import { SharedUtilGesuchFormStepManagerService } from '@dv/shared/util/gesuch-form-step-manager';
import { SharedUtilHeaderService } from '@dv/shared/util/header';
import { findIndexInOneOf } from '@dv/shared/util-fn/array-helper';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-gesuch-form',
  imports: [
    CommonModule,
    SharedUiRouterOutletWrapperComponent,
    SharedPatternGesuchStepNavComponent,
    SharedUiIconChipComponent,
    SharedUiProgressBarComponent,
    TranslocoDirective,
    MatMenuModule,
    RouterLink,
    PortalModule,
    SharedUiAenderungenMenuComponent,
    SharedUiDarlehenMenuComponent,
  ],
  templateUrl: './sachbearbeitung-app-feature-gesuch-form.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [SharedUtilHeaderService],
})
export class SachbearbeitungAppFeatureGesuchFormComponent
  implements AfterViewInit, OnDestroy
{
  @HostBinding('class') klass = 'tw:dv-pass-height';
  @ViewChild(CdkPortal)
  portalContent: CdkPortal | null = null;

  stepSig = signal<GesuchFormStep | undefined>(undefined);

  private store = inject(Store);
  private einreichenStore = inject(EinreichenStore);
  private permissionStore = inject(PermissionStore);
  private steuerdatenStore = inject(SteuerdatenStore);
  private gesuchHeaderStore = inject(GesuchHeaderStore);
  private navigationStore = inject(NavigationStore);
  private aenderungStore = inject(GesuchAenderungStore);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  revisionSig = this.store.selectSignal(selectRevision);
  headerService = inject(SharedUtilHeaderService);
  stepManager = inject(SharedUtilGesuchFormStepManagerService);
  gesuchIdSig = this.store.selectSignal(selectRouteGesuchId);
  gesuchTrancheIdSig = this.store.selectSignal(selectRouteTrancheId);
  berechnungIdSig = toSignal(
    this.route.queryParamMap.pipe(map((params) => params.get('berechnungId'))),
  );
  viewSig = this.store.selectSignal(selectSharedDataAccessGesuchsView);
  cacheViewSig = this.store.selectSignal(selectSharedDataAccessGesuchCacheView);
  stepsViewSig = this.store.selectSignal(selectSharedDataAccessGesuchStepsView);
  trancheIdSig = this.store.selectSignal(selectRouteTrancheId);

  headerViewSig: Signal<{ isLoading: boolean } & Partial<GesuchHeader>> =
    this.gesuchHeaderStore.viewSig;

  routeUrlSig = toSignal(
    urlAfterNavigationEnd(this.router).pipe(
      map(() => this.router.routerState.snapshot.url),
      startWith(this.router.routerState.snapshot.url),
    ),
  );

  isAenderungRouteSig = computed(() => {
    const url = this.routeUrlSig();
    return url?.includes('/aenderung/') ?? false;
  });

  isInitialRouteSig = computed(() => {
    const url = this.routeUrlSig();
    return url?.includes('/initial/') ?? false;
  });

  noGesuchActiveRoutes = ['aenderung', 'infos', 'darlehen'];
  isGesuchRouteSig = computed(() => {
    const url = this.routeUrlSig();
    return !this.noGesuchActiveRoutes.some((route) =>
      url?.includes(`/${route}/`),
    );
  });

  stepsSig = computed(() => {
    const { invalidFormularProps } = this.einreichenStore.validationViewSig();
    const rolesMap = this.permissionStore.rolesMapSig();
    const { cache, trancheTyp } = this.cacheViewSig();
    const steps = this.stepsViewSig().steps;
    const steuerdaten = this.steuerdatenStore.cachedSteuerdatenListViewSig();

    return this.stepManager.getValidatedSteps(
      steps,
      trancheTyp,
      cache.gesuch,
      rolesMap,
      steuerdaten,
      invalidFormularProps.validations,
    );
  });
  currentStepProgressSig = computed(() => {
    const currentStep = this.stepSig();
    const stepsFlow = this.stepsViewSig().stepsFlow;
    return this.stepManager.getStepProgress(stepsFlow, currentStep);
  });
  currentStepSig = computed(() => {
    const currentStep = this.stepSig();
    const steps = this.stepsSig();
    return steps.find((step) => step.route === currentStep?.route);
  });

  // tranchenSig = this.gesuchHeaderStore.getRelativeTranchenViewSig(
  //   this.gesuchIdSig,
  // );

  // todo: could be integrated into tranchenSig?
  // currentVersionTranchenSig = computed(() => {
  //   const berechnungId = this.berechnungIdSig();
  //   const versions = this.gesuchHeaderStore.header().data?.versions;

  //   if (berechnungId && versions) {
  //     const version = versions.find(
  //       (version) => version.berechnungId === berechnungId,
  //     );
  //     return version?.tranchen;
  //   }
  //   return undefined;
  // });

  // testing without getRealativeTrancheViewSig
  tranchenSig = computed(() => {
    const current = this.gesuchHeaderStore.header().data?.currentTranches;
    const versions = this.gesuchHeaderStore.viewSig().versions;
    const berechnungId = this.berechnungIdSig();

    if (berechnungId) {
      const version = versions?.find(
        (version) => version.berechnungId === berechnungId,
      );
      return version?.tranchen;
    }

    if (this.isInitialRouteSig()) {
      return this.gesuchHeaderStore.viewSig().initial?.tranchen;
    }

    return current;
  });

  currentTrancheSig = computed(() => {
    const trancheId = this.gesuchTrancheIdSig();
    const tranchen = this.tranchenSig();

    return trancheId && tranchen
      ? tranchen.find((tranche) => tranche.id === trancheId)
      : undefined;
  });

  // todo-after-merge: by moving the header => make reusable
  currentTrancheNumberSig = computed(() => {
    const { trancheSetting } = this.viewSig();
    const currentTranche = this.currentTrancheSig();

    const { currentTranches, aenderungs, initial, isLoading } =
      this.gesuchHeaderStore.viewSig();

    if (!currentTranche || isLoading) {
      return '…';
    }

    const gesuchUrlTyp = trancheSetting?.gesuchUrlTyp;
    const allTranchen = {
      TRANCHE: [currentTranches ?? []],
      AENDERUNG: [aenderungs?.akzeptiert ?? [], aenderungs?.abgelehnt ?? []],
      INITIAL: [initial?.tranchen ?? []],
    } satisfies Record<GesuchUrlType, unknown>;
    const index = gesuchUrlTyp
      ? findIndexInOneOf(
          (tranche) =>
            tranche.id === currentTranche.id &&
            isDefined(tranche.revision) === isDefined(this.revisionSig()),
          ...allTranchen[gesuchUrlTyp],
        )
      : -1;

    const foundIndex = index >= 0 ? index + 1 : null;
    if (foundIndex) {
      return foundIndex;
    }
    return gesuchUrlTyp !== 'AENDERUNG' ? '...' : null;
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
    // log effect
    // effect(() => {
    //   console.log('berechnungId', this.berechnungIdSig());
    //   console.log('relativeTranchen', this.tranchenSig());
    // });

    getLatestTrancheIdFromGesuchOnUpdate$(this.viewSig)
      .pipe(filter(isDefined), takeUntilDestroyed())
      .subscribe((gesuchTrancheId) => {
        this.einreichenStore.validateSteps$({ gesuchTrancheId });
        this.steuerdatenStore.getSteuerdaten$({ gesuchTrancheId });
      });
  }
}
