import { A11yModule } from '@angular/cdk/a11y';
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
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltip } from '@angular/material/tooltip';
import { TranslocoPipe } from '@jsverse/transloco';

import { InfosGesuchsdokumenteStore } from '@dv/sachbearbeitung-app/data-access/infos-gesuchsdokumente';
import { SachbearbeitungAppDialogCreateBuchhaltungsKorrekturComponent } from '@dv/sachbearbeitung-app/dialog/create-buchhaltungs-korrektur';
import { DEFAULT_PAGE_SIZE, PAGE_SIZES } from '@dv/shared/model/ui-constants';
import { SharedUiDownloadButtonDirective } from '@dv/shared/ui/download-button';
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
    MatTooltip,
    A11yModule,
    SharedUiDownloadButtonDirective,
  ],
  providers: [paginatorTranslationProvider()],
  templateUrl: './darlehen-dokumente.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DarlehenDokumenteComponent {
  infosStore = inject(InfosGesuchsdokumenteStore);
  private dialog = inject(MatDialog);

  // eslint-disable-next-line @angular-eslint/no-input-rename
  gesuchId = input.required<string>({ alias: 'id' });

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
      const gesuchId = this.gesuchId();
      if (gesuchId) {
        this.infosStore.loadDarlehenBuchhaltungEntrys$({ gesuchId });
      }
    });
  }

  createDarlehenBuchhaltungSaldokorrektur() {
    const gesuchId = this.gesuchId();

    if (!gesuchId) {
      return;
    }

    SachbearbeitungAppDialogCreateBuchhaltungsKorrekturComponent.open(
      this.dialog,
    )
      .afterClosed()
      .subscribe((korrektur) => {
        if (!korrektur) {
          return;
        }
        this.infosStore.createDarlehenBuchhaltungSaldokorrektur$({
          gesuchId,
          darlehenBuchhaltungSaldokorrektur: korrektur,
        });
      });
  }
}
