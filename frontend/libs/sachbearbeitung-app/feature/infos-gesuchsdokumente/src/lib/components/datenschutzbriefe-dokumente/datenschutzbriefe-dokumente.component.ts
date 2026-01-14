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
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';
import { paginatorTranslationProvider } from '@dv/shared/util/paginator-translation';

@Component({
  selector: 'dv-datenschutzbriefe-dokumente',
  standalone: true,
  imports: [
    DatePipe,
    TranslocoPipe,
    MatTableModule,
    MatPaginatorModule,
    TypeSafeMatCellDefDirective,
    SharedUiLoadingComponent,
  ],
  providers: [paginatorTranslationProvider()],
  templateUrl: './datenschutzbriefe-dokumente.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DatenschutzbriefeDokumenteComponent {
  infosStore = inject(InfosGesuchsdokumenteStore);

  // eslint-disable-next-line @angular-eslint/no-input-rename
  gesuchId = input.required<string>({ alias: 'id' });

  pageSizes = PAGE_SIZES;
  defaultPageSize = DEFAULT_PAGE_SIZE;

  paginatorSig = viewChild(MatPaginator);

  displayedColumns = [
    'datum',
    'kategorie',
    'sachbearbeiter',
    'person',
    'massendruck',
  ];

  paginatedDokumenteSig = computed(() => {
    const briefe =
      this.infosStore.datenschutzbriefeDokumenteViewSig().datenschutzbriefe ??
      [];

    const datasource = new MatTableDataSource(briefe);
    const paginator = this.paginatorSig();

    if (paginator) {
      datasource.paginator = paginator;
    }

    return datasource;
  });

  constructor() {
    effect(() => {
      const gesuchId = this.gesuchId();
      if (gesuchId) {
        this.infosStore.loadDatenschutzbriefeDokumente$({ gesuchId });
      }
    });
  }
}
