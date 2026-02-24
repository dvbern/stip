import { CdkPortal, PortalModule } from '@angular/cdk/portal';
import { CommonModule } from '@angular/common';
import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  OnDestroy,
  ViewChild,
  computed,
  effect,
  inject,
  input,
} from '@angular/core';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { MatMenuModule } from '@angular/material/menu';
import { Router, RouterLink } from '@angular/router';
import { Store } from '@ngrx/store';
import { filter, map } from 'rxjs';

import { GesuchAppUiAdvTranslocoDirective } from '@dv/gesuch-app/ui/adv-transloco-directive';
import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import {
  selectRouteId,
  selectRouteTrancheId,
  selectSharedDataAccessGesuchCacheView,
  selectSharedDataAccessGesuchStepsView,
  selectSharedDataAccessGesuchsView,
} from '@dv/shared/data-access/gesuch';
import { GesuchHeaderStore } from '@dv/shared/data-access/gesuch-header';
import {
  SharedDataAccessLanguageEvents,
  selectLanguage,
} from '@dv/shared/data-access/language';
import { NavigationStore } from '@dv/shared/data-access/navigation';
import { PermissionStore } from '@dv/shared/global/permission';
import { GesuchFormStep } from '@dv/shared/model/gesuch-form';
import { Language } from '@dv/shared/model/language';
import { urlAfterNavigationEnd } from '@dv/shared/model/router';
import { isDefined } from '@dv/shared/model/type-util';
import { SharedPatternAppHeaderPartsDirective } from '@dv/shared/pattern/app-header';
import { SharedPatternGesuchStepNavComponent } from '@dv/shared/pattern/gesuch-step-nav';
import { SharedPatternMainLayoutComponent } from '@dv/shared/pattern/main-layout';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiProgressBarComponent } from '@dv/shared/ui/progress-bar';
import { getLatestTrancheIdFromGesuchOnUpdate$ } from '@dv/shared/util/gesuch';
import { SharedUtilGesuchFormStepManagerService } from '@dv/shared/util/gesuch-form-step-manager';
import { SharedUtilHeaderService } from '@dv/shared/util/header';

@Component({
  selector: 'dv-gesuch-app-pattern-gesuch-step-layout',
  imports: [
    CommonModule,
    SharedPatternGesuchStepNavComponent,
    SharedUiProgressBarComponent,
    MatMenuModule,
    SharedUiIconChipComponent,
    SharedPatternMainLayoutComponent,
    SharedPatternAppHeaderPartsDirective,
    GesuchAppUiAdvTranslocoDirective,
    RouterLink,
    PortalModule,
  ],
  templateUrl: './gesuch-app-pattern-gesuch-step-layout.component.html',
  styleUrls: ['./gesuch-app-pattern-gesuch-step-layout.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [SharedUtilHeaderService],
})
export class GesuchAppPatternGesuchStepLayoutComponent
  implements OnDestroy, AfterViewInit
{
  stepSig = input<GesuchFormStep | undefined>(undefined, {
    // eslint-disable-next-line @angular-eslint/no-input-rename
    alias: 'step',
  });

  // navClicked = new EventEmitter<{ value: boolean }>();
  private navigationStore = inject(NavigationStore);

  @ViewChild(CdkPortal)
  portalContent: CdkPortal | null = null;

  ngAfterViewInit(): void {
    this.navigationStore.setPortal(this.portalContent);
  }
  ngOnDestroy() {
    this.portalContent?.detach();
  }

  private store = inject(Store);
  private einreichenStore = inject(EinreichenStore);
  private gesuchHeaderStore = inject(GesuchHeaderStore);
  private permissionStore = inject(PermissionStore);

  router = inject(Router);
  headerService = inject(SharedUtilHeaderService);
  stepManager = inject(SharedUtilGesuchFormStepManagerService);
  gesuchIdSig = this.store.selectSignal(selectRouteId);
  trancheIdSig = this.store.selectSignal(selectRouteTrancheId);
  languageSig = this.store.selectSignal(selectLanguage);
  viewSig = this.store.selectSignal(selectSharedDataAccessGesuchsView);
  cacheViewSig = this.store.selectSignal(selectSharedDataAccessGesuchCacheView);
  stepsViewSig = this.store.selectSignal(selectSharedDataAccessGesuchStepsView);
  tranchenSig = this.gesuchHeaderStore.getRelativeTranchenViewGsSig(
    this.gesuchIdSig,
  );
  fallIdSig = computed(() => {
    const { cache } = this.cacheViewSig();
    return cache.gesuch?.fallId;
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
