import { DatePipe } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  inject,
  signal,
} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

import { DemoDataAppTranslationKey } from '@dv/demo-data-app/assets/i18n';
import { DemoDataStore } from '@dv/demo-data-app/data-access/demo-data';
import { DemoDataAppUiAdvTranslocoDirective } from '@dv/demo-data-app/ui/adv-transloco-directive';
import { SharedPatternBasicLayoutComponent } from '@dv/shared/pattern/basic-layout';
import { SharedUiDownloadButtonDirective } from '@dv/shared/ui/download-button';
import { SharedUiFileUploadComponent } from '@dv/shared/ui/file-upload';
import { FilesizePipe } from '@dv/shared/ui/filesize-pipe';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiInfoContainerComponent } from '@dv/shared/ui/info-container';
import { SharedUiKommentarDialogComponent } from '@dv/shared/ui/kommentar-dialog';

@Component({
  selector: 'dv-demo-data-app-feature-demo-data-overview',
  imports: [
    DatePipe,
    FilesizePipe,
    SharedUiFileUploadComponent,
    SharedUiDownloadButtonDirective,
    SharedPatternBasicLayoutComponent,
    SharedUiInfoContainerComponent,
    SharedUiIconChipComponent,
    DemoDataAppUiAdvTranslocoDirective,
  ],
  templateUrl: './demo-data-app-feature-demo-data-overview.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DemoDataAppFeatureDemoDataOverviewComponent {
  private dialog = inject(MatDialog);
  demoDataStore = inject(DemoDataStore);
  selectedFileSig = signal<File | null>(null);

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
            },
          });
        }
      });
  }
}
