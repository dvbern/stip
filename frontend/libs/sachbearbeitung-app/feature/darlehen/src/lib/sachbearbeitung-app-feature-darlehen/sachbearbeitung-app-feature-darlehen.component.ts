import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  effect,
  inject,
  input,
} from '@angular/core';
import { MatSidenavModule } from '@angular/material/sidenav';
import { Router } from '@angular/router';
import { TranslocoPipe } from '@jsverse/transloco';
import { Store } from '@ngrx/store';

import { SachbearbeitungAppPatternGesuchHeaderComponent } from '@dv/sachbearbeitung-app/pattern/gesuch-header';
import { DarlehenStore } from '@dv/shared/data-access/darlehen';
import { selectRouteId } from '@dv/shared/data-access/gesuch';
import { SharedPatternDarlehenFormComponent } from '@dv/shared/pattern/darlehen-form';
import { SharedPatternMobileSidenavComponent } from '@dv/shared/pattern/mobile-sidenav';
import { SharedUtilFormService } from '@dv/shared/util/form';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-darlehen',
  imports: [
    CommonModule,
    MatSidenavModule,
    SharedPatternMobileSidenavComponent,
    SachbearbeitungAppPatternGesuchHeaderComponent,
    SharedPatternDarlehenFormComponent,
    TranslocoPipe,
  ],
  templateUrl: './sachbearbeitung-app-feature-darlehen.component.html',
  styleUrl: './sachbearbeitung-app-feature-darlehen.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureDarlehenComponent {
  private router = inject(Router);
  private store = inject(Store);
  private formUtils = inject(SharedUtilFormService);
  private darlehenStore = inject(DarlehenStore);
  // eslint-disable-next-line @angular-eslint/no-input-rename
  darlehenIdSig = input<string | undefined>(undefined, { alias: 'darlehenId' });
  gesuchIdSig = this.store.selectSignal(selectRouteId);
  hasUnsavedChanges = false;

  constructor() {
    effect(() => {
      const darlehenId = this.darlehenIdSig();
      if (darlehenId) {
        this.darlehenStore.getDarlehenSb$({
          darlehenId,
          onFailure: () => {
            this.router.navigate(['/darlehen']);
          },
        });
      }
    });
    this.formUtils.registerFormForUnsavedCheck(this);
  }

  reloadDarlehenList() {
    const gesuchId = this.gesuchIdSig();
    if (gesuchId) {
      this.darlehenStore.getAllDarlehenSb$({ gesuchId });
    }
  }
}
