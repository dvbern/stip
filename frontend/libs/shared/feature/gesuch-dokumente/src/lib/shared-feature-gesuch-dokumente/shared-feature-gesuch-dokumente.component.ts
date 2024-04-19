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
import { Dokument, DokumentTyp } from '@dv/shared/model/gesuch';
import {
  DOKUMENTE,
  SharedModelGesuchFormStep,
  gesuchFormSteps,
} from '@dv/shared/model/gesuch-form';
import {
  DOKUMENT_TYP_TO_DOCUMENT_OPTIONS,
  SharedPatternDocumentUploadTableComponent,
} from '@dv/shared/pattern/document-upload';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { GesuchAppUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';
import { getLatestGesuchIdFromGesuch$ } from '@dv/shared/util/gesuch';

interface TableDocument {
  id?: string;
  formStep: SharedModelGesuchFormStep;
  dokumentTyp?: DokumentTyp;
  titleKey?: string;
  dokumente?: Dokument[];
}

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
    SharedPatternDocumentUploadTableComponent,
  ],
  templateUrl: './shared-feature-gesuch-dokumente.component.html',
  styleUrl: './shared-feature-gesuch-dokumente.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchDokumenteComponent {
  private store = inject(Store);

  displayedColumns = [
    'status',
    'documentName',
    'formStep',
    // 'filename',
    'actions',
  ];

  viewSig = this.store.selectSignal(selectSharedDataAccessDokumentesView);
  gesuchViewSig = this.store.selectSignal(selectSharedDataAccessGesuchsView);

  dokumenteDataSourceSig = computed(() => {
    const documents = this.viewSig().dokumentes;
    const requiredDocumentTypes = this.viewSig().requiredDocumentTypes;

    if (!documents.length) {
      return new MatTableDataSource<TableDocument>([]);
    }

    if (!requiredDocumentTypes) {
      return new MatTableDataSource<TableDocument>([]);
    }

    const uploadedDocuments: TableDocument[] = documents.map((document) => {
      const formStep = getFormStep(document.dokumentTyp);

      return {
        ...document,
        formStep,
        titleKey: document.dokumentTyp
          ? DOKUMENT_TYP_TO_DOCUMENT_OPTIONS[document.dokumentTyp]
          : undefined,
      };
    });

    const missingDocuments: TableDocument[] = requiredDocumentTypes.map(
      (documentTyp) => {
        const formStep = getFormStep(documentTyp);

        return {
          documentTyp,
          formStep,
          titleKey: DOKUMENT_TYP_TO_DOCUMENT_OPTIONS[documentTyp],
        };
      },
    );

    return new MatTableDataSource<TableDocument>([
      ...uploadedDocuments,
      ...missingDocuments,
    ]);
  });

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
