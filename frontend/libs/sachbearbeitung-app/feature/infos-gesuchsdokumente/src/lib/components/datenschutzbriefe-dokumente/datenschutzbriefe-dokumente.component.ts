import { DatePipe } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
  input,
  signal,
} from '@angular/core';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatTableModule } from '@angular/material/table';
import { RouterLink } from '@angular/router';
import { TranslocoPipe } from '@jsverse/transloco';

import { InfosGesuchsdokumenteStore } from '@dv/sachbearbeitung-app/data-access/infos-gesuchsdokumente';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';
import { paginatorTranslationProvider } from '@dv/shared/util/paginator-translation';
import { isPending } from '@dv/shared/util/remote-data';

const PAGE_SIZE = 10;

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
    RouterLink,
  ],
  providers: [paginatorTranslationProvider()],
  templateUrl: './datenschutzbriefe-dokumente.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DatenschutzbriefeDokumenteComponent {
  private infosStore = inject(InfosGesuchsdokumenteStore);

  // eslint-disable-next-line @angular-eslint/no-input-rename
  gesuchId = input.required<string>({ alias: 'id' });

  displayedColumns = [
    'datum',
    'kategorie',
    'sachbearbeiter',
    'person',
    'massendruck',
  ];

  pageSig = signal(0);
  pageSizeSig = signal(PAGE_SIZE);

  viewSig = computed(() => {
    const allDokumente =
      this.infosStore.datenschutzbriefeDokumenteViewSig() ?? [];
    const loading = isPending(this.infosStore.datenschutzbriefeDokumente());

    return {
      loading,
      allDokumente,
      totalEntries: allDokumente.length,
    };
  });

  paginatedDokumenteSig = computed(() => {
    const { allDokumente } = this.viewSig();
    const page = this.pageSig();
    const pageSize = this.pageSizeSig();
    const startIndex = page * pageSize;
    const endIndex = startIndex + pageSize;

    return allDokumente.slice(startIndex, endIndex);
  });

  constructor() {
    effect(() => {
      const gesuchId = this.gesuchId();
      if (gesuchId) {
        this.infosStore.loadDatenschutzbriefeDokumente$({ gesuchId });
      }
    });
  }

  onPageChange(event: PageEvent) {
    this.pageSig.set(event.pageIndex);
    this.pageSizeSig.set(event.pageSize);
  }
}
