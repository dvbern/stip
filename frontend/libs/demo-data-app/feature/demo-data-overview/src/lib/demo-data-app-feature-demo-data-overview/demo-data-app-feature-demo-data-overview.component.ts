import { DatePipe } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
  input,
  signal,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatMenuModule } from '@angular/material/menu';
import { MatTooltipModule } from '@angular/material/tooltip';
import { diff } from 'json-diff-ts';

import { DemoDataAppTranslationKey } from '@dv/demo-data-app/assets/i18n';
import { DemoDataStore } from '@dv/demo-data-app/data-access/demo-data';
import { DemoDataAppUiAdvTranslocoDirective } from '@dv/demo-data-app/ui/adv-transloco-directive';
import { FallStore } from '@dv/shared/data-access/fall';
import { GlobalNotificationStore } from '@dv/shared/global/notification';
import { DemoDataTestBerechnungResultat } from '@dv/shared/model/gesuch';
import { type } from '@dv/shared/model/type-util';
import { TOOLTIP_DELAY } from '@dv/shared/model/ui-constants';
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
import { localStorageValue } from '@dv/shared/util/local-storage-helper';

import { BerechnungComparisonDialogComponent } from '../components/comparison/berechnung-comparison-dialog.component';
import { SollIstComponent } from '../components/comparison/soll-ist.component';

@Component({
  selector: 'dv-demo-data-app-feature-demo-data-overview',
  imports: [
    DatePipe,
    ReactiveFormsModule,
    FilesizePipe,
    MatFormFieldModule,
    MatInputModule,
    MatTooltipModule,
    MatMenuModule,
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
    SollIstComponent,
  ],
  templateUrl: './demo-data-app-feature-demo-data-overview.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DemoDataAppFeatureDemoDataOverviewComponent {
  private dialog = inject(MatDialog);
  private globalNotificationStore = inject(GlobalNotificationStore);
  previousBerechnungsResult = localStorageValue<
    DemoDataTestBerechnungResultat[]
  >('DEMO_DATA_PREVIOUS_BERECHNUNG_RESULT');
  demoDataStore = inject(DemoDataStore);
  fallStore = inject(FallStore);
  filterText = new FormControl<string | null>(null);
  selectedFileSig = signal<File[] | undefined>(undefined);
  tooltipDelay = TOOLTIP_DELAY;

  validateBerechnungSig = input<boolean>(false, {
    // eslint-disable-next-line @angular-eslint/no-input-rename
    alias: 'validateBerechnung',
  });

  private filterTextChangedSig = toSignal(this.filterText.valueChanges);
  demoDatasSig = computed(() => {
    const filterText = this.filterTextChangedSig()?.toLowerCase();
    const testResultsMap =
      this.demoDataStore.demoDataTestBerechnungResultatsSig();
    const list = this.demoDataStore
      .cachedDemoDataListViewSig()
      .data?.demoDatas.map((demoData) => {
        const result = testResultsMap[demoData.id];
        return {
          ...demoData,
          testResult: result
            ? {
                allValid: Object.values(result.valid ?? {}).every(Boolean),
                ...result,
              }
            : null,
        };
      });

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
            fileUpload: fileUpload[0],
            ignoreBerechnungErrors: !this.validateBerechnungSig(),
            kommentar: result.kommentar,
            onSuccess: () => {
              this.selectedFileSig.set(undefined);
              this.demoDataStore.loadDemoData$();
            },
          });
        }
      });
  }

  testDemoDataBerechnung() {
    this.demoDataStore.testAllDemoDataBerechnung$();
  }

  saveResult(results: DemoDataTestBerechnungResultat[]) {
    this.previousBerechnungsResult.set(results);
    this.globalNotificationStore.createSuccessNotification<DemoDataAppTranslationKey>(
      {
        messageKey:
          'demo-data-app.overview.apply-demo-data.berechnung-save.success',
      },
    );
  }

  compareResults<T extends DemoDataTestBerechnungResultat>(
    previous: T[],
    current: T[],
  ) {
    const changesRaw = diff(previous, current, {
      keysToSkip: ['demoDataId'],
      embeddedObjKeys: { '.': 'testFall' },
      treatTypeChangeAsReplace: false,
    });

    const changes = changesRaw
      .flatMap((c) => c.changes ?? [])
      .map((c) => ({
        ...c,
        berechnung: c?.key
          ? type<DemoDataTestBerechnungResultat>(
              previous[+c.key] ?? current[+c.key],
            )
          : null,
      }));
    BerechnungComparisonDialogComponent.open(this.dialog, changes);
  }

  generateAllGesucheAsVerfuegt() {
    SharedUiConfirmDialogComponent.open<DemoDataAppTranslationKey>(
      this.dialog,
      {
        title: 'demo-data-app.overview.generate-multi.persistent',
        message: 'demo-data-app.overview.generate-multi.persistent.warning',
      },
    )
      .afterClosed()
      .subscribe((confirmed) => {
        if (!confirmed) {
          return;
        }

        this.demoDataStore.generateAllGesucheAsVerfuegt$();
      });
  }

  getStatistikXmlWithAllTestcases() {
    this.demoDataStore.getStatistikXmlWithAllTestcases$();
  }

  copyToClipboard(text: string) {
    navigator.clipboard.writeText(text);
  }
}
