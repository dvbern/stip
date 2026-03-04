import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Output,
  inject,
} from '@angular/core';
import { MatSidenavModule } from '@angular/material/sidenav';
import { Router, RouterModule } from '@angular/router';
import { TranslocoDirective, TranslocoPipe } from '@jsverse/transloco';

import { FehlgeschlageneZahlungenStore } from '@dv/sachbearbeitung-app/data-access/fehlgeschlagene-zahlungen';
import { AdminOptions } from '@dv/sachbearbeitung-app/model/administration';
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
    TranslocoPipe,
    RouterModule,
    MatSidenavModule,
    SharedUiIconChipComponent,
    SharedUiHasRolesDirective,
    TranslocoDirective,
  ],
  templateUrl: './sachbearbeitung-app-feature-administration.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [SharedUtilHeaderService],
})
export class SachbearbeitungAppFeatureAdministrationComponent {
  // todo: can it be childadmin option?
  option?: AdminOption | ChildAdminOption;

  // todo: really needed?
  @Output() navClicked = new EventEmitter<{ value: boolean }>();

  fehlgeschlageneZahlungenStore = inject(FehlgeschlageneZahlungenStore);
  route = inject(Router);
  headerService = inject(SharedUtilHeaderService);
  options = AdminOptions;
}
