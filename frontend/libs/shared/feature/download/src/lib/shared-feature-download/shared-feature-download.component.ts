import { CommonModule, DOCUMENT } from '@angular/common';
import {
  ApplicationRef,
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  OnInit,
  inject,
  input,
} from '@angular/core';
import { TranslatePipe } from '@ngx-translate/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { debounceTime, exhaustMap, filter, take, tap } from 'rxjs';

import { DokumentService, DokumentTyp } from '@dv/shared/model/gesuch';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';

@Component({
  selector: 'dv-shared-feature-download',
  standalone: true,
  imports: [CommonModule, TranslatePipe, SharedUiLoadingComponent],
  templateUrl: './shared-feature-download.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureDownloadComponent implements OnInit {
  gesuchTrancheIdSig = input.required<string>({ alias: 'gesuchTrancheId' });
  typeSig = input.required<DokumentTyp>({ alias: 'type' });
  dokumentIdSig = input.required<string>({ alias: 'dokumentId' });
  oauthService = inject(OAuthService);
  dokumentService = inject(DokumentService);
  dcmnt = inject(DOCUMENT, { optional: true });
  appRef = inject(ApplicationRef);

  @HostBinding('class') class =
    'd-flex flex-column position-absolute top-0 bottom-0 start-0 end-0 p-5';

  private getDownloadPath = (token: string) => {
    return `/api/v1/dokument/download?token=${token}`;
  };

  ngOnInit() {
    if (this.dcmnt) {
      const dcmnt = this.dcmnt;

      this.dokumentService
        .getDokumentDownloadToken$({
          gesuchTrancheId: this.gesuchTrancheIdSig(),
          dokumentId: this.dokumentIdSig(),
          dokumentTyp: this.typeSig(),
        })
        .pipe(
          tap((token) => {
            dcmnt.location.href = this.getDownloadPath(token);
          }),
          exhaustMap(() =>
            this.appRef.isStable.pipe(
              filter((isStable) => isStable),
              debounceTime(1000),
              take(1),
            ),
          ),
        )
        .subscribe(() => {
          dcmnt.defaultView?.close();
        });
    }
  }
}
