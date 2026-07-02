/* eslint-disable @angular-eslint/no-input-rename */
import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  effect,
  inject,
  input,
} from '@angular/core';
import { MatSidenavModule } from '@angular/material/sidenav';
import { Router } from '@angular/router';
import { TranslocoDirective } from '@jsverse/transloco';

import { DarlehenStore } from '@dv/shared/data-access/darlehen';
import { GesuchHeaderStore } from '@dv/shared/data-access/gesuch-header';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import { byBusinessAppType } from '@dv/shared/model/permission-state';
import { SharedPatternDarlehenFormComponent } from '@dv/shared/pattern/darlehen-form';
import { SharedUiDarlehenMenuComponent } from '@dv/shared/ui/darlehen-menu';
import { SharedUiDarlehenVerfuegungDownloadComponent } from '@dv/shared/ui/darlehen-verfuegung-download';
import { SharedUtilFormService } from '@dv/shared/util/form';

@Component({
  selector: 'dv-shared-feature-darlehen',
  imports: [
    MatSidenavModule,
    SharedPatternDarlehenFormComponent,
    SharedUiDarlehenVerfuegungDownloadComponent,
    TranslocoDirective,
    SharedUiDarlehenMenuComponent,
  ],
  templateUrl: './shared-feature-darlehen.component.html',
  styleUrl: './shared-feature-darlehen.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureDarlehenComponent {
  @HostBinding('class') klass = 'tw:dv-pass-height';
  private router = inject(Router);
  private formUtils = inject(SharedUtilFormService);
  private config = inject(SharedModelCompileTimeConfig);
  private gesuchHeaderStore = inject(GesuchHeaderStore);
  darlehenStore = inject(DarlehenStore);
  darlehenIdSig = input<string | undefined>(undefined, { alias: 'darlehenId' });
  gesuchIdSig = input<string | undefined>(undefined, { alias: 'gesuchId' });
  fallIdSig = input<string | undefined>(undefined, { alias: 'fallId' });
  hasUnsavedChanges = false;

  constructor() {
    effect(() => {
      const darlehenId = this.darlehenIdSig();
      if (darlehenId) {
        byBusinessAppType(this.config.appType, {
          'gesuch-app': () =>
            this.darlehenStore.getDarlehenGs$({
              darlehenId,
              onFailure: () => {
                this.router.navigate(['/darlehen']);
              },
            }),
          'sachbearbeitung-app': () =>
            this.darlehenStore.getDarlehenSb$({
              darlehenId,
              onFailure: () => {
                this.router.navigate(['/darlehen']);
              },
            }),
        });
      }
    });

    effect(() => {
      const gesuchId = this.gesuchIdSig();
      if (gesuchId) {
        this.gesuchHeaderStore.loadHeader$({ gesuchId });
      }
    });
    this.formUtils.registerFormForUnsavedCheck(this);
  }

  reloadDarlehenList() {
    const fallId = this.fallIdSig();
    if (fallId) {
      this.darlehenStore.getAllDarlehen$({ fallId });
    }
  }
}
