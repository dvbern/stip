import { Component, HostBinding, inject } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { Store } from '@ngrx/store';

import { SharedDataAccessBenutzerApiEvents } from '@dv/shared/data-access/benutzer';
import { GlobalNotificationsComponent } from '@dv/shared/pattern/global-notification';

@Component({
  imports: [RouterOutlet, GlobalNotificationsComponent],
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
