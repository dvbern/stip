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
  createDocumentOptions,
} from '@dv/shared/pattern/document-upload';
import { SharedUiBadgeComponent } from '@dv/shared/ui/badge';
import { SharedUiIconBadgeComponent } from '@dv/shared/ui/icon-badge';
import {
  SharedUiIfGesuchstellerDirective,
  SharedUiIfSachbearbeiterDirective,
} from '@dv/shared/ui/if-app-type';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';
import { getLatestGesuchIdFromGesuch$ } from '@dv/shared/util/gesuch';

function getFormStep(
  dokumentTyp: DokumentTyp | undefined,
): SharedModelGesuchFormStep {
  const unknownStep: SharedModelGesuchFormStep = {
    route: 'unknown',
    translationKey: 'unknown',
    titleTranslationKey: 'unknown',
    currentStepNumber: 0,
    iconSymbolName: 'unknown',
  };

  if (!dokumentTyp) {
    return unknownStep;
  }

  switch (dokumentTyp) {
    case DokumentTyp.KINDER_UNTERHALTSVERTRAG_TRENNUNGSKONVENTION: {
      return gesuchFormSteps.DOKUMENTE;
    }
    case DokumentTyp.EK_BELEG_BETREUUNGSKOSTEN_KINDER:
    case DokumentTyp.EK_BELEG_KINDERZULAGEN: {
      return gesuchFormSteps.EINNAHMEN_KOSTEN;
    }
    case DokumentTyp.GESCHWISTER_BESTAETIGUNG_AUSBILDUNGSSTAETTE: {
      return gesuchFormSteps.GESCHWISTER;
    }
    case DokumentTyp.PARTNER_AUSBILDUNG_LOHNABRECHNUNG:
    case DokumentTyp.PARTNER_BELEG_OV_ABONNEMENT: {
      return gesuchFormSteps.PARTNER;
    }
    default: {
      const step = Object.keys(gesuchFormSteps).find((key) => {
        if (key === 'EINNAHMEN_KOSTEN') {
          return dokumentTyp.includes('EK');
        }
        return dokumentTyp.includes(key);
      }) as keyof typeof gesuchFormSteps;

      return step ? gesuchFormSteps[step] : unknownStep;
    }
  }
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
    SharedUiStepFormButtonsComponent,
    SharedPatternDocumentUploadComponent,
    SharedUiBadgeComponent,
    SharedUiIfSachbearbeiterDirective,
    SharedUiIfGesuchstellerDirective,
    SharedUiIconBadgeComponent,
  ],
  templateUrl: './shared-feature-gesuch-dokumente.component.html',
  styleUrl: './shared-feature-gesuch-dokumente.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchDokumenteComponent {
  private store = inject(Store);

  displayedColumns = [
    'expander',
    'documentName',
    'formStep',
    'status',
    'actions',
  ];

  dokumenteSig = this.store.selectSignal(selectSharedDataAccessDokumentesView);
  gesuchViewSig = this.store.selectSignal(selectSharedDataAccessGesuchsView);

  dokumenteDataSourceSig = computed(() => {
    const documents = this.dokumenteSig().dokumentes;
    const requiredDocumentTypes = this.dokumenteSig().requiredDocumentTypes;
    const gesuchId = this.gesuchViewSig().gesuchId;
    const readonly = this.gesuchViewSig().readonly;
    const allowTypes = this.gesuchViewSig().allowTypes;

    if (!gesuchId || !allowTypes) {
      return new MatTableDataSource<TableDocument>([]);
    }

    const uploadedDocuments: TableDocument[] = documents.map((document) => {
      const dokumentTyp = document.dokumentTyp;

      if (!dokumentTyp) {
        throw new Error('Document type is missing');
      }

      const documentOptions = createDocumentOptions({
        gesuchId,
        allowTypes,
        dokumentTyp,
        initialDocuments: document.dokumente,
        readonly,
      });

      const formStep = getFormStep(dokumentTyp);

      return {
        ...document,
        dokumentTyp,
        formStep,
        titleKey: DOKUMENT_TYP_TO_DOCUMENT_OPTIONS[dokumentTyp],
        documentOptions,
      };
    });

    const missingDocuments: TableDocument[] = requiredDocumentTypes.map(
      (dokumentTyp) => {
        const formStep = getFormStep(dokumentTyp);

        const documentOptions = createDocumentOptions({
          gesuchId,
          allowTypes,
          dokumentTyp,
          initialDocuments: [],
          readonly,
        });

        return {
          dokumentTyp,
          formStep,
          titleKey: DOKUMENT_TYP_TO_DOCUMENT_OPTIONS[dokumentTyp],
          documentOptions,
        };
      },
    );

    return new MatTableDataSource<TableDocument>(
      [...uploadedDocuments, ...missingDocuments].sort((a, b) => {
        if (a.formStep.currentStepNumber === b.formStep.currentStepNumber) {
          return a.dokumentTyp.localeCompare(b.dokumentTyp);
        }

        return a.formStep.currentStepNumber - b.formStep.currentStepNumber;
      }),
    );
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

  // TODO: Implement the following methods in 993
  acceptDocument(document: TableDocument) {
    alert(`Accept ${document.dokumentTyp} kommt in 993`);
  }

  // TODO: Implement the following methods in 993
  rejectDocument(document: TableDocument) {
    alert(`reject ${document.dokumentTyp} kommt in 993`);
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
