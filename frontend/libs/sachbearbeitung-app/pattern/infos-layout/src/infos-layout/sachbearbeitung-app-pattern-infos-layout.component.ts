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
import { TranslateModule } from '@ngx-translate/core';

import {
  INFOS_OPTIONS,
  INFOS_ROUTE,
  InfosOptions,
} from '@dv/sachbearbeitung-app/model/infos';
import { SachbearbeitungAppPatternGesuchHeaderComponent } from '@dv/sachbearbeitung-app/pattern/gesuch-header';
import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import { SharedPatternAppHeaderComponent } from '@dv/shared/pattern/app-header';
import { GlobalNotificationsComponent } from '@dv/shared/pattern/global-notification';
import { SharedPatternMobileSidenavComponent } from '@dv/shared/pattern/mobile-sidenav';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';

@Component({
  selector: 'dv-sachbearbeitung-app-pattern-infos-layout',
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    RouterLink,
    RouterLinkActive,
    MatSidenavModule,
    SharedPatternMobileSidenavComponent,
    SharedPatternAppHeaderComponent,
    GlobalNotificationsComponent,
    SharedUiIconChipComponent,
    SachbearbeitungAppPatternGesuchHeaderComponent,
  ],
  templateUrl: './sachbearbeitung-app-pattern-infos-layout.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppPatterninfosLayoutComponent {
  private store = inject(Store);

  @Input() option?: InfosOptions;
  infosOptions = INFOS_OPTIONS;
  infosRoute = INFOS_ROUTE;
  navClicked$ = new EventEmitter();
  gesuchViewSig = this.store.selectSignal(selectSharedDataAccessGesuchsView);
}
