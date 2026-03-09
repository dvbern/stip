import { Component, HostBinding, inject } from '@angular/core';
import { Router } from '@angular/router';
import { SozialdienstAppPatternMainLayoutComponent } from '@dv/sozialdienst-app/pattern/main-layout';
import { Store } from '@ngrx/store';

import { SharedDataAccessBenutzerApiEvents } from '@dv/shared/data-access/benutzer';
import { GlobalNotificationsComponent } from '@dv/shared/pattern/global-notification';

@Component({
  imports: [
    GlobalNotificationsComponent,
    SozialdienstAppPatternMainLayoutComponent,
  ],
  selector: 'dv-root',
  templateUrl: './app.component.html',
})
export class AppComponent {
  @HostBinding('class') klass = 'app-container shadow';

  constructor() {
    const store = inject(Store);
    const router = inject(Router);
    store.dispatch(SharedDataAccessBenutzerApiEvents.loadCurrentBenutzer());
    router.initialNavigation();
  }
}
