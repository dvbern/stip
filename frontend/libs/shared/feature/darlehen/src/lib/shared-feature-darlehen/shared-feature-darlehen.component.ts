/* eslint-disable @angular-eslint/no-input-rename */
import {
  ChangeDetectionStrategy,
  Component,
  effect,
  inject,
  input,
} from '@angular/core';
import { MatMenuModule } from '@angular/material/menu';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslocoDirective } from '@jsverse/transloco';

import { DarlehenStore } from '@dv/shared/data-access/darlehen';
import { DashboardStore } from '@dv/shared/data-access/dashboard';
import { SharedPatternDarlehenFormComponent } from '@dv/shared/pattern/darlehen-form';
import { SharedUiDarlehenVerfuegungDownloadComponent } from '@dv/shared/ui/darlehen-verfuegung-download';
import { SharedUtilFormService } from '@dv/shared/util/form';

@Component({
  selector: 'dv-shared-feature-darlehen-feature',
  imports: [
    TranslocoDirective,
    MatMenuModule,
    SharedPatternDarlehenFormComponent,
    SharedUiDarlehenVerfuegungDownloadComponent,
  ],
  templateUrl: './shared-feature-darlehen.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureDarlehenFeatureComponent {
  darlehenStore = inject(DarlehenStore);
  dashboardStore = inject(DashboardStore);
  route = inject(ActivatedRoute);
  router = inject(Router);
  private formUtils = inject(SharedUtilFormService);
  hasUnsavedChanges = false;
  darlehenIdSig = input<string | undefined>(undefined, { alias: 'darlehenId' });

  constructor() {
    this.formUtils.registerFormForUnsavedCheck(this);

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
