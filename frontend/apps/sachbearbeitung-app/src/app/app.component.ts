import { Component, HostBinding, inject } from '@angular/core';
import { Router } from '@angular/router';

import { SachbearbeitungAppPatternMainLayoutComponent } from '@dv/sachbearbeitung-app/pattern/main-layout';
import { AblehnungGrundStore } from '@dv/shared/global/ablehnung-grund';
import { GlobalNotificationsComponent } from '@dv/shared/pattern/global-notification';
import { StoreUtilService } from '@dv/shared/util-data-access/store-util';

@Component({
  imports: [
    GlobalNotificationsComponent,
    SachbearbeitungAppPatternMainLayoutComponent,
  ],
  selector: 'dv-root',
  templateUrl: './app.component.html',
})
export class AppComponent {
  @HostBinding('class') klass = 'app-container shadow';

  constructor() {
    const globalGrundStore = inject(AblehnungGrundStore);
    const router = inject(Router);
    const storeUtilService = inject(StoreUtilService);
    storeUtilService.loadAndGetBenutzerData().then(() => {
      router.initialNavigation();
      globalGrundStore.loadAblehnungsGruende$();
    });
  }
}
