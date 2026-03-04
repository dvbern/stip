import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  computed,
  inject,
  signal,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { TranslocoDirective, TranslocoPipe } from '@jsverse/transloco';
import { Store } from '@ngrx/store';
import { filter } from 'rxjs';

import { SteuerdatenStore } from '@dv/sachbearbeitung-app/data-access/steuerdaten';
import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import {
  selectRouteId,
  selectRouteTrancheId,
  selectSharedDataAccessGesuchCacheView,
  selectSharedDataAccessGesuchStepsView,
  selectSharedDataAccessGesuchsView,
} from '@dv/shared/data-access/gesuch';
import { GesuchHeaderStore } from '@dv/shared/data-access/gesuch-header';
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
  ],
  templateUrl: './sachbearbeitung-app-feature-gesuch-form.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [SharedUtilHeaderService],
})
export class SachbearbeitungAppFeatureGesuchFormComponent {
  // currentStep?: GesuchFormStep;
  stepSig = signal<GesuchFormStep | undefined>(undefined);

  navClicked$ = new EventEmitter();

  private store = inject(Store);
  private einreichenStore = inject(EinreichenStore);
  private permissionStore = inject(PermissionStore);
  private steuerdatenStore = inject(SteuerdatenStore);
  private gesuchHeaderStore = inject(GesuchHeaderStore);
  // todo: rename to gesuchRouteId (also rename route param?)
  gesuchIdSig = this.store.selectSignal(selectRouteId);

  tranchenSig = this.gesuchHeaderStore.getRelativeTranchenViewSbSig(
    this.gesuchIdSig,
  );
  gesuchTrancheIdSig = this.store.selectSignal(selectRouteTrancheId);

  // private gesuchUpdatedSig = toSignal(
  //   this.store.select(selectSharedDataAccessGesuchCache).pipe(
  //     map(({ gesuch }) => gesuch),
  //     filter(isDefined),
  //   ),
  // );

  // gesuchInfoSig = computed(() => {
  //   const cache = this.store.selectSignal(selectSharedDataAccessGesuchCache)();

  //   return cache;
  // });

  // headerViewSbSig: Signal<{ isLoading: boolean } & Partial<GesuchHeaderSb>> =
  //   this.gesuchHeaderStore.viewSbSig;

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

  constructor() {
    getLatestTrancheIdFromGesuchOnUpdate$(this.viewSig)
      .pipe(filter(isDefined), takeUntilDestroyed())
      .subscribe((gesuchTrancheId) => {
        this.einreichenStore.validateSteps$({ gesuchTrancheId });
        this.steuerdatenStore.getSteuerdaten$({ gesuchTrancheId });
      });
    // effect(() => {
    //   const gesuchTrancheId = this.gesuchTrancheIdSig();
    //   this.gesuchUpdatedSig();
    //   if (gesuchTrancheId) {
    //     this.gesuchHeaderStore.loadHeaderSb$({ gesuchTrancheId });
    //   }
    // });
  }
}
