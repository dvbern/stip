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

import { FehlgeschlageneZahlungenStore } from '@dv/sachbearbeitung-app/data-access/fehlgeschlagene-zahlungen';
import { AdminOptions } from '@dv/sachbearbeitung-app/model/administration';
import { NavigationStore } from '@dv/shared/data-access/navigation';
import { AdminOption, ChildAdminOption } from '@dv/shared/model/router';
import { SharedUiHasRolesDirective } from '@dv/shared/ui/has-roles';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiRouterOutletWrapperComponent } from '@dv/shared/ui/router-outlet-wrapper';
import { SharedUtilHeaderService } from '@dv/shared/util/header';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-administration',
  imports: [
    SharedUiRouterOutletWrapperComponent,
    CommonModule,
    RouterModule,
    MatSidenavModule,
    SharedUiIconChipComponent,
    SharedUiHasRolesDirective,
    TranslocoDirective,
    PortalModule,
  ],
  templateUrl: './sachbearbeitung-app-feature-administration.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [SharedUtilHeaderService],
})
export class SachbearbeitungAppFeatureAdministrationComponent
  implements AfterViewInit, OnDestroy
{
  @HostBinding('class') klass = 'tw:dv-pass-height';
  @ViewChild(CdkPortal)
  portalContent: CdkPortal | null = null;

  private navigationStore = inject(NavigationStore);

  option?: AdminOption | ChildAdminOption;

  fehlgeschlageneZahlungenStore = inject(FehlgeschlageneZahlungenStore);
  route = inject(Router);
  headerService = inject(SharedUtilHeaderService);
  options = AdminOptions;

  ngAfterViewInit(): void {
    this.navigationStore.setPortal(this.portalContent);
  }
  ngOnDestroy() {
    if (this.portalContent?.isAttached) {
      this.portalContent.detach();
      this.navigationStore.setPortal(null);
    }
  }
}
