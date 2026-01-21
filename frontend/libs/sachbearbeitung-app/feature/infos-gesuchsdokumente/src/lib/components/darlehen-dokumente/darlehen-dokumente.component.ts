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
import { TranslocoPipe } from '@jsverse/transloco';

import { InfosGesuchsdokumenteStore } from '@dv/sachbearbeitung-app/data-access/infos-gesuchsdokumente';
import { DEFAULT_PAGE_SIZE, PAGE_SIZES } from '@dv/shared/model/ui-constants';
import { SharedUiFormatChfPipe } from '@dv/shared/ui/format-chf-pipe';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';
import { paginatorTranslationProvider } from '@dv/shared/util/paginator-translation';

@Component({
  selector: 'dv-darlehen-dokumente',
  standalone: true,
  imports: [
    DatePipe,
    TranslocoPipe,
    MatTableModule,
    MatPaginatorModule,
    TypeSafeMatCellDefDirective,
    SharedUiLoadingComponent,
    SharedUiFormatChfPipe,
  ],
  providers: [paginatorTranslationProvider()],
  templateUrl: './darlehen-dokumente.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DarlehenDokumenteComponent {
  infosStore = inject(InfosGesuchsdokumenteStore);

  // eslint-disable-next-line @angular-eslint/no-input-rename
  fallId = input.required<string>({ alias: 'fallId' });

  pageSizes = PAGE_SIZES;
  defaultPageSize = DEFAULT_PAGE_SIZE;

  paginatorSig = viewChild(MatPaginator);

  displayedColumns = [
    'timestampErstellt',
    'kategorie',
    'verfuegung',
    'betrag',
    'userErstellt',
    'kommentar',
  ];

  paginatedDokumenteSig = computed(() => {
    const entries =
      this.infosStore.darlehenBuchhaltungViewSig().darlehenBuchhaltung
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
        this.infosStore.loadDarlehenBuchhaltungEntrys$({ fallId });
      }
    });
  }
}
