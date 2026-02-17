import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
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
import { TranslocoPipe } from '@jsverse/transloco';
import { Store } from '@ngrx/store';

import { SachbearbeitungAppTranslationKey } from '@dv/sachbearbeitung-app/assets/i18n';
import { AusbildungAdminStore } from '@dv/sachbearbeitung-app/data-access/ausbildung-admin';
import { SachbearbeitungAppUiAdvTranslocoDirective } from '@dv/sachbearbeitung-app/ui/adv-transloco-directive';
import { selectSharedDataAccessConfigsView } from '@dv/shared/data-access/config';
import { GlobalNotificationStore } from '@dv/shared/global/notification';
import {
  SharedPatternDocumentUploadComponent,
  createSimpleDokumentOptions,
} from '@dv/shared/pattern/document-upload';
import { SharedUiKommentarDialogComponent } from '@dv/shared/ui/kommentar-dialog';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';

import { AusbildungUnterbruchAntragEntry } from '../types';
import { UnterbruchInfoDialogComponent } from './unterbruch-info-dialog/unterbruch-info-dialog.component';

@Component({
  imports: [
    CommonModule,
    TranslocoPipe,
    MatTableModule,
    SachbearbeitungAppUiAdvTranslocoDirective,
    SharedPatternDocumentUploadComponent,
    SharedUiLoadingComponent,
    TypeSafeMatCellDefDirective,
  ],
  templateUrl: './ausbildung-unterbrechen.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AusbildungUnterbrechenComponent {
  private store = inject(Store);
  private destroyRef = inject(DestroyRef);
  private dialog = inject(MatDialog);
  private globalNotificationStore = inject(GlobalNotificationStore);
  private ausbildungStore = inject(AusbildungAdminStore);
  private config = this.store.selectSignal(selectSharedDataAccessConfigsView);
  unterbruchListSig = computed<AusbildungUnterbruchAntragEntry[]>(() => {
    const allowTypes =
      this.config().deploymentConfig?.allowedMimeTypes?.join(',');
    if (!allowTypes) {
      return [];
    }
    return this.ausbildungStore
      .ausbildungUnterbruchListViewSig()
      .map((unterbruch) => ({
        ...unterbruch,
        documentDownloadOptions: createSimpleDokumentOptions({
          dokumentTyp: 'ausbildungUnterbruch',
          id: unterbruch.id,
          allowTypes,
          initialDokumente: unterbruch.dokuments,
          info: {
            type: 'TRANSLATABLE',
            title: 'shared.ausbildung-unterbrechen.dokumente.title',
            description: 'shared.ausbildung-unterbrechen.dokumente.description',
          },
          readonly: true,
        }),
      }));
  });

  // eslint-disable-next-line @angular-eslint/no-input-rename
  gesuchIdSig = input.required<string>({ alias: 'id' });

  gesuchTableColumns = ['timestamp', 'user', 'kommentar', 'actions'];

  gesuchUnterbrechenSig = computed(() => {
    return new MatTableDataSource(this.unterbruchListSig());
  });

  constructor() {
    effect(() => {
      const gesuchId = this.gesuchIdSig();
      if (!gesuchId) {
        return;
      }

      this.ausbildungStore.loadAusbildungUnterbrueche$({ gesuchId });
    });
  }

  showInfo(unterbruch: AusbildungUnterbruchAntragEntry) {
    UnterbruchInfoDialogComponent.open(this.dialog, unterbruch)
      .afterClosed()
      .subscribe((result) => {
        if (!result) {
          return;
        }
        const { data, status } = result;
        const gesuchId = this.gesuchIdSig();

        this.ausbildungStore.updateAusbildungUnterbruch$({
          data: {
            ausbildungUnterbruchAntragId: unterbruch.id,
            updateAusbildungUnterbruchAntragSB: data,
          },
          onSuccess: () => {
            this.globalNotificationStore.createSuccessNotification<SachbearbeitungAppTranslationKey>(
              {
                messageKey: `sachbearbeitung-app.infos.admin.ausbildung-unterbrechen.info.${status}.success`,
              },
            );
            this.ausbildungStore.loadAusbildungUnterbrueche$({ gesuchId });
          },
        });
      });
  }

  gesuchUnterbrechen() {
    SharedUiKommentarDialogComponent.open(this.dialog, {
      titleKey: 'sachbearbeitung-app.infos.admin.ausbildung-unterbrechen',
      messageKey:
        'sachbearbeitung-app.infos.admin.ausbildung-unterbrechen.message',
      placeholderKey: 'sachbearbeitung-app.infos.admin.kommentar.placeholder',
      confirmKey: 'sachbearbeitung-app.infos.admin.ausbildung-unterbrechen',
    })
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((result) => {
        if (result) {
          // do something
        }
      });
  }
}
