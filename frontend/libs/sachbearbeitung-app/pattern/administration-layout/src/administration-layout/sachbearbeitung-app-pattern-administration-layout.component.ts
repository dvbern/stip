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

import { FehlgeschlageneZahlungenStore } from '@dv/sachbearbeitung-app/data-access/fehlgeschlagene-zahlungen';
import { AdminOptions } from '@dv/sachbearbeitung-app/model/administration';
import { AdminOption, ChildAdminOption } from '@dv/shared/model/router';
import {
  SharedPatternAppHeaderComponent,
  SharedPatternAppHeaderPartsDirective,
} from '@dv/shared/pattern/app-header';
import { SharedPatternMobileSidenavComponent } from '@dv/shared/pattern/mobile-sidenav';
import { SharedUiHasRolesDirective } from '@dv/shared/ui/has-roles';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUtilHeaderService } from '@dv/shared/util/header';

@Component({
  selector: 'dv-sachbearbeitung-app-pattern-administration-layout',
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
    './sachbearbeitung-app-pattern-administration-layout.component.html',
  styleUrls: [
    './sachbearbeitung-app-pattern-administration-layout.component.scss',
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [SharedUtilHeaderService],
})
export class SachbearbeitungAppPatternAdministrationLayoutComponent {
  @Input() option?: AdminOption | ChildAdminOption;
  @Output() navClicked = new EventEmitter<{ value: boolean }>();

  fehlgeschlageneZahlungenStore = inject(FehlgeschlageneZahlungenStore);
  route = inject(Router);
  headerService = inject(SharedUtilHeaderService);
  options = AdminOptions;
}
