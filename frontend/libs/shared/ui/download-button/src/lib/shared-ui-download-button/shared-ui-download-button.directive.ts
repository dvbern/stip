import {
  DOCUMENT,
  Directive,
  HostListener,
  inject,
  input,
} from '@angular/core';
import { firstValueFrom, map } from 'rxjs';

import {
  AusbildungService,
  DarlehenService,
  DatenschutzbriefService,
  DemoDataService,
  DokumentArt,
  DokumentService,
  GesuchService,
  MassendruckService,
  StatistikService,
  VerfuegungService,
} from '@dv/shared/model/gesuch';
import { assertUnreachable } from '@dv/shared/model/type-util';

type IdOnlyTypes =
  | 'darlehen'
  | 'darlehenNegativVerfuegung'
  | 'berechnungsblatt'
  | 'verfuegung'
  | 'massendruck'
  | 'demoData'
  | 'ausbildungUnterbruch'
  | 'sachbearbeiterGesuchDokument'
  | 'statistik';
export type DownloadOptions =
  | {
      type: 'datenschutzbrief';
      id: string;
      gesuchId: string;
    }
  | {
      type: 'datenschutzbriefCreate';
      id: string;
      gesuchId: string;
    }
  | {
      type: 'dokument';
      id: string;
      dokumentArt: DokumentArt;
    }
  | {
      type: IdOnlyTypes;
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
  private ausbildungService = inject(AusbildungService);
  private statistikService = inject(StatistikService);
  private dcmnt = inject(DOCUMENT, { optional: true });

  @HostListener('click')
  onClick() {
    firstValueFrom(this.getDownloadObservable$()).then((href) => {
      this.dcmnt?.defaultView?.open(href, '_blank');
    });
  }

  private getDownloadObservable$() {
    const downloadOptions = this.optionsSig();
    const { type, id } = downloadOptions;
    switch (type) {
      case 'datenschutzbrief': {
        return this.datenschutzbriefService
          .getDatenschutzbriefDownloadToken$({
            gesuchId: downloadOptions.gesuchId,
            datenschutzbriefId: id,
          })
          .pipe(
            map(({ token }) =>
              this.datenschutzbriefService.getDatenschutzbriefPath({ token }),
            ),
          );
      }
      case 'datenschutzbriefCreate': {
        return this.datenschutzbriefService
          .createAndGetDatenschutzbriefDownloadToken$({
            gesuchId: downloadOptions.gesuchId,
            datenschutzbriefCreate: {
              elternId: id,
            },
          })
          .pipe(
            map(({ token }) =>
              this.datenschutzbriefService.getDatenschutzbriefPath({ token }),
            ),
          );
      }
      case 'darlehen': {
        return this.darlehenService
          .getDarlehenDownloadToken$({
            dokumentId: id,
          })
          .pipe(
            map(({ token }) =>
              this.darlehenService.downloadDarlehenDokumentPath({ token }),
            ),
          );
      }
      case 'darlehenNegativVerfuegung': {
        return this.darlehenService
          .getDarlehenNegativVerfuegungDownloadToken$({
            dokumentId: id,
          })
          .pipe(
            map(({ token }) =>
              this.darlehenService.downloadDarlehenNegativVerfuegungPath({
                token,
              }),
            ),
          );
      }
      case 'berechnungsblatt': {
        return this.gesuchService
          .getBerechnungsblattDownloadToken$({
            gesuchId: id,
          })
          .pipe(
            map(
              ({ token }) => `/api/v1/gesuch/berechnungsblatt?token=${token}`,
            ),
          );
      }
      case 'dokument': {
        return this.dokumentService
          .getDokumentDownloadToken$({
            dokumentId: id,
          })
          .pipe(
            map(({ token }) =>
              this.dokumentService.getDokumentPath({
                token,
                dokumentArt: downloadOptions.dokumentArt,
              }),
            ),
          );
      }
      case 'verfuegung': {
        return this.verfuegungService
          .getVerfuegungDokumentDownloadToken$({
            verfuegungDokumentId: id,
          })
          .pipe(
            map(({ token }) =>
              this.verfuegungService.getVerfuegungDokumentPath({ token }),
            ),
          );
      }
      case 'massendruck': {
        return this.massendruckService
          .getMassendruckDownloadToken$({
            massendruckId: id,
          })
          .pipe(
            map(({ token }) =>
              this.massendruckService.downloadMassendruckDocumentPath({
                token,
              }),
            ),
          );
      }
      case 'demoData': {
        return this.demoDataService
          .getDemoDataDokumentDownloadToken$({ dokumentId: id })
          .pipe(
            map(({ token }) =>
              this.demoDataService.getDemoDataDokumentPath({ token }),
            ),
          );
      }
      case 'ausbildungUnterbruch': {
        return this.ausbildungService
          .getAusbildungUnterbruchAntragDokumentDownloadToken$({
            dokumentId: id,
          })
          .pipe(
            map(({ token }) =>
              this.ausbildungService.downloadAusbildungUnterbruchAntragDokumentPath(
                {
                  token,
                },
              ),
            ),
          );
      }
      case 'sachbearbeiterGesuchDokument': {
        return this.dokumentService
          .getSachbearbeiterGesuchDokumentDokumentDownloadToken$({
            dokumentId: id,
          })
          .pipe(
            map(({ token }) =>
              this.dokumentService.getSachbearbeiterGesuchDokumentDokumentPath({
                token,
              }),
            ),
          );
      }
      case 'statistik': {
        return this.statistikService
          .getStatistikDownloadToken$({
            statistikId: id,
          })
          .pipe(
            map(({ token }) =>
              this.statistikService.getStatistikDownloadPath({ token }),
            ),
          );
      }
      default: {
        assertUnreachable(type);
      }
    }
  }
}
