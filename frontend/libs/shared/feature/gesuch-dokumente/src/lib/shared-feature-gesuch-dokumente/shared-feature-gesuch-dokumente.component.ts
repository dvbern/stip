import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  computed,
  inject,
} from '@angular/core';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { RouterModule } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';

import { selectSharedDataAccessDokumentesView } from '@dv/shared/data-access/dokumente';
import { SharedEventGesuchDokumente } from '@dv/shared/event/gesuch-dokumente';
import {
  DOKUMENTE,
  KINDER,
  SharedModelGesuchFormStep,
  gesuchFormSteps,
} from '@dv/shared/model/gesuch-form';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { GesuchAppUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';

type RequiredDocument = {
  documentName: string;
  formStep: SharedModelGesuchFormStep;
  objectId: string;
};

type DocumentTableSource = {
  id: string;
  status: boolean;
  objectId: string;
  formStep: SharedModelGesuchFormStep;
  filename: string;
  documentName: string;
};

const requiredDocuments: RequiredDocument[] = Object.values(gesuchFormSteps)
  .filter(
    (formStep) => formStep.currentStepNumber !== DOKUMENTE.currentStepNumber,
  )
  .map((formStep) => ({
    documentName: `Dokument-${formStep.route}`,
    formStep,
    objectId: `${formStep.route}.${formStep.currentStepNumber}`,
  }));

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
  ],
  templateUrl: './shared-feature-gesuch-dokumente.component.html',
  styleUrl: './shared-feature-gesuch-dokumente.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchDokumenteComponent implements OnInit {
  private store = inject(Store);

  displayedColumns = [
    'status',
    'documentName',
    'formStep',
    'filename',
    'actions',
  ];

  viewSig = this.store.selectSignal(selectSharedDataAccessDokumentesView);

  dokumenteDataSourceSig = computed(() => {
    const dokumente = this.viewSig().dokumentes;

    const documentTableSource: DocumentTableSource[] = requiredDocuments.map(
      (requiredDocument) => {
        const document = dokumente.find(
          (dokument) => dokument.objectId === requiredDocument.objectId,
        );

        return {
          status: !!document,
          id: document?.id ?? '',
          objectId: requiredDocument.objectId,
          formStep: requiredDocument.formStep,
          filename: document?.filename ?? '',
          documentName: requiredDocument.documentName,
        };
      },
    );

    return new MatTableDataSource<DocumentTableSource>(documentTableSource);
  });

  ngOnInit() {
    this.store.dispatch(SharedEventGesuchDokumente.init());
  }

  uploadDocument(objectId: string) {
    alert(`Upload document for ${objectId}`);
  }

  downloadDocument(id: string) {
    alert(`Download document ${id}`);
  }

  handleDocumentDelete(id: string) {
    alert(`Delete document ${id}`);
  }

  handleContinue() {
    const { gesuchId } = this.viewSig();
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
