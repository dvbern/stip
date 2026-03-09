import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  inject,
} from '@angular/core';
import { MatSidenavModule } from '@angular/material/sidenav';
import { Router, RouterModule } from '@angular/router';
import { TranslocoDirective, TranslocoPipe } from '@jsverse/transloco';

import { AdminOption, ChildAdminOption } from '@dv/shared/model/router';
import { SharedUiHasRolesDirective } from '@dv/shared/ui/has-roles';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiRouterOutletWrapperComponent } from '@dv/shared/ui/router-outlet-wrapper';
import { SharedUtilHeaderService } from '@dv/shared/util/header';
import { AdminOptions } from '@dv/sozialdienst-app/model/administration';

@Component({
  imports: [
    CommonModule,
    TranslocoPipe,
    RouterModule,
    MatSidenavModule,
    SharedUiIconChipComponent,
    SharedUiHasRolesDirective,
    TranslocoDirective,
    SharedUiRouterOutletWrapperComponent,
  ],
  templateUrl: './sozialdienst-app-feature-administration.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [SharedUtilHeaderService],
})
export class SozialdienstAppFeatureAdministrationComponent {
  @HostBinding('class') klass = 'tw:dv-pass-height';

  options = AdminOptions;
  route = inject(Router);
  headerService = inject(SharedUtilHeaderService);
  option?: AdminOption | ChildAdminOption;
}
