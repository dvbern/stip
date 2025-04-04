import { DOCUMENT } from '@angular/common';
import { Injectable, computed, effect, inject, signal } from '@angular/core';

import { isTenantKey } from '@dv/shared/model/config';
import { TenantInfo } from '@dv/shared/model/gesuch';
import { isDefined } from '@dv/shared/model/type-util';

@Injectable({
  providedIn: 'root',
})
export class SharedUtilTenantConfigService {
  private _tenantInfoSig = signal<TenantInfo | null>(null);
  private document = inject(DOCUMENT);

  tenantInfoSig = computed(() => this._tenantInfoSig());

  constructor() {
    effect(() => {
      const tenantInfo = this._tenantInfoSig();
      if (tenantInfo) {
        this.setFavicon(tenantInfo.identifier);
      }
    });
  }

  setTenantInfo(tenantInfo: TenantInfo) {
    this._tenantInfoSig.set(tenantInfo);
  }

  private setFavicon(identifier: string | undefined | null) {
    const favicon = this.document.querySelector('link[rel="icon"]');
    if (favicon && isDefined(identifier) && isTenantKey(identifier)) {
      favicon.setAttribute(
        'href',
        `assets/images/logo_kanton_${identifier}.svg`,
      );
    }
  }
}
