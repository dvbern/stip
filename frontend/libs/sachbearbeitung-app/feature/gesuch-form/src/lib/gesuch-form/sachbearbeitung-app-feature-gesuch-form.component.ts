import { CdkPortal, PortalModule } from '@angular/cdk/portal';
import { CommonModule } from '@angular/common';
import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  OnDestroy,
  ViewChild,
  computed,
  inject,
  signal,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatMenuModule } from '@angular/material/menu';
import { RouterLink } from '@angular/router';
import { TranslocoDirective } from '@jsverse/transloco';
import { Store } from '@ngrx/store';
import { filter } from 'rxjs';

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
import { GesuchUrlType } from '@dv/shared/model/gesuch';
import { GesuchFormStep } from '@dv/shared/model/gesuch-form';
import { isDefined } from '@dv/shared/model/type-util';
import { SharedPatternGesuchStepNavComponent } from '@dv/shared/pattern/gesuch-step-nav';
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

  revisionSig = this.store.selectSignal(selectRevision);
  headerService = inject(SharedUtilHeaderService);
  stepManager = inject(SharedUtilGesuchFormStepManagerService);
  gesuchIdSig = this.store.selectSignal(selectRouteGesuchId);
  gesuchTrancheIdSig = this.store.selectSignal(selectRouteTrancheId);
  tranchenSig = this.gesuchHeaderStore.getRelativeTranchenViewSbSig(
    this.gesuchIdSig,
  );
  viewSig = this.store.selectSignal(selectSharedDataAccessGesuchsView);
  cacheViewSig = this.store.selectSignal(selectSharedDataAccessGesuchCacheView);
  stepsViewSig = this.store.selectSignal(selectSharedDataAccessGesuchStepsView);
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

  // todo-after-merge: @scph: not working!
  // currentTrancheWithIndexSig = computed(() => {
  //   const tranchenWithIndex = this.tranchenSig().map((tranche, index) => ({
  //     tranche,
  //     index,
  //   }));
  //   const trancheId = this.gesuchTrancheIdSig();

  //   return tranchenWithIndex.find(({ tranche }) => tranche.id === trancheId);
  // });

  // todo-after-merge: by moving the header into duplication!
  currentTrancheSig = computed(() => {
    const trancheId = this.gesuchTrancheIdSig();
    const tranchen = this.tranchenSig();

    return trancheId && tranchen
      ? tranchen.find((tranche) => tranche.id === trancheId)
      : undefined;
  });
  currentTrancheNumberSig = computed(() => {
    const { trancheSetting } = this.viewSig();
    const currentTranche = this.currentTrancheSig();

    const { list, isLoading } = this.aenderungStore.tranchenListViewSig();

    if (!currentTranche || isLoading) {
      return '…';
    }

    const { currentTranchen, historized } = list ?? {};

    const gesuchUrlTyp = trancheSetting?.gesuchUrlTyp;
    const allTranchen = {
      TRANCHE: [currentTranchen ?? []],
      AENDERUNG: [
        historized?.akzeptierteAenderungen?.map((a) => a.aenderung) ?? [],
        historized?.abgelehnteAenderungen ?? [],
      ],
      INITIAL: [historized?.initial?.tranchen ?? []],
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
    this.portalContent?.detach();
  }

  constructor() {
    getLatestTrancheIdFromGesuchOnUpdate$(this.viewSig)
      .pipe(filter(isDefined), takeUntilDestroyed())
      .subscribe((gesuchTrancheId) => {
        this.einreichenStore.validateSteps$({ gesuchTrancheId });
        this.steuerdatenStore.getSteuerdaten$({ gesuchTrancheId });
      });
  }
}
