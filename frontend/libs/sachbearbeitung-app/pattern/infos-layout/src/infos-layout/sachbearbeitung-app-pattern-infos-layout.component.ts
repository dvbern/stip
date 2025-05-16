import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  inject,
} from '@angular/core';
import { MatSidenavModule } from '@angular/material/sidenav';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslatePipe } from '@ngx-translate/core';

import {
  INFOS_OPTIONS,
  INFOS_ROUTE,
  InfosOptions,
} from '@dv/sachbearbeitung-app/model/infos';
import { SachbearbeitungAppPatternGesuchHeaderComponent } from '@dv/sachbearbeitung-app/pattern/gesuch-header';
import { selectRouteId } from '@dv/shared/data-access/gesuch';
import { SharedPatternAppHeaderComponent } from '@dv/shared/pattern/app-header';
import { SharedPatternMobileSidenavComponent } from '@dv/shared/pattern/mobile-sidenav';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';

@Component({
    selector: 'dv-sachbearbeitung-app-pattern-infos-layout',
    imports: [
        CommonModule,
        TranslatePipe,
        RouterLink,
        RouterLinkActive,
        MatSidenavModule,
        SharedPatternMobileSidenavComponent,
        SharedPatternAppHeaderComponent,
        SharedUiIconChipComponent,
        SachbearbeitungAppPatternGesuchHeaderComponent,
    ],
    templateUrl: './sachbearbeitung-app-pattern-infos-layout.component.html',
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class SachbearbeitungAppPatterninfosLayoutComponent {
  private store = inject(Store);

  @Input() option?: InfosOptions;
  infosOptions = INFOS_OPTIONS;
  infosRoute = INFOS_ROUTE;
  navClicked$ = new EventEmitter();
  gesuchIdSig = this.store.selectSignal(selectRouteId);
}
