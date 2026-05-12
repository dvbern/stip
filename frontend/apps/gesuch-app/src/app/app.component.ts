import { Component, HostBinding, inject } from '@angular/core';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';

import { GesuchAppPatternMainLayoutComponent } from '@dv/gesuch-app/pattern/main-layout';
import { SharedDataAccessBenutzerApiEvents } from '@dv/shared/data-access/benutzer';
import { GlobalNotificationsComponent } from '@dv/shared/pattern/global-notification';

@Component({
  imports: [GlobalNotificationsComponent, GesuchAppPatternMainLayoutComponent],
  selector: 'dv-root',
  templateUrl: './app.component.html',
})
export class AppComponent {
  @HostBinding('class') klass = 'app-container';

  constructor() {
    const store = inject(Store);
    const router = inject(Router);
    store.dispatch(SharedDataAccessBenutzerApiEvents.loadCurrentBenutzer());
    router.initialNavigation();
  }
}
