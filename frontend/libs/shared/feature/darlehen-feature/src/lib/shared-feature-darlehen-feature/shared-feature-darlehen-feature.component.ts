/* eslint-disable @angular-eslint/no-input-rename */
import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
  input,
} from '@angular/core';
import { MatMenuModule } from '@angular/material/menu';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { TranslocoDirective } from '@jsverse/transloco';

import { DarlehenStore } from '@dv/shared/data-access/darlehen';
import { DashboardStore } from '@dv/shared/data-access/dashboard';
import { FallStore } from '@dv/shared/data-access/fall';
import { SharedFeatureDarlehenComponent } from '@dv/shared/feature/darlehen';
import { SharedPatternMainLayoutComponent } from '@dv/shared/pattern/main-layout';
import { SharedUiDarlehenMenuComponent } from '@dv/shared/ui/darlehen-menu';

@Component({
  selector: 'dv-shared-feature-darlehen-feature',
  imports: [
    CommonModule,
    RouterLink,
    TranslocoDirective,
    SharedPatternMainLayoutComponent,
    SharedFeatureDarlehenComponent,
    MatMenuModule,
    SharedUiDarlehenMenuComponent,
  ],
  templateUrl: './shared-feature-darlehen-feature.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureDarlehenFeatureComponent {
  darlehenStore = inject(DarlehenStore);
  dashboardStore = inject(DashboardStore);
  route = inject(ActivatedRoute);
  fallStore = inject(FallStore);
  hasUnsavedChanges = false;
  darlehenIdSig = input<string | undefined>(undefined, { alias: 'darlehenId' });

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
        this.dashboardStore.loadDashboard$();
        this.darlehenStore.getAllDarlehenGs$({ fallId });
      }
    });
  }
}
