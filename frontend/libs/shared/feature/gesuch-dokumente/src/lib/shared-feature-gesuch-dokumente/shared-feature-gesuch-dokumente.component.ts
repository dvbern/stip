import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { RouterModule } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';

import { selectSharedDataAccessDokumentesView } from '@dv/shared/data-access/dokumente';
import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import { SharedEventGesuchDokumente } from '@dv/shared/event/gesuch-dokumente';
import { DokumentTyp } from '@dv/shared/model/gesuch';
import {
  DOKUMENTE,
  SharedModelGesuchFormStep,
  gesuchFormSteps,
} from '@dv/shared/model/gesuch-form';
import {
  DOKUMENT_TYP_TO_DOCUMENT_OPTIONS,
  SharedPatternDocumentUploadComponent,
  TableDocument,
  createUploadOptionsFactorySync,
} from '@dv/shared/pattern/document-upload';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { GesuchAppUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';
import { getLatestGesuchIdFromGesuch$ } from '@dv/shared/util/gesuch';

function getFormStep(
  dokumentTyp: DokumentTyp | undefined,
): SharedModelGesuchFormStep {
  const unknownStep: SharedModelGesuchFormStep = {
    route: 'unknown',
    translationKey: 'unknown',
    currentStepNumber: 0,
    iconSymbolName: 'unknown',
  };

  if (!dokumentTyp) {
    return unknownStep;
  }

  const key = Object.keys(gesuchFormSteps).find((key) => {
    if (key === 'EINNAHMEN_KOSTEN') {
      return dokumentTyp.includes('EK');
    }

    return dokumentTyp.includes(key);
  }) as keyof typeof gesuchFormSteps;

  return key ? gesuchFormSteps[key] : unknownStep;
}

@Component({
  selector: 'dv-shared-feature-gesuch-dokumente',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    SharedUiLoadingComponent,
    TranslateModule,
    MatTableModule,
    GesuchAppUiStepFormButtonsComponent,
    SharedPatternDocumentUploadComponent,
  ],
  templateUrl: './shared-feature-gesuch-dokumente.component.html',
  styleUrl: './shared-feature-gesuch-dokumente.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchDokumenteComponent {
  private store = inject(Store);

  displayedColumns = ['status', 'documentName', 'formStep', 'actions'];

  dokumenteSig = this.store.selectSignal(selectSharedDataAccessDokumentesView);
  gesuchViewSig = this.store.selectSignal(selectSharedDataAccessGesuchsView);

  private createUploadOptions = createUploadOptionsFactorySync(
    this.gesuchViewSig,
  );

  dokumenteDataSourceSig = computed(() => {
    const documents = this.dokumenteSig().dokumentes;
    const requiredDocumentTypes = this.dokumenteSig().requiredDocumentTypes;

    const uploadedDocuments: TableDocument[] = documents.map((document) => {
      const documentType = document.dokumentTyp;

      if (!documentType) {
        throw new Error('Document type is missing');
      }

      const documentOptions = this.createUploadOptions(() => documentType, {
        initialDocuments: document.dokumente,
      });

      const formStep = getFormStep(documentType);

      return {
        ...document,
        dokumentTyp: documentType,
        formStep,
        titleKey: DOKUMENT_TYP_TO_DOCUMENT_OPTIONS[documentType],
        documentOptions,
      };
    });

    const missingDocuments: TableDocument[] = requiredDocumentTypes.map(
      (dokumentTyp) => {
        const formStep = getFormStep(dokumentTyp);

        const documentOptions = this.createUploadOptions(() => dokumentTyp, {
          initialDocuments: [],
        });

        return {
          dokumentTyp,
          formStep,
          titleKey: DOKUMENT_TYP_TO_DOCUMENT_OPTIONS[dokumentTyp],
          documentOptions,
        };
      },
    );

    return new MatTableDataSource<TableDocument>([
      ...uploadedDocuments,
      ...missingDocuments,
    ]);
  });

  trackByFn(index: number, item: TableDocument) {
    return item.id ?? item.dokumentTyp;
  }

  constructor() {
    getLatestGesuchIdFromGesuch$(this.gesuchViewSig)
      .pipe(takeUntilDestroyed())
      .subscribe((gesuchId) => {
        this.store.dispatch(
          SharedEventGesuchDokumente.loadDocuments({ gesuchId }),
        );
      });
    this.store.dispatch(SharedEventGesuchDokumente.init());
  }

  handleContinue() {
    const { gesuchId } = this.gesuchViewSig();
    if (gesuchId) {
      this.store.dispatch(
        SharedEventGesuchDokumente.nextTriggered({
          id: gesuchId,
          origin: DOKUMENTE,
        }),
      );
    }
  }
}
