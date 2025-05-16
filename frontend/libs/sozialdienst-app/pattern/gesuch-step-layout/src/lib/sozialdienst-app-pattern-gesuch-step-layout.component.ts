import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  computed,
  effect,
  inject,
  input,
} from '@angular/core';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { MatMenuModule } from '@angular/material/menu';
import { MatSidenavModule } from '@angular/material/sidenav';
import { Router, RouterLink } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslatePipe, isDefined } from '@ngx-translate/core';
import { filter, map } from 'rxjs';

import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import {
  selectRouteId,
  selectRouteTrancheId,
  selectSharedDataAccessGesuchCacheView,
  selectSharedDataAccessGesuchStepsView,
  selectSharedDataAccessGesuchsView,
} from '@dv/shared/data-access/gesuch';
import { GesuchAenderungStore } from '@dv/shared/data-access/gesuch-aenderung';
import { SharedDataAccessLanguageEvents } from '@dv/shared/data-access/language';
import { PermissionStore } from '@dv/shared/global/permission';
import { GesuchFormStep } from '@dv/shared/model/gesuch-form';
import { Language } from '@dv/shared/model/language';
import { urlAfterNavigationEnd } from '@dv/shared/model/router';
import {
  SharedPatternAppHeaderComponent,
  SharedPatternAppHeaderPartsDirective,
} from '@dv/shared/pattern/app-header';
import { SharedPatternGesuchStepNavComponent } from '@dv/shared/pattern/gesuch-step-nav';
import { SharedPatternMobileSidenavComponent } from '@dv/shared/pattern/mobile-sidenav';
import { SharedUiHasRolesDirective } from '@dv/shared/ui/has-roles';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiProgressBarComponent } from '@dv/shared/ui/progress-bar';
import { getLatestTrancheIdFromGesuchOnUpdate$ } from '@dv/shared/util/gesuch';
import { SharedUtilGesuchFormStepManagerService } from '@dv/shared/util/gesuch-form-step-manager';
import { SharedUtilHeaderService } from '@dv/shared/util/header';

@Component({
  selector: 'dv-sozialdienst-app-pattern-gesuch-step-layout',
  imports: [
    CommonModule,
    RouterLink,
    TranslatePipe,
    MatSidenavModule,
    MatMenuModule,
    SharedPatternAppHeaderComponent,
    SharedPatternGesuchStepNavComponent,
    SharedPatternMobileSidenavComponent,
    SharedPatternAppHeaderPartsDirective,
    SharedUiProgressBarComponent,
    SharedUiIconChipComponent,
    SharedUiHasRolesDirective,
  ],
  templateUrl: './sozialdienst-app-pattern-gesuch-step-layout.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [SharedUtilHeaderService],
})
export class GesuchAppPatternGesuchStepLayoutComponent {
  stepSig = input<GesuchFormStep | undefined>(undefined, {
    alias: 'step',
  });

  navClicked$ = new EventEmitter<{ value: boolean }>();

  private store = inject(Store);
  private einreichenStore = inject(EinreichenStore);
  private permissionStore = inject(PermissionStore);
  private router = inject(Router);

  gesuchAenderungStore = inject(GesuchAenderungStore);
  headerService = inject(SharedUtilHeaderService);
  stepManager = inject(SharedUtilGesuchFormStepManagerService);
  gesuchIdSig = this.store.selectSignal(selectRouteId);
  trancheIdSig = this.store.selectSignal(selectRouteTrancheId);
  viewSig = this.store.selectSignal(selectSharedDataAccessGesuchsView);
  cacheViewSig = this.store.selectSignal(selectSharedDataAccessGesuchCacheView);
  stepsViewSig = this.store.selectSignal(selectSharedDataAccessGesuchStepsView);
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
    effect(
      () => {
        const gesuchId = this.gesuchIdSig();
        if (gesuchId) {
          this.gesuchAenderungStore.getAllTranchenForGesuch$({ gesuchId });
        }
      },
      { allowSignalWrites: true },
    );
  }

  handleLanguageChangeHeader(language: Language) {
    this.store.dispatch(
      SharedDataAccessLanguageEvents.headerMenuSelectorChange({ language }),
    );
  }
}
