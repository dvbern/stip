import { CdkPortal, PortalModule } from '@angular/cdk/portal';
import { CommonModule } from '@angular/common';
import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  OnDestroy,
  ViewChild,
  inject,
} from '@angular/core';
import { MatSidenavModule } from '@angular/material/sidenav';
import { Router, RouterModule } from '@angular/router';
import { TranslocoDirective } from '@jsverse/transloco';

import { NavigationStore } from '@dv/shared/data-access/navigation';
import { AdminOption, ChildAdminOption } from '@dv/shared/model/router';
import { SharedUiHasRolesDirective } from '@dv/shared/ui/has-roles';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiRouterOutletWrapperComponent } from '@dv/shared/ui/router-outlet-wrapper';
import { SharedUtilHeaderService } from '@dv/shared/util/header';
import { AdminOptions } from '@dv/sozialdienst-app/model/administration';

@Component({
  imports: [
    CommonModule,
    RouterModule,
    MatSidenavModule,
    SharedUiIconChipComponent,
    SharedUiHasRolesDirective,
    TranslocoDirective,
    SharedUiRouterOutletWrapperComponent,
    PortalModule,
  ],
  templateUrl: './sozialdienst-app-feature-administration.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [SharedUtilHeaderService],
})
export class SozialdienstAppFeatureAdministrationComponent
  implements AfterViewInit, OnDestroy
{
  @HostBinding('class') klass = 'tw:dv-pass-height';
  @ViewChild(CdkPortal)
  portalContent: CdkPortal | null = null;

  private navigationStore = inject(NavigationStore);

  options = AdminOptions;
  route = inject(Router);
  headerService = inject(SharedUtilHeaderService);
  option?: AdminOption | ChildAdminOption;

  ngAfterViewInit(): void {
    this.navigationStore.setPortal(this.portalContent);
  }
  ngOnDestroy() {
    if (this.portalContent?.isAttached) {
      this.portalContent.detach();
    }
  }
}
