import {
  DOCUMENT,
  Directive,
  HostListener,
  inject,
  input,
} from '@angular/core';
import { firstValueFrom, map } from 'rxjs';

import {
  DatenschutzbriefService,
  DokumentArt,
  DokumentService,
  GesuchService,
  MassendruckService,
  VerfuegungService,
} from '@dv/shared/model/gesuch';
import { assertUnreachable } from '@dv/shared/model/type-util';

type DownloadOptions =
  | {
      type: 'datenschutzbrief';
      id: string;
      gesuchTrancheId: string;
    }
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
    }
  | {
      type: 'massendruck';
      id: string;
    };

@Directive({
  selector: 'button[dvDownloadButton]',
  standalone: true,
})
export class SharedUiDownloadButtonDirective {
  optionsSig = input.required<DownloadOptions>({ alias: 'dvDownloadButton' });
  private datenschutzbriefService = inject(DatenschutzbriefService);
  private dokumentService = inject(DokumentService);
  private gesuchService = inject(GesuchService);
  private verfuegungService = inject(VerfuegungService);
  private massendruckService = inject(MassendruckService);
  private dcmnt = inject(DOCUMENT, { optional: true });

  @HostListener('click')
  onClick() {
    firstValueFrom(
      getDownloadObservable$(
        this.optionsSig(),
        this.datenschutzbriefService,
        this.dokumentService,
        this.gesuchService,
        this.verfuegungService,
        this.massendruckService,
      ),
    ).then((href) => {
      this.dcmnt?.defaultView?.open(href, '_blank');
    });
  }
}

const getDownloadObservable$ = (
  downloadOptions: DownloadOptions,
  datenschutzbriefService: DatenschutzbriefService,
  dokumentService: DokumentService,
  gesuchService: GesuchService,
  verfuegungService: VerfuegungService,
  massendruckService: MassendruckService,
) => {
  const { type, id } = downloadOptions;
  switch (type) {
    case 'datenschutzbrief': {
      return datenschutzbriefService
        .getDatenschutzbriefDownloadToken$({
          elternId: id,
        })
        .pipe(
          map(
            ({ token }) =>
              `/api/v1/datenschutzbrief/${downloadOptions.gesuchTrancheId}/download?token=${token}`,
          ),
        );
    }
    case 'berechnungsblatt': {
      return gesuchService
        .getBerechnungsblattDownloadToken$({
          gesuchId: id,
        })
        .pipe(
          map(({ token }) => `/api/v1/gesuch/berechnungsblatt?token=${token}`),
        );
    }
    case 'dokument': {
      return dokumentService
        .getDokumentDownloadToken$({
          dokumentId: id,
        })
        .pipe(
          map(
            ({ token }) =>
              `/api/v1/dokument/${downloadOptions.dokumentArt}/download?token=${token}`,
          ),
        );
    }
    case 'verfuegung': {
      return verfuegungService
        .getVerfuegungDokumentDownloadToken$({
          verfuegungDokumentId: id,
        })
        .pipe(
          map(
            ({ token }) =>
              `/api/v1/verfuegung/dokument/download?token=${token}`,
          ),
        );
    }
    case 'massendruck': {
      return massendruckService
        .getMassendruckDownloadToken$({
          massendruckId: id,
        })
        .pipe(
          map(({ token }) => `/api/v1/massendruck/download?token=${token}`),
        );
    }
    default: {
      assertUnreachable(type);
    }
  }
};
