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
import { TranslatePipe } from '@ngx-translate/core';

import { AdminOption, ChildAdminOption } from '@dv/shared/model/router';
import {
  SharedPatternAppHeaderComponent,
  SharedPatternAppHeaderPartsDirective,
} from '@dv/shared/pattern/app-header';
import { SharedPatternMobileSidenavComponent } from '@dv/shared/pattern/mobile-sidenav';
import { SharedUiHasRolesDirective } from '@dv/shared/ui/has-roles';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiSearchComponent } from '@dv/shared/ui/search';
import { SharedUtilHeaderService } from '@dv/shared/util/header';
import { AdminOptions } from '@dv/sozialdienst-app/model/administration';

@Component({
  selector: 'dv-sozialdienst-app-pattern-administration-layout',
  standalone: true,
  imports: [
    CommonModule,
    TranslatePipe,
    RouterModule,
    MatSidenavModule,
    SharedPatternMobileSidenavComponent,
    SharedUiSearchComponent,
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
