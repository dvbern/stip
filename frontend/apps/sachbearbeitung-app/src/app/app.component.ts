import { Component, HostBinding, inject } from '@angular/core';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';

import { SachbearbeitungAppPatternMainLayoutComponent } from '@dv/sachbearbeitung-app/pattern/main-layout';
import { SharedDataAccessBenutzerApiEvents } from '@dv/shared/data-access/benutzer';
import { AblehnungGrundStore } from '@dv/shared/global/ablehnung-grund';
import { GlobalNotificationsComponent } from '@dv/shared/pattern/global-notification';

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
    const store = inject(Store);
    const router = inject(Router);
    const globalGrundStore = inject(AblehnungGrundStore);
    store.dispatch(SharedDataAccessBenutzerApiEvents.loadCurrentBenutzer());
    router.initialNavigation();
    globalGrundStore.loadAblehnungsGruende$();
  }
}
