import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
} from '@angular/core';
import { MatMenuModule } from '@angular/material/menu';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { TranslocoDirective, TranslocoPipe } from '@jsverse/transloco';

import {
  DarlehenStore,
  canCreateDarlehenFn,
} from '@dv/shared/data-access/darlehen';
import { DashboardStore } from '@dv/shared/data-access/dashboard';
import { FallStore } from '@dv/shared/data-access/fall';
import { SharedFeatureDarlehenComponent } from '@dv/shared/feature/darlehen';
import { SharedPatternMainLayoutComponent } from '@dv/shared/pattern/main-layout';

@Component({
  selector: 'dv-sozialdienst-app-feature-darlehen',
  imports: [
    CommonModule,
    RouterLink,
    TranslocoDirective,
    SharedPatternMainLayoutComponent,
    SharedFeatureDarlehenComponent,
    MatMenuModule,
    TranslocoPipe,
  ],
  templateUrl: './sozialdienst-app-feature-darlehen.component.html',
  styleUrl: './sozialdienst-app-feature-darlehen.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SozialdienstAppFeatureDarlehenComponent {
  darlehenStore = inject(DarlehenStore);
  dashboardStore = inject(DashboardStore);
  route = inject(ActivatedRoute);
  fallStore = inject(FallStore);
  hasUnsavedChanges = false;

  darlehenIdRouteSig = computed(() => {
    return this.route.snapshot.paramMap.get('darlehenId');
  });

  // todo: improve this check, maybe move to helper or backend
  canCreateDarlehenSig = canCreateDarlehenFn(
    this.dashboardStore.dashboardViewSig,
    this.darlehenStore.darlehenListSig,
  );

  fallIdSig = computed(() => {
    return this.fallStore.currentFallViewSig()?.id;
  });

  constructor() {
    effect(() => {
      if (!this.fallIdSig()) {
        this.fallStore.loadCurrentFall$();
      }
    });

    effect(() => {
      const fallId = this.fallIdSig();

      if (fallId) {
        this.darlehenStore.getAllDarlehenGs$({ fallId });
      }
    });
  }
}
