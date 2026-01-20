/* eslint-disable @angular-eslint/no-input-rename */
import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  effect,
  inject,
  input,
} from '@angular/core';
import { MatMenuModule } from '@angular/material/menu';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { TranslocoDirective } from '@jsverse/transloco';

import { DarlehenStore } from '@dv/shared/data-access/darlehen';
import { DashboardStore } from '@dv/shared/data-access/dashboard';
import { FallStore } from '@dv/shared/data-access/fall';
import { SharedPatternDarlehenFormComponent } from '@dv/shared/pattern/darlehen-form';
import { SharedPatternMainLayoutComponent } from '@dv/shared/pattern/main-layout';
import { SharedUiDarlehenMenuComponent } from '@dv/shared/ui/darlehen-menu';
import { SharedUtilFormService } from '@dv/shared/util/form';

@Component({
  selector: 'dv-shared-feature-darlehen-feature',
  imports: [
    CommonModule,
    RouterLink,
    TranslocoDirective,
    MatMenuModule,
    SharedPatternMainLayoutComponent,
    SharedPatternDarlehenFormComponent,
    SharedUiDarlehenMenuComponent,
  ],
  templateUrl: './shared-feature-darlehen.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureDarlehenFeatureComponent {
  darlehenStore = inject(DarlehenStore);
  dashboardStore = inject(DashboardStore);
  route = inject(ActivatedRoute);
  fallStore = inject(FallStore);
  router = inject(Router);
  private formUtils = inject(SharedUtilFormService);
  hasUnsavedChanges = false;
  darlehenIdSig = input<string | undefined>(undefined, { alias: 'darlehenId' });
  fallIdSig = input<string | undefined>(undefined, { alias: 'fallId' });

  constructor() {
    this.formUtils.registerFormForUnsavedCheck(this);

    effect(() => {
      const fallId = this.fallIdSig();

      if (fallId) {
        this.darlehenStore.getAllDarlehenGs$({ fallId });
      }
    });

    effect(() => {
      const darlehenId = this.darlehenIdSig();
      if (darlehenId) {
        this.darlehenStore.getDarlehenGs$({
          darlehenId,
          onFailure: () => {
            this.redirectToHome();
          },
        });
      }
    });
  }

  redirectToHome() {
    this.hasUnsavedChanges = false;
    this.router.navigate(['/']);
  }
}
