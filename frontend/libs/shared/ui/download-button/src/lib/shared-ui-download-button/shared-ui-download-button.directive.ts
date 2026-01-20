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
      id: string;
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
  const { type, id } = downloadOptions;
  switch (type) {
    case 'datenschutzbrief': {
      return datenschutzbriefService
        .getDatenschutzbriefDownloadToken$({
          elternId: id,
        })
        .pipe(
          map(({ token }) =>
            datenschutzbriefService.getDatenschutzbriefPath({
              token,
              trancheId: downloadOptions.gesuchTrancheId,
            }),
          ),
        );
    }
    case 'darlehen': {
      return darlehenService
        .getDarlehenDownloadToken$({
          dokumentId: id,
        })
        .pipe(
          map(({ token }) =>
            darlehenService.downloadDarlehenDokumentPath({ token }),
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
          map(({ token }) =>
            dokumentService.getDokumentPath({
              token,
              dokumentArt: downloadOptions.dokumentArt,
            }),
          ),
        );
    }
    case 'verfuegung': {
      return verfuegungService
        .getVerfuegungDokumentDownloadToken$({
          verfuegungDokumentId: id,
        })
        .pipe(
          map(({ token }) =>
            verfuegungService.getVerfuegungDokumentPath({ token }),
          ),
        );
    }
    case 'massendruck': {
      return massendruckService
        .getMassendruckDownloadToken$({
          massendruckId: id,
        })
        .pipe(
          map(({ token }) =>
            massendruckService.downloadMassendruckDocumentPath({ token }),
          ),
        );
    }
    case 'demoData': {
      return demoDataService
        .getDemoDataDokumentDownloadToken$({ dokumentId: id })
        .pipe(
          map(({ token }) =>
            demoDataService.getDemoDataDokumentPath({ token }),
          ),
        );
    }
    default: {
      assertUnreachable(type);
    }
  }
};
