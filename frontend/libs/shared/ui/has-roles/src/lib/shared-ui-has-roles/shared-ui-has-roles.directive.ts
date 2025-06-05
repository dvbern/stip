/* eslint-disable @angular-eslint/no-input-rename */
import {
  Directive,
  TemplateRef,
  ViewContainerRef,
  effect,
  inject,
  input,
} from '@angular/core';

import { PermissionStore } from '@dv/shared/global/permission';
import { AvailableBenutzerRole } from '@dv/shared/model/benutzer';

@Directive({
  selector: '[dvHasRoles]',
  standalone: true,
})
export class SharedUiHasRolesDirective {
  rolesSig = input.required<AvailableBenutzerRole[]>({ alias: 'dvHasRoles' });
  elseTemplateRefSig = input<TemplateRef<unknown> | null>(null, {
    alias: 'dvHasRolesElse',
  });

  private viewRef = inject(ViewContainerRef);
  private templateRef = inject(TemplateRef);
  private permissionStore = inject(PermissionStore);

  constructor() {
    effect(() => {
      const roleMap = this.permissionStore.rolesMapSig();
      const roles = this.rolesSig();
      const elseTemplateRef = this.elseTemplateRefSig();

      if (roles.some((role) => roleMap[role])) {
        if (!this.viewRef.length) {
          this.viewRef.createEmbeddedView(this.templateRef);
        }
      } else if (elseTemplateRef) {
        this.viewRef.clear();
        this.viewRef.createEmbeddedView(elseTemplateRef);
      }
    });
  }
}
