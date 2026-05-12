import { Component, HostBinding, inject } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';

import { GlobalNotificationsComponent } from '@dv/shared/pattern/global-notification';
import { StoreUtilService } from '@dv/shared/util-data-access/store-util';

@Component({
  imports: [RouterOutlet, GlobalNotificationsComponent],
  selector: 'dv-root',
  templateUrl: './app.component.html',
})
export class AppComponent {
  @HostBinding('class') klass = 'app-container shadow';

  constructor() {
    const router = inject(Router);
    const storeUtilService = inject(StoreUtilService);
    storeUtilService.loadAndGetBenutzerData().then(() => {
      router.initialNavigation();
    });
  }
}
