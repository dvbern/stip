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
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslocoDirective } from '@jsverse/transloco';
import { Store } from '@ngrx/store';
import { filter, map } from 'rxjs';

import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import {
  selectRouteGesuchId,
  selectRouteTrancheId,
  selectSharedDataAccessGesuchCacheView,
  selectSharedDataAccessGesuchStepsView,
  selectSharedDataAccessGesuchsView,
  selectTrancheTyp,
} from '@dv/shared/data-access/gesuch';
import { NavigationStore } from '@dv/shared/data-access/navigation';
import { PermissionStore } from '@dv/shared/global/permission';
import { GesuchFormStep } from '@dv/shared/model/gesuch-form';
import { urlAfterNavigationEnd } from '@dv/shared/model/router';
import { isDefined } from '@dv/shared/model/type-util';
import { SharedPatternGesuchStepNavComponent } from '@dv/shared/pattern/gesuch-step-nav';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiProgressBarComponent } from '@dv/shared/ui/progress-bar';
import { SharedUiRouterOutletWrapperComponent } from '@dv/shared/ui/router-outlet-wrapper';
import { getLatestTrancheIdFromGesuchOnUpdate$ } from '@dv/shared/util/gesuch';
import { SharedUtilGesuchFormStepManagerService } from '@dv/shared/util/gesuch-form-step-manager';
import { SharedUtilHeaderService } from '@dv/shared/util/header';
import { createStepFallbackRouteEffect } from '@dv/shared/util/navigation';

@Component({
  selector: 'dv-shared-feature-gesuch-form',
  imports: [
    SharedUiRouterOutletWrapperComponent,
    CommonModule,
    SharedPatternGesuchStepNavComponent,
    SharedUiProgressBarComponent,
    SharedUiIconChipComponent,
    TranslocoDirective,
    PortalModule,
  ],
  templateUrl: './shared-feature-gesuch-form.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [SharedUtilHeaderService],
})
export class SharedFeatureGesuchFormComponent
  implements AfterViewInit, OnDestroy
{
  @HostBinding('class') klass = 'tw:dv-pass-height';
  @ViewChild(CdkPortal)
  portalContent: CdkPortal | null = null;

  private store = inject(Store);
  private einreichenStore = inject(EinreichenStore);
  private permissionStore = inject(PermissionStore);
  private navigationStore = inject(NavigationStore);
  private route = inject(ActivatedRoute);

  router = inject(Router);
  headerService = inject(SharedUtilHeaderService);
  stepManager = inject(SharedUtilGesuchFormStepManagerService);
  gesuchIdSig = this.store.selectSignal(selectRouteGesuchId);
  trancheIdSig = this.store.selectSignal(selectRouteTrancheId);
  trancheTypSig = this.store.selectSignal(selectTrancheTyp);
  cacheViewSig = this.store.selectSignal(selectSharedDataAccessGesuchCacheView);
  stepsViewSig = this.store.selectSignal(selectSharedDataAccessGesuchStepsView);

  viewSig = this.store.selectSignal(selectSharedDataAccessGesuchsView);

  stepSig = signal<GesuchFormStep | undefined>(undefined);
  stepsSig = computed(() => {
    const { cache, trancheTyp } = this.cacheViewSig();
    const { invalidFormularProps } = this.einreichenStore.validationViewSig();
    const steps = this.stepsViewSig().steps;
    const rolesMap = this.permissionStore.rolesMapSig();
    const validatedSteps = this.stepManager.getValidatedSteps(
      steps,
      trancheTyp,
      cache.gesuch,
      rolesMap,
      undefined,
      invalidFormularProps.validations,
    );
    return validatedSteps;
  });
  currentStepProgressSig = computed(() => {
    const stepsFlow = this.stepsViewSig().stepsFlow;
    return this.stepManager.getStepProgress(stepsFlow, this.stepSig());
  });
  currentStepSig = computed(() => {
    const steps = this.stepsSig();
    return steps.find((step) => step.route === this.stepSig()?.route);
  });
  isTrancheRouteSig = toSignal(
    urlAfterNavigationEnd(this.router).pipe(
      map((url) => url.includes('/tranche/')),
    ),
  );

  ngAfterViewInit(): void {
    this.navigationStore.setPortal(this.portalContent);
  }
  ngOnDestroy() {
    if (this.portalContent?.isAttached) {
      this.portalContent.detach();
    }
  }

  constructor() {
    createStepFallbackRouteEffect({
      router: this.router,
      stepSig: this.stepSig,
      currentStepSig: this.currentStepSig,
      loadingSig: computed(() => this.viewSig().loading),
      gesuchIdSig: this.gesuchIdSig,
      trancheIdSig: this.trancheIdSig,
      trancheTypSig: this.trancheTypSig,
    });

    getLatestTrancheIdFromGesuchOnUpdate$(this.viewSig)
      .pipe(filter(isDefined), takeUntilDestroyed())
      .subscribe((gesuchTrancheId) => {
        this.einreichenStore.validateSteps$({ gesuchTrancheId });
      });
  }
}
