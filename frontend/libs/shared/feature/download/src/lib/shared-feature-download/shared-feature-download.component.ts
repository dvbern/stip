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
  DokumentArt,
  DokumentService,
  GesuchService,
} from '@dv/shared/model/gesuch';
import { assertUnreachable } from '@dv/shared/model/type-util';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';

type DownloadOptions =
  | {
      type: 'berechnungsblatt';
      id: string;
    }
  | {
      type: 'dokument';
      id: string;
      dokumentArt: DokumentArt;
    };
type DownloadType = DownloadOptions['type'];

@Component({
  selector: 'dv-shared-feature-download',
  standalone: true,
  imports: [CommonModule, TranslatePipe, SharedUiLoadingComponent],
  templateUrl: './shared-feature-download.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureDownloadComponent implements OnInit {
  idSig = input.required<string>({ alias: 'id' });
  typeSig = input.required<DownloadType>({ alias: 'type' });
  dokumentArtSig = input<DokumentArt | undefined>(undefined, {
    alias: 'dokumentArt',
  });

  private gesuchService = inject(GesuchService);
  private dokumentService = inject(DokumentService);
  private dcmnt = inject(DOCUMENT, { optional: true });
  private appRef = inject(ApplicationRef);

  @HostBinding('class') class =
    'd-flex flex-column position-absolute top-0 bottom-0 start-0 end-0 p-5';

  ngOnInit() {
    const downloadOptions = createDownloadOptions(
      this.typeSig(),
      this.idSig(),
      this.dokumentArtSig(),
    );
    if (!downloadOptions) {
      console.error('Invalid download options', downloadOptions);
      return;
    }
    if (this.dcmnt) {
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

const createDownloadOptions = (
  type: DownloadType,
  id: string,
  dokumentArt?: DokumentArt,
) => {
  switch (type) {
    case 'berechnungsblatt': {
      return { type, id };
    }
    case 'dokument': {
      return dokumentArt ? { type, id, dokumentArt } : null;
    }
    default: {
      assertUnreachable(type);
    }
  }
};

const getDocumentDownloadPath = (token: string, dokumentArt: DokumentArt) => {
  return `/api/v1/dokument//${dokumentArt}/download?token=${token}`;
};

const getBerechnungsblattDownloadPath = (token: string) => {
  return `/api/v1/gesuch/berechnungsblatt?token=${token}`;
};

const getDownloadObservable$ = (
  downloadOptions: DownloadOptions,
  dokumentService: DokumentService,
  gesuchService: GesuchService,
) => {
  const { type, id } = downloadOptions;
  switch (type) {
    case 'berechnungsblatt': {
      return gesuchService
        .getBerechnungsblattDownloadToken$({
          gesuchId: id,
        })
        .pipe(map(({ token }) => getBerechnungsblattDownloadPath(token)));
    }
    case 'dokument': {
      return dokumentService
        .getDokumentDownloadToken$({
          dokumentId: id,
        })
        .pipe(
          map(({ token }) =>
            getDocumentDownloadPath(token, downloadOptions.dokumentArt),
          ),
        );
    }
    default: {
      assertUnreachable(type);
    }
  }
};
