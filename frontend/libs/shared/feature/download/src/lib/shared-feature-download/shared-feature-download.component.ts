import { CommonModule, DOCUMENT } from '@angular/common';
import {
  ApplicationRef,
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  OnInit,
  computed,
  inject,
  input,
} from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { addSeconds } from 'date-fns';
import { debounceTime, filter, take } from 'rxjs';

import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';

@Component({
  selector: 'dv-shared-feature-download',
  standalone: true,
  imports: [CommonModule, TranslateModule, SharedUiLoadingComponent],
  templateUrl: './shared-feature-download.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureDownloadComponent implements OnInit {
  gesuchIdSig = input.required<string>({ alias: 'gesuchId' });
  typeSig = input.required<string>({ alias: 'type' });
  dokumentIdSig = input.required<string>({ alias: 'dokumentId' });
  oauthService = inject(OAuthService);
  dcmnt = inject(DOCUMENT, { optional: true });
  appRef = inject(ApplicationRef);

  @HostBinding('class') class =
    'd-flex flex-column position-absolute top-0 bottom-0 start-0 end-0 p-5';

  private apiPathSig = computed(() => {
    return `/api/v1/gesuch/${this.gesuchIdSig()}/dokument/${this.typeSig()}/${this.dokumentIdSig()}`;
  });

  ngOnInit() {
    if (this.dcmnt) {
      const dcmnt = this.dcmnt;

      const token = this.oauthService.getAccessToken();
      dcmnt.cookie = `token=${token}; expires=${addSeconds(
        new Date(),
        30,
      ).toUTCString()}; Secure; Path=${this.apiPathSig()}`;
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
    }
  }
}
