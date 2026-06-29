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
import { MatTooltip } from '@angular/material/tooltip';

import { FallDokumenteStore } from '@dv/shared/data-access/fall-dokumente';
import { DEFAULT_PAGE_SIZE, PAGE_SIZES } from '@dv/shared/model/ui-constants';
import { SharedUiAdvTranslocoDirective } from '@dv/shared/ui/adv-transloco-directive';
import { SharedUiDownloadButtonDirective } from '@dv/shared/ui/download-button';
import { SharedUiFormatChfPipe } from '@dv/shared/ui/format-chf-pipe';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';
import { paginatorTranslationProvider } from '@dv/shared/util/paginator-translation';

@Component({
  selector: 'dv-darlehen-dokumente-fall',
  standalone: true,
  imports: [
    DatePipe,
    MatTableModule,
    MatPaginatorModule,
    TypeSafeMatCellDefDirective,
    SharedUiLoadingComponent,
    SharedUiFormatChfPipe,
    MatTooltip,
    SharedUiDownloadButtonDirective,
    SharedUiAdvTranslocoDirective,
  ],
  providers: [paginatorTranslationProvider()],
  templateUrl: './darlehen-dokumente.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DarlehenDokumenteComponent {
  fallDokumenteStore = inject(FallDokumenteStore);

  // eslint-disable-next-line @angular-eslint/no-input-rename
  fallId = input.required<string>({ alias: 'fallId' });

  pageSizes = PAGE_SIZES;
  defaultPageSize = DEFAULT_PAGE_SIZE;

  paginatorSig = viewChild(MatPaginator);

  displayedColumns = [
    'timestampErstellt',
    'kategorie',
    'yearRange',
    'betrag',
    'verfuegung',
  ];

  paginatedDokumenteSig = computed(() => {
    const entries =
      this.fallDokumenteStore.darlehenBuchhaltungViewSig().darlehenBuchhaltung
        ?.darlehenBuchhaltungEntrys ?? [];

    const datasource = new MatTableDataSource(entries);
    const paginator = this.paginatorSig();

    if (paginator) {
      datasource.paginator = paginator;
    }

    return datasource;
  });

  constructor() {
    effect(() => {
      const fallId = this.fallId();
      if (fallId) {
        this.fallDokumenteStore.loadDarlehenBuchhaltungEntrys$({ fallId });
      }
    });
  }
}
