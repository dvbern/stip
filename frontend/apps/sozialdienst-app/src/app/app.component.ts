import { Component, HostBinding, inject } from '@angular/core';
import { Router } from '@angular/router';

import { GlobalNotificationsComponent } from '@dv/shared/pattern/global-notification';
import { StoreUtilService } from '@dv/shared/util-data-access/store-util';
import { SozialdienstAppPatternMainLayoutComponent } from '@dv/sozialdienst-app/pattern/main-layout';

@Component({
  imports: [
    GlobalNotificationsComponent,
    SozialdienstAppPatternMainLayoutComponent,
  ],
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
