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
import { TranslocoPipe } from '@jsverse/transloco';

import { InfosGesuchsdokumenteStore } from '@dv/sachbearbeitung-app/data-access/infos-gesuchsdokumente';
import { VerfuegungDokument } from '@dv/shared/model/gesuch';
import { DEFAULT_PAGE_SIZE, PAGE_SIZES } from '@dv/shared/model/ui-constants';
import { SharedUiDownloadButtonDirective } from '@dv/shared/ui/download-button';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import {
  TypeSafeMatCellDefDirective,
  TypeSafeMatRowDefDirective,
} from '@dv/shared/ui/table-helper';
import { paginatorTranslationProvider } from '@dv/shared/util/paginator-translation';

type DokumenteColumns = {
  datum: string;
  verfuegungsbrief?: VerfuegungDokument;
  versendeteVerfuegung?: VerfuegungDokument;
  berechnungsblaetter: VerfuegungDokument[];
  // 'gesetzlichesDarlehen'?: VerfuegungDokument; Todo KSTIP-2584
};

@Component({
  selector: 'dv-stipendien-dokumente',
  standalone: true,
  imports: [
    DatePipe,
    TranslocoPipe,
    MatTableModule,
    MatPaginatorModule,
    MatTooltip,
    TypeSafeMatCellDefDirective,
    SharedUiLoadingComponent,
    TypeSafeMatCellDefDirective,
    TypeSafeMatRowDefDirective,
    SharedUiDownloadButtonDirective,
  ],
  providers: [paginatorTranslationProvider()],
  templateUrl: './stipendien-dokumente.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class StipendienDokumenteComponent {
  infosStore = inject(InfosGesuchsdokumenteStore);

  // eslint-disable-next-line @angular-eslint/no-input-rename
  gesuchId = input.required<string>({ alias: 'id' });

  pageSizes = PAGE_SIZES;
  defaultPageSize = DEFAULT_PAGE_SIZE;

  paginatorSig = viewChild(MatPaginator);

  displayedColumns: (keyof DokumenteColumns)[] = [
    'datum',
    'versendeteVerfuegung',
    'verfuegungsbrief',
    'berechnungsblaetter',
    // 'gesetzlichesDarlehen', Todo KSTIP-2584
  ];

  paginatedDokumenteSig = computed(() => {
    const verfuegungen =
      this.infosStore.verfuegungenViewSig().verfuegungen ?? [];

    const data = verfuegungen.map((v) => {
      return v.dokumente.reduce(
        (acc, doc) => {
          switch (doc.typ) {
            case 'VERFUEGUNGSBRIEF':
              acc.verfuegungsbrief = doc;
              break;
            case 'VERSENDETE_VERFUEGUNG':
              acc.versendeteVerfuegung = doc;
              break;
            case 'BERECHNUNGSBLATT_PIA':
            case 'BERECHNUNGSBLATT_MUTTER':
            case 'BERECHNUNGSBLATT_VATER':
            case 'BERECHNUNGSBLATT_FAMILIE':
              acc.berechnungsblaetter.push(doc);
              break;
          }
          return acc;
        },
        {
          datum: v.timestampErstellt,
          verfuegungsId: v.id,
          verfuegungsbrief: undefined as VerfuegungDokument | undefined,
          versendeteVerfuegung: undefined as VerfuegungDokument | undefined,
          berechnungsblaetter: [] as VerfuegungDokument[],
          // 'gesetzlichesDarlehen': undefined as VerfuegungDokument | undefined, Todo KSTIP-2584
        } satisfies DokumenteColumns & { verfuegungsId: string },
      );
    });

    const datasource = new MatTableDataSource(data);
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
        this.infosStore.loadVerfuegungDokumente$({ gesuchId });
      }
    });
  }
}
