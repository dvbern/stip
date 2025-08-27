import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  Output,
  inject,
} from '@angular/core';
import { MatSidenavModule } from '@angular/material/sidenav';
import { Router, RouterModule } from '@angular/router';
import { TranslocoPipe } from '@jsverse/transloco';

import { AdminOption, ChildAdminOption } from '@dv/shared/model/router';
import {
  SharedPatternAppHeaderComponent,
  SharedPatternAppHeaderPartsDirective,
} from '@dv/shared/pattern/app-header';
import { SharedPatternMobileSidenavComponent } from '@dv/shared/pattern/mobile-sidenav';
import { SharedUiHasRolesDirective } from '@dv/shared/ui/has-roles';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUtilHeaderService } from '@dv/shared/util/header';
import { AdminOptions } from '@dv/sozialdienst-app/model/administration';

@Component({
  selector: 'dv-sozialdienst-app-pattern-administration-layout',
  imports: [
    CommonModule,
    TranslocoPipe,
    RouterModule,
    MatSidenavModule,
    SharedPatternMobileSidenavComponent,
    SharedPatternAppHeaderComponent,
    SharedPatternAppHeaderPartsDirective,
    SharedUiIconChipComponent,
    SharedUiHasRolesDirective,
  ],
  templateUrl:
    './sozialdienst-app-pattern-administration-layout.component.html',
  styleUrls: [
    './sozialdienst-app-pattern-administration-layout.component.scss',
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [SharedUtilHeaderService],
})
export class SozialdienstAppPatternAdministrationLayoutComponent {
  @Input() option?: AdminOption | ChildAdminOption;
  @Output() navClicked = new EventEmitter<{ value: boolean }>();

  route = inject(Router);
  headerService = inject(SharedUtilHeaderService);
  options = AdminOptions;
}
