import { Component, HostBinding, inject } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { Store } from '@ngrx/store';
import { KeycloakEventType, KeycloakService } from 'keycloak-angular';

import { SharedDataAccessBenutzerApiEvents } from '@dv/shared/data-access/benutzer';

@Component({
  standalone: true,
  imports: [RouterOutlet],
  selector: 'dv-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
  @HostBinding('class') klass = 'app-container shadow';

  constructor() {
    const store = inject(Store);
    const router = inject(Router);
    const keycloakService = inject(KeycloakService);
    store.dispatch(SharedDataAccessBenutzerApiEvents.loadCurrentBenutzer());
    router.initialNavigation();
    keycloakService.keycloakEvents$.subscribe({
      next(event) {
        if (event.type == KeycloakEventType.OnTokenExpired) {
          keycloakService.updateToken();
        }
      },
    });
  }
}
