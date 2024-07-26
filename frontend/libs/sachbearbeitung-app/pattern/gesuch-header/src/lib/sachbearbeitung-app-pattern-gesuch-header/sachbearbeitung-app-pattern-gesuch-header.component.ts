import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
  input,
} from '@angular/core';
import { MatMenuModule } from '@angular/material/menu';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';

import { SharedDataAccessGesuchEvents } from '@dv/shared/data-access/gesuch';
import { SharedModelGesuch } from '@dv/shared/model/gesuch';
import {
  SharedPatternAppHeaderComponent,
  SharedPatternAppHeaderPartsDirective,
} from '@dv/shared/pattern/app-header';

@Component({
  selector: 'dv-sachbearbeitung-app-pattern-gesuch-header',
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    RouterLink,
    RouterLinkActive,
    MatMenuModule,
    SharedPatternAppHeaderComponent,
    SharedPatternAppHeaderPartsDirective,
  ],
  templateUrl: './sachbearbeitung-app-pattern-gesuch-header.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppPatternGesuchHeaderComponent {
  currentGesuchSig = input.required<SharedModelGesuch | null>({
    alias: 'currentGesuch',
  });
  navClickedSig = input.required<{ value: unknown }>({ alias: 'navClicked' });
  store = inject(Store);
  router = inject(Router);

  isGesuchRouteSig = computed(() => {
    const gesuch = this.currentGesuchSig();
    if (!gesuch) {
      return false;
    }
    return this.router.isActive('/gesuch', {
      paths: 'subset',
      fragment: 'ignored',
      matrixParams: 'ignored',
      queryParams: 'ignored',
    });
  });
  canSetToBearbeitungSig = computed(() => {
    const gesuch = this.currentGesuchSig();
    const status = gesuch?.gesuchStatus;
    if (!status) {
      return false;
    }
    return status === 'BEREIT_FUER_BEARBEITUNG';
  });

  setToBearbeitung() {
    this.store.dispatch(SharedDataAccessGesuchEvents.setGesuchToBearbeitung());
  }
}
