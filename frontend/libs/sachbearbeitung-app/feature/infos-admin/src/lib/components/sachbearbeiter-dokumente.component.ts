import {
  Component,
  DestroyRef,
  computed,
  effect,
  inject,
  input,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Store } from '@ngrx/store';

import { SachbearbeiterDokumentsStore } from '@dv/sachbearbeitung-app/data-access/sachbearbeiter-dokuments';
import { SachbearbeitungAppUiAdvTranslocoDirective } from '@dv/sachbearbeitung-app/ui/adv-transloco-directive';
import { selectSharedDataAccessConfigsView } from '@dv/shared/data-access/config';
import { SharedDialogCreateCustomDokumentComponent } from '@dv/shared/dialog/create-custom-dokument';
import { SharedModelTableSachbearbeiterDokument } from '@dv/shared/model/dokument';
import { SharedPatternDocumentUploadComponent } from '@dv/shared/pattern/document-upload';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiRdIsPendingPipe } from '@dv/shared/ui/remote-data-pipe';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';

@Component({
  templateUrl: './sachbearbeiter-dokumente.component.html',
  imports: [
    MatTableModule,
    MatTooltipModule,
    TypeSafeMatCellDefDirective,
    SharedPatternDocumentUploadComponent,
    SharedUiLoadingComponent,
    SharedUiRdIsPendingPipe,
    SachbearbeitungAppUiAdvTranslocoDirective,
  ],
})
export class SachbearbeiterGesuchDokumentComponent {
  private store = inject(Store);
  private dialog = inject(MatDialog);
  private destroyRef = inject(DestroyRef);
  private configSig = this.store.selectSignal(
    selectSharedDataAccessConfigsView,
  );
  dokumentStore = inject(SachbearbeiterDokumentsStore);
  // eslint-disable-next-line @angular-eslint/no-input-rename
  gesuchIdSig = input.required<string>({ alias: 'gesuchId' });

  displayedColumns = ['documentName', 'description', 'actions'];

  dokumenteDataSourceSig = computed(() => {
    const gesuchId = this.gesuchIdSig();
    const dokuments = this.dokumentStore.sachbearbeiterDokuments().data;
    const allowTypes =
      this.configSig().deploymentConfig?.allowedMimeTypes?.join(',');
    const dataSource =
      new MatTableDataSource<SharedModelTableSachbearbeiterDokument>([]);

    if (!gesuchId || !dokuments || !allowTypes) {
      return dataSource;
    }

    dataSource.data = dokuments.map<SharedModelTableSachbearbeiterDokument>(
      (dokument) => ({
        ...dokument,
        canDelete: !dokument.dokumente.length,
        dokumentOptions: {
          dokument: {
            art: 'SACHBEARBEITER_GESUCH_DOKUMENT',
            dokumentId: dokument.id,
            gesuchId,
            dokumentTyp: dokument,
          },
          allowTypes,
          info: {
            type: 'TEXT',
            title: dokument.type,
            description: dokument.description,
          },
          initialDokumente: dokument.dokumente,
        },
      }),
    );
    return dataSource;
  });

  constructor() {
    effect(() => {
      const gesuchId = this.gesuchIdSig();
      if (!gesuchId) {
        return;
      }

      this.dokumentStore.loadSachbearbeiterDokuments$({ gesuchId });
    });
  }

  createSachbearbeiterDokument() {
    const gesuchId = this.gesuchIdSig();
    if (!gesuchId) {
      return;
    }

    SharedDialogCreateCustomDokumentComponent.open(this.dialog, {
      hideDescription: true,
    })
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((result) => {
        if (result) {
          this.dokumentStore.createSachbearbeiterDokument$({
            gesuchId,
            type: result.name,
            description: result.kommentar,
            onSuccess: () => {
              this.dokumentStore.loadSachbearbeiterDokuments$({ gesuchId });
            },
          });
        }
      });
  }

  deleteSachbearbeiterDokument(id: string) {
    const gesuchId = this.gesuchIdSig();
    if (!gesuchId) {
      return;
    }

    this.dokumentStore.deleteSachbearbeiterDokument$({
      id,
      onSuccess: () => {
        this.dokumentStore.loadSachbearbeiterDokuments$({ gesuchId });
      },
    });
  }

  documentsChanged() {
    const gesuchId = this.gesuchIdSig();
    if (!gesuchId) {
      return;
    }
    this.dokumentStore.loadSachbearbeiterDokuments$({ gesuchId });
  }

  trackByFn(_index: number, item: SharedModelTableSachbearbeiterDokument) {
    return item.type;
  }
}
