import {
  DOCUMENT,
  Directive,
  HostListener,
  inject,
  input,
} from '@angular/core';
import { firstValueFrom, map } from 'rxjs';

import {
  DarlehenService,
  DatenschutzbriefService,
  DemoDataService,
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
      type: 'darlehen';
      id: string;
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
    }
  | {
      type: 'demoData';
    };

@Directive({
  selector: 'button[dvDownloadButton]',
  standalone: true,
})
export class SharedUiDownloadButtonDirective {
  optionsSig = input.required<DownloadOptions>({ alias: 'dvDownloadButton' });
  private datenschutzbriefService = inject(DatenschutzbriefService);
  private darlehenService = inject(DarlehenService);
  private dokumentService = inject(DokumentService);
  private gesuchService = inject(GesuchService);
  private verfuegungService = inject(VerfuegungService);
  private massendruckService = inject(MassendruckService);
  private demoDataService = inject(DemoDataService);
  private dcmnt = inject(DOCUMENT, { optional: true });

  @HostListener('click')
  onClick() {
    firstValueFrom(
      getDownloadObservable$(
        this.optionsSig(),
        this.datenschutzbriefService,
        this.darlehenService,
        this.dokumentService,
        this.gesuchService,
        this.verfuegungService,
        this.massendruckService,
        this.demoDataService,
      ),
    ).then((href) => {
      this.dcmnt?.defaultView?.open(href, '_blank');
    });
  }
}

const getDownloadObservable$ = (
  downloadOptions: DownloadOptions,
  datenschutzbriefService: DatenschutzbriefService,
  darlehenService: DarlehenService,
  dokumentService: DokumentService,
  gesuchService: GesuchService,
  verfuegungService: VerfuegungService,
  massendruckService: MassendruckService,
  demoDataService: DemoDataService,
) => {
  const type = downloadOptions.type;
  switch (type) {
    case 'datenschutzbrief': {
      return datenschutzbriefService
        .getDatenschutzbriefDownloadToken$({
          elternId: downloadOptions.id,
        })
        .pipe(
          map(
            ({ token }) =>
              `/api/v1/datenschutzbrief/${downloadOptions.gesuchTrancheId}/download?token=${token}`,
          ),
        );
    }
    case 'darlehen': {
      return darlehenService
        .getDarlehenDownloadToken$({
          dokumentId: downloadOptions.id,
        })
        .pipe(
          map(
            ({ token }) => `/api/v1/darlehen/dokument/download?token=${token}`,
          ),
        );
    }
    case 'berechnungsblatt': {
      return gesuchService
        .getBerechnungsblattDownloadToken$({
          gesuchId: downloadOptions.id,
        })
        .pipe(
          map(({ token }) => `/api/v1/gesuch/berechnungsblatt?token=${token}`),
        );
    }
    case 'dokument': {
      return dokumentService
        .getDokumentDownloadToken$({
          dokumentId: downloadOptions.id,
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
          verfuegungDokumentId: downloadOptions.id,
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
          massendruckId: downloadOptions.id,
        })
        .pipe(
          map(({ token }) => `/api/v1/massendruck/download?token=${token}`),
        );
    }
    case 'demoData': {
      return demoDataService
        .getDemoDataDokumentDownloadToken$()
        .pipe(map(({ token }) => `/api/v1/demo-data/download?token=${token}`));
    }
    default: {
      assertUnreachable(type);
    }
  }
};
