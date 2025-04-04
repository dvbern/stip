import { Injectable, computed, signal } from '@angular/core';

import { TenantInfo } from '@dv/shared/model/gesuch';

@Injectable({
  providedIn: 'root',
})
export class SharedUtilTenantCacheService {
  private _tenantInfoSig = signal<TenantInfo | null>(null);

  tenantInfoSig = computed(() => this._tenantInfoSig());

  setTenantInfo(tenantInfo: TenantInfo) {
    this._tenantInfoSig.set(tenantInfo);
  }
}
