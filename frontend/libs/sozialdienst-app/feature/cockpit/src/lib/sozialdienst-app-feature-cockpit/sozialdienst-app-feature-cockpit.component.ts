import {
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
  input,
  signal,
  viewChild,
} from '@angular/core';
import { NonNullableFormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatSidenav, MatSidenavModule } from '@angular/material/sidenav';
import { RouterLink } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';

import { PermissionStore } from '@dv/shared/global/permission';
import {
  BenutzerRole,
  SozialdienstBenutzerRole,
} from '@dv/shared/model/benutzer';
import { GesuchFilter } from '@dv/shared/model/gesuch';
import { GesuchFormStep } from '@dv/shared/model/gesuch-form';
import { SharedPatternAppHeaderComponent } from '@dv/shared/pattern/app-header';
import { SharedPatternMobileSidenavComponent } from '@dv/shared/pattern/mobile-sidenav';
import { SharedUiHasRolesDirective } from '@dv/shared/ui/has-roles';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';

@Component({
  selector: 'dv-sozialdienst-app-feature-cockpit',
  standalone: true,
  imports: [
    RouterLink,
    TranslatePipe,
    MatSidenavModule,
    SharedUiIconChipComponent,
    SharedPatternMobileSidenavComponent,
    SharedPatternAppHeaderComponent,
    SharedUiHasRolesDirective,
    MatInputModule,
    ReactiveFormsModule,
  ],
  templateUrl: './sozialdienst-app-feature-cockpit.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SozialdienstAppFeatureCockpitComponent {
  stepSig = signal<GesuchFormStep | undefined>(undefined);
  private sidenavSig = viewChild.required(MatSidenav);
  private formBuilder = inject(NonNullableFormBuilder);
  private permissionStore = inject(PermissionStore);

  closeMenuSig = input<{ value: boolean } | null>(null, { alias: 'closeMenu' });

  quickFilterForm = this.formBuilder.group({
    query: [<GesuchFilter | undefined>undefined],
  });

  quickFilters: {
    typ: GesuchFilter;
    icon: string;
    roles: SozialdienstBenutzerRole[];
  }[] = [
    {
      typ: 'ALLE_BEARBEITBAR_MEINE',
      icon: 'person',
      roles: ['V0_Sozialdienst-Mitarbeiter'],
    },
    {
      typ: 'ALLE',
      icon: 'all_inclusive',
      roles: ['V0_Sozialdienst-Mitarbeiter'],
    },
  ];

  availableQuickFiltersSig = computed(() => {
    const roles = this.permissionStore.rolesMapSig();

    return this.quickFilters.filter((filter) =>
      filter.roles.some((role) => roles?.[role]),
    );
  });

  constructor() {
    effect(() => {
      if (this.closeMenuSig()?.value) {
        this.sidenavSig().close();
      }
    });
  }
}
