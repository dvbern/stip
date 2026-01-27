import { DatePipe } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
  signal,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatTooltipModule } from '@angular/material/tooltip';

import { DemoDataAppTranslationKey } from '@dv/demo-data-app/assets/i18n';
import { DemoDataStore } from '@dv/demo-data-app/data-access/demo-data';
import { DemoDataAppUiAdvTranslocoDirective } from '@dv/demo-data-app/ui/adv-transloco-directive';
import { FallStore } from '@dv/shared/data-access/fall';
import { SharedPatternBasicLayoutComponent } from '@dv/shared/pattern/basic-layout';
import { SharedUiConfirmDialogComponent } from '@dv/shared/ui/confirm-dialog';
import { SharedUiDownloadButtonDirective } from '@dv/shared/ui/download-button';
import { SharedUiFileUploadComponent } from '@dv/shared/ui/file-upload';
import { FilesizePipe } from '@dv/shared/ui/filesize-pipe';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiInfoContainerComponent } from '@dv/shared/ui/info-container';
import { SharedUiKommentarDialogComponent } from '@dv/shared/ui/kommentar-dialog';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import { SharedUiRdIsPendingPipe } from '@dv/shared/ui/remote-data-pipe';
import { SharedUiTruncateTooltipDirective } from '@dv/shared/ui/truncate-tooltip';

@Component({
  selector: 'dv-demo-data-app-feature-demo-data-overview',
  imports: [
    DatePipe,
    ReactiveFormsModule,
    FilesizePipe,
    MatFormFieldModule,
    MatInputModule,
    MatTooltipModule,
    SharedUiFileUploadComponent,
    SharedUiDownloadButtonDirective,
    SharedPatternBasicLayoutComponent,
    SharedUiInfoContainerComponent,
    SharedUiIconChipComponent,
    SharedUiMaxLengthDirective,
    SharedUiLoadingComponent,
    SharedUiRdIsPendingPipe,
    SharedUiTruncateTooltipDirective,
    DemoDataAppUiAdvTranslocoDirective,
  ],
  templateUrl: './demo-data-app-feature-demo-data-overview.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DemoDataAppFeatureDemoDataOverviewComponent {
  private dialog = inject(MatDialog);
  demoDataStore = inject(DemoDataStore);
  fallStore = inject(FallStore);
  filterText = new FormControl<string | null>(null);
  selectedFileSig = signal<File | null>(null);

  private filterTextChangedSig = toSignal(this.filterText.valueChanges);

  demoDatasSig = computed(() => {
    const filterText = this.filterTextChangedSig()?.toLowerCase();
    const list = this.demoDataStore.cachedDemoDataListViewSig().data?.demoDatas;

    if (filterText) {
      return list?.filter(
        (item) =>
          item.testFall.toLowerCase().includes(filterText) ||
          item.name.toLowerCase().includes(filterText) ||
          item.description
            .replace(/<[^>]*>?/gm, '')
            .toLowerCase()
            .includes(filterText),
      );
    }
    return list;
  });

  constructor() {
    this.demoDataStore.loadDemoData$();
    this.fallStore.loadCurrentFall$();
  }

  applyDemoData(demoDataId: string) {
    SharedUiConfirmDialogComponent.open<DemoDataAppTranslationKey>(
      this.dialog,
      {
        title: 'demo-data-app.overview.apply-demo-data.confirm.title',
        message: 'demo-data-app.overview.apply-demo-data.confirm.description',
      },
    )
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          this.demoDataStore.applyDemoData$(demoDataId);
        }
      });
  }

  createDemoDataImport() {
    const fileUpload = this.selectedFileSig();
    if (!fileUpload) {
      return;
    }
    SharedUiKommentarDialogComponent.open<DemoDataAppTranslationKey>(
      this.dialog,
      {
        titleKey: 'demo-data-app.overview.file-upload.confirm.title',
        messageKey: 'demo-data-app.overview.file-upload.confirm.description',
        placeholderKey:
          'demo-data-app.overview.file-upload.confirm.placeholder',
        confirmKey: 'demo-data-app.overview.file-upload.confirm.button',
      },
    )
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          this.demoDataStore.createNewDemoDataImport$({
            fileUpload,
            kommentar: result.kommentar,
            onSuccess: () => {
              this.selectedFileSig.set(null);
              this.demoDataStore.loadDemoData$();
            },
          });
        }
      });
  }
}
