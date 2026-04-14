import { DatePipe } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
  input,
  viewChild,
} from '@angular/core';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { RouterLink } from '@angular/router';

import { InfosGesuchsdokumenteStore } from '@dv/sachbearbeitung-app/data-access/infos-gesuchsdokumente';
import { SachbearbeitungAppUiAdvTranslocoDirective } from '@dv/sachbearbeitung-app/ui/adv-transloco-directive';
import { DEFAULT_PAGE_SIZE, PAGE_SIZES } from '@dv/shared/model/ui-constants';
import { SharedUiDownloadButtonDirective } from '@dv/shared/ui/download-button';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';
import { SharedUiTruncateTooltipDirective } from '@dv/shared/ui/truncate-tooltip';
import { paginatorTranslationProvider } from '@dv/shared/util/paginator-translation';

@Component({
  selector: 'dv-datenschutzbriefe-dokumente',
  standalone: true,
  imports: [
    DatePipe,
    RouterLink,
    MatTooltipModule,
    MatTableModule,
    MatPaginatorModule,
    TypeSafeMatCellDefDirective,
    SharedUiLoadingComponent,
    SachbearbeitungAppUiAdvTranslocoDirective,
    SharedUiTruncateTooltipDirective,
    SharedUiDownloadButtonDirective,
  ],
  providers: [paginatorTranslationProvider()],
  templateUrl: './datenschutzbriefe-dokumente.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DatenschutzbriefeDokumenteComponent {
  infosStore = inject(InfosGesuchsdokumenteStore);

  // eslint-disable-next-line @angular-eslint/no-input-rename
  gesuchIdSig = input.required<string>({ alias: 'gesuchId' });

  pageSizes = PAGE_SIZES;
  defaultPageSize = DEFAULT_PAGE_SIZE;

  paginatorSig = viewChild(MatPaginator);

  displayedColumns = [
    'datum',
    'typ',
    'userErstellt',
    'sozialversicherungsnummer',
    'vorname',
    'nachname',
    'dokument',
  ];

  paginatedDokumenteSig = computed(() => {
    const datenschutzbriefe =
      this.infosStore.datenschutzbriefeDokumenteViewSig().datenschutzbriefe ??
      [];

    const datasource = new MatTableDataSource(datenschutzbriefe);
    const paginator = this.paginatorSig();

    if (paginator) {
      datasource.paginator = paginator;
    }

    return datasource;
  });

  constructor() {
    effect(() => {
      const gesuchId = this.gesuchIdSig();
      if (gesuchId) {
        this.infosStore.loadDatenschutzbriefeDokumente$({ gesuchId });
      }
    });
  }
}
