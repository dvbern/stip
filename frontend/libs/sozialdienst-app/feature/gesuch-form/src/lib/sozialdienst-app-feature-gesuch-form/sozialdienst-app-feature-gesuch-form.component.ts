import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  computed,
  effect,
  inject,
  signal,
} from '@angular/core';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { Router } from '@angular/router';
import { TranslocoDirective, TranslocoPipe } from '@jsverse/transloco';
import { Store } from '@ngrx/store';
import { filter, map } from 'rxjs';

import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import {
  selectRouteId,
  selectRouteTrancheId,
  selectSharedDataAccessGesuchCacheView,
  selectSharedDataAccessGesuchStepsView,
  selectSharedDataAccessGesuchsView,
} from '@dv/shared/data-access/gesuch';
import { GesuchHeaderStore } from '@dv/shared/data-access/gesuch-header';
import { SharedDataAccessLanguageEvents } from '@dv/shared/data-access/language';
import { PermissionStore } from '@dv/shared/global/permission';
import { GesuchFormStep } from '@dv/shared/model/gesuch-form';
import { Language } from '@dv/shared/model/language';
import { urlAfterNavigationEnd } from '@dv/shared/model/router';
import { isDefined } from '@dv/shared/model/type-util';
import { SharedPatternGesuchStepNavComponent } from '@dv/shared/pattern/gesuch-step-nav';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiProgressBarComponent } from '@dv/shared/ui/progress-bar';
import { SharedUiRouterOutletWrapperComponent } from '@dv/shared/ui/router-outlet-wrapper';
import { getLatestTrancheIdFromGesuchOnUpdate$ } from '@dv/shared/util/gesuch';
import { SharedUtilGesuchFormStepManagerService } from '@dv/shared/util/gesuch-form-step-manager';
import { SharedUtilHeaderService } from '@dv/shared/util/header';

// todo: unify with gesuch-app-feature-gesuch-form component

@Component({
  selector: 'dv-sozialdienst-app-feature-gesuch-form',
  imports: [
    SharedUiRouterOutletWrapperComponent,
    CommonModule,
    TranslocoPipe,
    SharedPatternGesuchStepNavComponent,
    SharedUiProgressBarComponent,
    SharedUiIconChipComponent,
    TranslocoDirective,
  ],
  templateUrl: './sozialdienst-app-feature-gesuch-form.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SozialdienstAppFeatureGesuchFormComponent {
  stepSig = signal<GesuchFormStep | undefined>(undefined);

  navClicked$ = new EventEmitter<{ value: boolean }>();

  private store = inject(Store);
  private einreichenStore = inject(EinreichenStore);
  private gesuchHeaderStore = inject(GesuchHeaderStore);
  private permissionStore = inject(PermissionStore);

  router = inject(Router);
  headerService = inject(SharedUtilHeaderService);
  stepManager = inject(SharedUtilGesuchFormStepManagerService);
  gesuchIdSig = this.store.selectSignal(selectRouteId);
  trancheIdSig = this.store.selectSignal(selectRouteTrancheId);
  viewSig = this.store.selectSignal(selectSharedDataAccessGesuchsView);
  cacheViewSig = this.store.selectSignal(selectSharedDataAccessGesuchCacheView);
  stepsViewSig = this.store.selectSignal(selectSharedDataAccessGesuchStepsView);
  // tranchenSig = this.gesuchHeaderStore.getRelativeTranchenViewGsSig(
  //   this.gesuchIdSig,
  // );
  fallIdSig = computed(() => {
    const gesuch = this.cacheViewSig().cache.gesuch;
    return gesuch?.fallId;
  });
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

  constructor() {
    getLatestTrancheIdFromGesuchOnUpdate$(this.viewSig)
      .pipe(filter(isDefined), takeUntilDestroyed())
      .subscribe((gesuchTrancheId) => {
        this.einreichenStore.validateSteps$({ gesuchTrancheId });
      });
    effect(() => {
      const gesuchTrancheId = this.trancheIdSig();
      if (gesuchTrancheId) {
        this.gesuchHeaderStore.loadHeaderGs$({ gesuchTrancheId });
      }
    });
  }

  handleLanguageChangeHeader(language: Language) {
    this.store.dispatch(
      SharedDataAccessLanguageEvents.headerMenuSelectorChange({ language }),
    );
  }
}
