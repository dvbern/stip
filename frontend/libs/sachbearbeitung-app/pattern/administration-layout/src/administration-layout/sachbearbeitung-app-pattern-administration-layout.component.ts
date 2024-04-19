import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  inject,
} from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

import {
  AdminOption,
  AdminOptions,
} from '@dv/sachbearbeitung-app/model/administration';
import { SharedPatternAppHeaderComponent } from '@dv/shared/pattern/app-header';
import { GlobalNotificationsComponent } from '@dv/shared/pattern/global-notification';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiSearchComponent } from '@dv/shared/ui/search';

@Component({
  selector: 'dv-sachbearbeitung-app-pattern-administration-layout',
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    RouterModule,
    SharedUiSearchComponent,
    SharedPatternAppHeaderComponent,
    SharedUiIconChipComponent,
    GlobalNotificationsComponent,
  ],
  templateUrl:
    './sachbearbeitung-app-pattern-administration-layout.component.html',
  styleUrls: [
    './sachbearbeitung-app-pattern-administration-layout.component.scss',
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppPatternAdministrationLayoutComponent {
  @Input() option?: AdminOption;
  navClicked = new EventEmitter();

  route = inject(Router);
  options = AdminOptions;

  trackByIndex(index: number) {
    return index;
  }
}
