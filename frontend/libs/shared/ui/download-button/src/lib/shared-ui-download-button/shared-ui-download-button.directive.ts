import { DOCUMENT } from '@angular/common';
import { Directive, HostListener, inject, input } from '@angular/core';
import { firstValueFrom, map } from 'rxjs';

import {
  DokumentArt,
  DokumentService,
  GesuchService,
  VerfuegungService,
} from '@dv/shared/model/gesuch';
import { assertUnreachable } from '@dv/shared/model/type-util';

type DownloadOptions =
  | {
      type: 'berechnungsblatt';
      id: string;
    }
  | {
      type: 'verfuegung';
      id: string;
    }
  | {
      type: 'dokument';
      id: string;
      dokumentArt: DokumentArt;
    };

@Directive({
  selector: 'button[dvDownloadButton]',
  standalone: true,
})
export class SharedUiDownloadButtonDirective {
  optionsSig = input.required<DownloadOptions>({ alias: 'dvDownloadButton' });
  private dokumentService = inject(DokumentService);
  private gesuchService = inject(GesuchService);
  private verfuegungService = inject(VerfuegungService);
  private dcmnt = inject(DOCUMENT, { optional: true });

  @HostListener('click')
  onClick() {
    firstValueFrom(
      getDownloadObservable$(
        this.optionsSig(),
        this.dokumentService,
        this.gesuchService,
        this.verfuegungService,
      ),
    ).then((href) => {
      this.dcmnt?.defaultView?.open(href, '_blank');
    });
  }
}

const getVerfuegungsDownloadPath = (token: string) => {
  return `/api/v1/verfuegung/download?token=${token}`;
};

const getDocumentDownloadPath = (token: string, dokumentArt: DokumentArt) => {
  return `/api/v1/dokument/${dokumentArt}/download?token=${token}`;
};

const getBerechnungsblattDownloadPath = (token: string) => {
  return `/api/v1/gesuch/berechnungsblatt?token=${token}`;
};

const getDownloadObservable$ = (
  downloadOptions: DownloadOptions,
  dokumentService: DokumentService,
  gesuchService: GesuchService,
  verfuegungService: VerfuegungService,
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
    case 'verfuegung': {
      return verfuegungService
        .getVerfuegungsDownloadToken$({
          verfuegungsId: id,
        })
        .pipe(map(({ token }) => getVerfuegungsDownloadPath(token)));
    }
    default: {
      assertUnreachable(type);
    }
  }
};
