import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  Directive,
  EventEmitter,
  HostBinding,
  Output,
  computed,
  effect,
  inject,
  input,
  signal,
} from '@angular/core';
import { MatExpansionModule } from '@angular/material/expansion';
import { Params, Router, RouterLink, isActive } from '@angular/router';
import { TranslocoPipe } from '@jsverse/transloco';
import { Store } from '@ngrx/store';

import { isHistorizedView } from '@dv/shared/data-access/gesuch';
import {
  GesuchFormStepView,
  StepGroup,
  StepState,
} from '@dv/shared/model/gesuch-form';
import { SharedUiChangeIndicatorComponent } from '@dv/shared/ui/change-indicator';
import { stepHasChanges } from '@dv/shared/util-fn/gesuch-util';

import { sharedPatternGesuchStepNavView } from './shared-pattern-gesuch-step-nav.selectors';

type StepView = {
  hasChanges: boolean | undefined;
  name: string;
  routerLink: (string | null)[] | null;
  queryParams: Params;
  active: () => boolean;
  group?: StepGroup;
  statusIconSymbolName?: string;
  marginTop?: boolean;
} & GesuchFormStepView;

type GroupEntry = {
  type: 'group';
  group: StepGroup;
  steps: StepView[];
  hasActive: boolean;
  hasChanges: boolean;
  borderTop?: boolean;
  groupStatus?: StepState;
};

type StandaloneEntry = {
  type: 'standalone';
  step: StepView;
};

type NavEntry = GroupEntry | StandaloneEntry;

@Directive({ selector: 'ng-template[dvStepView]', standalone: true })
export class StepViewTemplateDirective {
  static ngTemplateContextGuard(
    _dir: StepViewTemplateDirective,
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    ctx: unknown,
  ): ctx is { step: StepView } {
    return true;
  }
}

@Component({
  selector: 'dv-shared-pattern-gesuch-step-nav',
  imports: [
    CommonModule,
    RouterLink,
    TranslocoPipe,
    SharedUiChangeIndicatorComponent,
    MatExpansionModule,
    StepViewTemplateDirective,
  ],
  templateUrl: './shared-pattern-gesuch-step-nav.component.html',
  styleUrls: ['./shared-pattern-gesuch-step-nav.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedPatternGesuchStepNavComponent {
  @HostBinding('class') klass = 'tw:dv-pass-height tw:h-full tw:p-6';
  @Output() navClicked = new EventEmitter();
  private store = inject(Store);

  statusIconMap: Record<StepState, string> = {
    VALID: 'check_circle',
    INVALID: 'error',
    WARNING: 'error',
  };

  isHistorizedSig = this.store.selectSignal(isHistorizedView);
  stepsSig = input<GesuchFormStepView[]>();
  viewSig = this.store.selectSignal(sharedPatternGesuchStepNavView);
  route = inject(Router);

  openedGroupsSig = signal<Record<StepGroup, boolean>>({
    PERSOENLICHE_ANGABEN: false,
    FAMILIENANGABEN: false,
  });

  stepsViewSig = computed<StepView[] | undefined>(() => {
    const { cachedGesuchId, trancheSetting, tranchenChanges } = this.viewSig();
    return this.stepsSig()?.map((step) => ({
      ...step,
      hasChanges: stepHasChanges(tranchenChanges, step),
      name: step.route,
      routerLink: trancheSetting
        ? [
            '/',
            'gesuch',
            ...step.route.split('/'),
            cachedGesuchId,
            ...trancheSetting.routesSuffix,
          ]
        : null,
      queryParams: { formularTab: step.route },
      active: isActive(`gesuch/${step.route}`, this.route, {
        paths: 'subset',
        queryParams: 'ignored',
        fragment: 'ignored',
        matrixParams: 'ignored',
      }),
      statusIconSymbolName: step.status
        ? this.statusIconMap[step.status]
        : undefined,
    }));
  });

  groupedStepsViewSig = computed<NavEntry[]>(() => {
    const steps = this.stepsViewSig() ?? [];

    return steps.reduce<NavEntry[]>((acc, step) => {
      const last = acc[acc.length - 1];
      if (step.group && last?.type === 'group' && last.group === step.group) {
        last.steps.push(step);
        if (step.active()) last.hasActive = true;
        if (step.hasChanges) last.hasChanges = true;
        last.groupStatus = this.mergeGroupStatus(last.groupStatus, step.status);
        return acc;
      }
      return [
        ...acc,
        step.group
          ? {
              type: 'group',
              group: step.group,
              steps: [step],
              hasActive: step.active(),
              hasChanges: !!step.hasChanges,
              borderTop: last?.type === 'standalone',
              groupStatus: step.status,
            }
          : {
              type: 'standalone',
              step:
                last?.type === 'group' ? { ...step, marginTop: true } : step,
            },
      ];
    }, []);
  });

  private mergeGroupStatus(
    current: StepState | undefined,
    next: StepState | undefined,
  ): StepState | undefined {
    if (current === 'INVALID' || next === 'INVALID') return 'INVALID';
    if (current === 'WARNING' || next === 'WARNING') return 'WARNING';
    if (current === 'VALID' || next === 'VALID') return 'VALID';
    return undefined;
  }

  constructor() {
    effect(() => {
      const grouped = this.groupedStepsViewSig();
      for (const entry of grouped) {
        if (entry.type === 'group' && entry.hasActive) {
          this.openedGroupsSig.update((rec) => {
            if (rec[entry.group]) return rec;
            return { ...rec, [entry.group]: true };
          });
        }
      }
    });
  }
}
