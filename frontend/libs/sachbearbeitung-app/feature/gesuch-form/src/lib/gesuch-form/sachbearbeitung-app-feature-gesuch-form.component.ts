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
  inject,
  signal,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatMenuModule } from '@angular/material/menu';
import { Router, RouterLink } from '@angular/router';
import { TranslocoDirective, TranslocoPipe } from '@jsverse/transloco';
import { Store } from '@ngrx/store';
import { filter } from 'rxjs';

import { SteuerdatenStore } from '@dv/sachbearbeitung-app/data-access/steuerdaten';
import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import {
  selectRouteGesuchId,
  selectRouteTrancheId,
  selectSharedDataAccessGesuchCacheView,
  selectSharedDataAccessGesuchStepsView,
  selectSharedDataAccessGesuchsView,
} from '@dv/shared/data-access/gesuch';
import { GesuchHeaderStore } from '@dv/shared/data-access/gesuch-header';
import { NavigationStore } from '@dv/shared/data-access/navigation';
import { PermissionStore } from '@dv/shared/global/permission';
import { GesuchFormStep } from '@dv/shared/model/gesuch-form';
import { isDefined } from '@dv/shared/model/type-util';
import { SharedPatternGesuchStepNavComponent } from '@dv/shared/pattern/gesuch-step-nav';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiProgressBarComponent } from '@dv/shared/ui/progress-bar';
import { SharedUiRouterOutletWrapperComponent } from '@dv/shared/ui/router-outlet-wrapper';
import { getLatestTrancheIdFromGesuchOnUpdate$ } from '@dv/shared/util/gesuch';
import { SharedUtilGesuchFormStepManagerService } from '@dv/shared/util/gesuch-form-step-manager';
import { SharedUtilHeaderService } from '@dv/shared/util/header';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-gesuch-form',
  imports: [
    CommonModule,
    TranslocoPipe,
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

  gesuchIdSig = this.store.selectSignal(selectRouteGesuchId);
  gesuchTrancheIdSig = this.store.selectSignal(selectRouteTrancheId);

  tranchenSig = this.gesuchHeaderStore.getRelativeTranchenViewSbSig(
    this.gesuchIdSig,
  );

  // todo-before-merge: ask scph if correct
  currentTrancheWithIndexSig = computed(() => {
    const tranchenWithIndex = this.tranchenSig().map((tranche, index) => ({
      tranche,
      index,
    }));
    const trancheId = this.gesuchTrancheIdSig();

    return tranchenWithIndex.find(({ tranche }) => tranche.id === trancheId);
  });

  headerService = inject(SharedUtilHeaderService);
  stepManager = inject(SharedUtilGesuchFormStepManagerService);
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
