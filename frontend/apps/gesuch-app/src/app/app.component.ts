import { Component, HostBinding, inject } from '@angular/core';
import { Router } from '@angular/router';

import { GesuchAppPatternMainLayoutComponent } from '@dv/gesuch-app/pattern/main-layout';
import { GlobalNotificationsComponent } from '@dv/shared/pattern/global-notification';
import { StoreUtilService } from '@dv/shared/util-data-access/store-util';

@Component({
  imports: [GlobalNotificationsComponent, GesuchAppPatternMainLayoutComponent],
  selector: 'dv-root',
  templateUrl: './app.component.html',
})
export class AppComponent {
  @HostBinding('class') klass = 'app-container';

  constructor() {
    const router = inject(Router);
    const storeUtilService = inject(StoreUtilService);
    storeUtilService.loadAndGetBenutzerData().then(() => {
      router.initialNavigation();
    });
  }
}
