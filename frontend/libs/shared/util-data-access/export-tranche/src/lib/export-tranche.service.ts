import { Injectable, inject, signal } from '@angular/core';
import { TranslocoService, translateSignal } from '@jsverse/transloco';
import { lastValueFrom } from 'rxjs';

import { translatableShared } from '@dv/shared/assets/i18n';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import {
  AusbildungsstaetteService,
  SteuerdatenService,
} from '@dv/shared/model/gesuch';
import { LandLookupService } from '@dv/shared/util-data-access/land-lookup';

import { getUebersicht } from './structure/01-uebersicht';
import { getAusbildung } from './structure/02-ausbildung';
import { getPersonInAusbildung } from './structure/03-person-in-ausbildung';
import { getLebenslaufItems } from './structure/04-lebenslauf';
import { getPartner } from './structure/05-partner';
import { getKinder } from './structure/06-kinder';
import { getEinnahmenKosten } from './structure/07-einnahmenkosten';
import { getFamiliensituation } from './structure/08-familiensituation';
import { getEltern } from './structure/09-eltern';
import { getSteuererklaerungs } from './structure/10-steuererklaerung';
import { getSteuerdatens } from './structure/11-steuerdaten';
import { getGeschwisters } from './structure/12-geschwister';
import { getFonts, getPageFooter, getPageHeader } from './structure/generic';
import {
  CURRENT_FONT,
  ExportView,
  MARGINS_PAGE,
  PAGE_SIZE,
  STYLES,
} from './types';

@Injectable({
  providedIn: 'root',
})
export class SharedExportTrancheService {
  private config = inject(SharedModelCompileTimeConfig);
  private isExportingSig = signal<boolean>(false);
  private translate = inject(TranslocoService);
  private ausbildungsstaetteService = inject(AusbildungsstaetteService);
  private steuerdatenService = inject(SteuerdatenService);
  private landLookupService = inject(LandLookupService);
  private defaultCommentSig = translateSignal(
    translatableShared('shared.form.tranche.bemerkung.initialgesuch'),
  );

  async exportTranche(view: ExportView) {
    const { tranche, gesuch } = view;
    if (this.isExportingSig()) {
      console.warn('Export already in progress');
      return;
    }
    this.isExportingSig.set(true);

    try {
      const [abschluesse, steuerdaten, { default: pdfmake }] =
        await Promise.all([
          lastValueFrom(
            this.ausbildungsstaetteService.getAllAbschluessForAuswahl$(),
          ),
          lastValueFrom(
            this.steuerdatenService.getSteuerdaten$({
              gesuchTrancheId: tranche.id,
            }),
          ),
          import('pdfmake/build/pdfmake'),
        ]);

      pdfmake.fonts = getFonts(location.origin);
      const laender = this.landLookupService.getCachedLandLookup()().data ?? [];

      const pdf = pdfmake.createPdf({
        header: getPageHeader(this.translate, gesuch, tranche),
        footer: getPageFooter(),
        pageSize: PAGE_SIZE,
        pageMargins: MARGINS_PAGE,
        content: [
          getUebersicht(
            this.translate,
            this.defaultCommentSig(),
            this.config,
            view,
          ),
          getAusbildung(this.translate, tranche),
          getPersonInAusbildung(this.translate, tranche, laender),
          getLebenslaufItems(this.translate, tranche, abschluesse),
          getPartner(this.translate, tranche, laender),
          getKinder(this.translate, tranche),
          getEinnahmenKosten(
            this.translate,
            tranche.gesuchFormular?.einnahmenKosten,
            'pia',
          ),
          getEinnahmenKosten(
            this.translate,
            tranche.gesuchFormular?.einnahmenKostenPartner,
            'partner',
          ),
          getFamiliensituation(this.translate, tranche),
          getEltern(this.translate, tranche, laender),
          getSteuererklaerungs(this.translate, tranche),
          getSteuerdatens(this.translate, steuerdaten),
          getGeschwisters(this.translate, tranche),
        ],
        pageBreakBefore: (currentNode) => {
          const isTableNode = !!currentNode.table;
          const nodeIsMultiPage = currentNode.pageNumbers.length > 1;
          return isTableNode && nodeIsMultiPage;
        },
        defaultStyle: {
          font: CURRENT_FONT,
        },
        styles: STYLES,
      });

      pdf.download(
        `${gesuch.fallNummer}_${gesuch.gesuchNummer}_${tranche.gueltigAb}-${tranche.gueltigBis}.pdf`,
      );
    } catch (error) {
      console.error('Error exporting tranche:', error);
    } finally {
      this.isExportingSig.set(false);
    }
  }
}
