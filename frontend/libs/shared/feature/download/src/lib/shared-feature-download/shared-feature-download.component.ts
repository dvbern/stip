import { CommonModule, DOCUMENT } from '@angular/common';
import {
  ApplicationRef,
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  computed,
  inject,
  input,
} from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';
import { addSeconds } from 'date-fns';
import { KeycloakService } from 'keycloak-angular';
import { debounceTime, filter, take } from 'rxjs';

import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';

@Component({
  selector: 'dv-shared-feature-download',
  standalone: true,
  imports: [CommonModule, TranslateModule, SharedUiLoadingComponent],
  templateUrl: './shared-feature-download.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureDownloadComponent {
  gesuchIdSig = input.required<string>({ alias: 'gesuchId' });
  typeSig = input.required<string>({ alias: 'type' });
  dokumentIdSig = input.required<string>({ alias: 'dokumentId' });
  keycloakService = inject(KeycloakService);
  dcmnt = inject(DOCUMENT, { optional: true });
  appRef = inject(ApplicationRef);

  @HostBinding('class') class =
    'd-flex flex-column position-absolute top-0 bottom-0 start-0 end-0 p-5';

  private apiPathSig = computed(() => {
    return `/api/v1/gesuch/${this.gesuchIdSig()}/dokument/${this.typeSig()}/${this.dokumentIdSig()}`;
  });

  constructor() {
    if (this.dcmnt) {
      const dcmnt = this.dcmnt;
      this.keycloakService.getToken().then((token) => {
        dcmnt.cookie = `token=${token}; expires=${addSeconds(
          new Date(),
          30,
        )}; Secure; Path=${this.apiPathSig()}`;
        dcmnt.location.href = this.apiPathSig();

        this.appRef.isStable
          .pipe(
            filter((isStable) => isStable),
            debounceTime(1000),
            take(1),
          )
          .subscribe(() => {
            dcmnt.defaultView?.close();
          });
      });
    }
  }
}
