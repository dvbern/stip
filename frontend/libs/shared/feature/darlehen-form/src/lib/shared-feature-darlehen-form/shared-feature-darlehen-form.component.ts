/* eslint-disable @angular-eslint/no-input-rename */
import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  effect,
  inject,
  input,
} from '@angular/core';
import { MatMenuModule } from '@angular/material/menu';
import { Router } from '@angular/router';

import { DarlehenStore } from '@dv/shared/data-access/darlehen';
import { FallHeaderStore } from '@dv/shared/data-access/fall-header';
import { GesuchHeaderStore } from '@dv/shared/data-access/gesuch-header';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import { byAppConfig } from '@dv/shared/model/permission-state';
import { DVBreakpoints } from '@dv/shared/model/ui-constants';
import { SharedPatternDarlehenFormComponent } from '@dv/shared/pattern/darlehen-form';
import { SharedUiAdvTranslocoDirective } from '@dv/shared/ui/adv-transloco-directive';
import { SharedUiDarlehenMenuComponent } from '@dv/shared/ui/darlehen-menu';
import { SharedUiDarlehenVerfuegungDownloadComponent } from '@dv/shared/ui/darlehen-verfuegung-download';
import { SharedUtilFormService } from '@dv/shared/util/form';

@Component({
  selector: 'dv-shared-feature-darlehen-form-feature',
  imports: [
    CommonModule,
    MatMenuModule,
    SharedPatternDarlehenFormComponent,
    SharedUiDarlehenVerfuegungDownloadComponent,
    SharedUiDarlehenMenuComponent,
    SharedUiAdvTranslocoDirective,
  ],
  templateUrl: './shared-feature-darlehen-form.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureDarlehenFormComponent {
  @HostBinding('class') klass = 'tw:h-full';
  private formUtils = inject(SharedUtilFormService);
  private config = inject(SharedModelCompileTimeConfig);
  private router = inject(Router);
  private gesuchHeaderStore = inject(GesuchHeaderStore);
  private fallHeaderStore = inject(FallHeaderStore);
  darlehenStore = inject(DarlehenStore);
  hasUnsavedChanges = false;
  darlehenIdSig = input<string | undefined>(undefined, { alias: 'darlehenId' });
  gesuchIdSig = input<string | undefined>(undefined, { alias: 'gesuchId' });
  fallIdSig = input<string | undefined>(undefined, { alias: 'fallId' });

  constructor() {
    this.formUtils.registerFormForUnsavedCheck(this);

    effect(() => {
      const fallId = this.fallIdSig();

      if (fallId) {
        byAppConfig(this.config.app, {
          gesuchsteller: () => {
            this.darlehenStore.getAllDarlehenGs$({ fallId });
          },
          sachbearbeiter: () => {
            this.darlehenStore.getAllDarlehen$({ fallId });
          },
        });
      }
    });

    effect(() => {
      const fallId = this.fallIdSig();
      const darlehenId = this.darlehenIdSig();
      const gesuchId = this.gesuchIdSig();
      const firstDarlehenId =
        this.darlehenStore.darlehenListViewSig().list[0]?.id;

      const isDesktop = window.innerWidth >= DVBreakpoints.LG;
      if (!darlehenId && firstDarlehenId && gesuchId && fallId && isDesktop) {
        this.router.navigate(
          ['/gesuch', gesuchId, 'darlehen', firstDarlehenId, 'fall', fallId],
          { replaceUrl: true },
        );
      }
    });

    effect(() => {
      const darlehenId = this.darlehenIdSig();
      if (darlehenId) {
        byAppConfig(this.config.app, {
          gesuchsteller: () =>
            this.darlehenStore.getDarlehenGs$({
              darlehenId,
              onFailure: () => {
                this.redirectToHome();
              },
            }),
          sachbearbeiter: () =>
            this.darlehenStore.getDarlehenSb$({
              darlehenId,
              onFailure: () => {
                this.redirectToHome();
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
  }

  redirectToHome() {
    this.hasUnsavedChanges = false;
    this.router.navigate([
      byAppConfig(this.config.app, {
        gesuchsteller: () => '/',
        sachbearbeiter: () => '/darlehen',
      }),
    ]);
  }

  toDarlehenOverview(): void {
    const gesuchId = this.gesuchIdSig();
    const fallId = this.fallIdSig();

    if (!gesuchId || !fallId) {
      return;
    }

    this.router.navigate(['/gesuch', gesuchId, 'darlehen', 'fall', fallId]);
  }

  reloadFallHeader() {
    const fallId = this.fallIdSig();
    if (fallId) {
      this.fallHeaderStore.loadFallHeader$({ fallId });
    }
  }

  reloadDarlehenList() {
    const fallId = this.fallIdSig();
    if (fallId) {
      this.darlehenStore.getAllDarlehen$({ fallId });
    }
  }
}
