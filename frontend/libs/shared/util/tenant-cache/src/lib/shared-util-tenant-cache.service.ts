import { DOCUMENT } from '@angular/common';
import { Injectable, computed, effect, inject, signal } from '@angular/core';

import { TenantInfo } from '@dv/shared/model/gesuch';

const faviconMap = {
  bern: 'assets/images/logo_kanton_bern_full.svg',
  dv: 'assets/images/dv-favicon-logo.svg',
} as const;

@Injectable({
  providedIn: 'root',
})
export class SharedUtilTenantCacheService {
  private _tenantInfoSig = signal<TenantInfo | null>(null);
  private document = inject(DOCUMENT);

  tenantInfoSig = computed(() => this._tenantInfoSig());

  // effect to set favicon
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

  private setFavicon(identifier: string) {
    const favicon = this.document.querySelector('link[rel="icon"]');
    if (favicon) {
      favicon.setAttribute(
        'href',
        faviconMap[identifier as keyof typeof faviconMap] ||
          'assets/images/logo_kanton_bern_full.svg',
      );
    }
  }
}
