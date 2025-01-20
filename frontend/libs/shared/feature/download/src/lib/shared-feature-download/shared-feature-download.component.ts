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
import { debounceTime, exhaustMap, filter, map, take, tap } from 'rxjs';

import {
  DokumentService,
  DokumentTyp,
  GesuchService,
} from '@dv/shared/model/gesuch';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';

type DownloadOptions =
  | {
      type: 'berechnungsblatt';
      gesuchId: string;
    }
  | {
      type: 'dokument';
      gesuchTrancheId: string;
      dokumentId: string;
      dokumentTyp: DokumentTyp;
    };

@Component({
  selector: 'dv-shared-feature-download',
  standalone: true,
  imports: [CommonModule, TranslatePipe, SharedUiLoadingComponent],
  templateUrl: './shared-feature-download.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureDownloadComponent implements OnInit {
  id = input.required<string>({ alias: 'id' });
  typeSig = input.required<DownloadOptions['type']>({ alias: 'type' });
  dokumentTyp = input<DokumentTyp | undefined>(undefined, {
    alias: 'dokumentTyp',
  });
  dokumentIdSig = input<string | undefined>(undefined, { alias: 'dokumentId' });

  private gesuchService = inject(GesuchService);
  private dokumentService = inject(DokumentService);
  private dcmnt = inject(DOCUMENT, { optional: true });
  private appRef = inject(ApplicationRef);

  @HostBinding('class') class =
    'd-flex flex-column position-absolute top-0 bottom-0 start-0 end-0 p-5';

  ngOnInit() {
    const downloadOptions = getDownloadOptions(
      this.typeSig(),
      this.id(),
      this.dokumentIdSig(),
      this.dokumentTyp(),
    );

    if (this.dcmnt && downloadOptions) {
      const dcmnt = this.dcmnt;
      getDownloadObservable$(
        downloadOptions,
        this.dokumentService,
        this.gesuchService,
      )
        .pipe(
          tap((href) => {
            dcmnt.location.href = href;
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

const getDocumentDownloadPath = (token: string) => {
  return `/api/v1/dokument/download?token=${token}`;
};

const getBerechnungsblattDownloadPath = (token: string) => {
  return `/api/v1/gesuch/berechnungsblatt?token=${token}`;
};

const getDownloadOptions = (
  type: DownloadOptions['type'],
  id: string,
  dokumentId?: string,
  dokumentTyp?: DokumentTyp,
): DownloadOptions | null => {
  switch (type) {
    case 'berechnungsblatt':
      return {
        type: 'berechnungsblatt',
        gesuchId: id,
      };
    case 'dokument':
      if (!dokumentId || !dokumentTyp) {
        return null;
      }
      return {
        type: 'dokument',
        gesuchTrancheId: id,
        dokumentId,
        dokumentTyp,
      };
  }
};

const getDownloadObservable$ = (
  downloadOptions: DownloadOptions,
  dokumentService: DokumentService,
  gesuchService: GesuchService,
) => {
  switch (downloadOptions.type) {
    case 'berechnungsblatt': {
      return gesuchService
        .getBerechnungsblattDownloadToken$({
          gesuchId: downloadOptions.gesuchId,
        })
        .pipe(map(({ token }) => getBerechnungsblattDownloadPath(token)));
    }
    case 'dokument': {
      return dokumentService
        .getDokumentDownloadToken$({
          gesuchTrancheId: downloadOptions.gesuchTrancheId,
          dokumentId: downloadOptions.dokumentId,
          dokumentTyp: downloadOptions.dokumentTyp,
        })
        .pipe(map((token) => getDocumentDownloadPath(token)));
    }
  }
};
