/* eslint-disable @angular-eslint/no-input-rename */
import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  computed,
  effect,
  inject,
  input,
} from '@angular/core';
import {
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatDialog } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';

import { SharedTranslationKey } from '@dv/shared/assets/i18n';
import { AusbildungStore } from '@dv/shared/data-access/ausbildung';
import { selectSharedDataAccessConfigsView } from '@dv/shared/data-access/config';
import { GlobalNotificationStore } from '@dv/shared/global/notification';
import { DokumentOptions } from '@dv/shared/model/dokument';
import { SharedPatternDocumentUploadComponent } from '@dv/shared/pattern/document-upload';
import { SharedPatternMainLayoutComponent } from '@dv/shared/pattern/main-layout';
import { SharedUiAdvTranslocoDirective } from '@dv/shared/ui/adv-transloco-directive';
import { SharedUiConfirmDialogComponent } from '@dv/shared/ui/confirm-dialog';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { SharedUiInfoContainerComponent } from '@dv/shared/ui/info-container';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import { provideDvDateAdapter } from '@dv/shared/util/date-adapter';
import {
  SharedUtilFormService,
  convertTempFormToRealValues,
} from '@dv/shared/util/form';

@Component({
  selector: 'dv-shared-feature-ausbildung-unterbrechung',
  imports: [
    ReactiveFormsModule,
    MatInputModule,
    MatDatepickerModule,
    SharedUiMaxLengthDirective,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedPatternMainLayoutComponent,
    SharedUiAdvTranslocoDirective,
    SharedUiInfoContainerComponent,
    SharedPatternDocumentUploadComponent,
    SharedUiLoadingComponent,
  ],
  templateUrl: './shared-feature-ausbildung-unterbrechung.component.html',
  providers: [provideDvDateAdapter()],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureAusbildungUnterbrechungComponent {
  private store = inject(Store);
  private router = inject(Router);
  private elementRef = inject(ElementRef);
  private ausbildungStore = inject(AusbildungStore);
  private dialog = inject(MatDialog);
  private globalNotificationStore = inject(GlobalNotificationStore);
  private formBuilder = inject(NonNullableFormBuilder);

  private formUtils = inject(SharedUtilFormService);
  private config = this.store.selectSignal(selectSharedDataAccessConfigsView);

  fallIdSig = input<string | undefined>(undefined, { alias: 'fallId' });
  ausbildungUnterbruchIdSig = input<string | undefined>(undefined, {
    alias: 'ausbildungUnterbruchId',
  });
  isMissingDokumenteSig = computed(
    () => !this.ausbildungStore.ausbildungUnterbruchDokumente().data?.length,
  );
  form = this.formBuilder.group({
    startDate: [<string | undefined>undefined, Validators.required],
    endDate: [<string | undefined>undefined, Validators.required],
    kommentarGS: [<string | undefined>undefined, Validators.required],
  });
  unterbruchDokumenteOptionsSig = computed(() => {
    const allowTypes =
      this.config().deploymentConfig?.allowedMimeTypes?.join(',');
    const ausbildungUnterbruchId = this.ausbildungUnterbruchIdSig();
    const initialDokumente =
      this.ausbildungStore.ausbildungUnterbruchDokumente().data;

    if (!allowTypes || !ausbildungUnterbruchId || !initialDokumente) {
      return null;
    }
    return {
      allowTypes,
      dokument: {
        art: 'SIMPLE',
        dokumentTyp: 'ausbildungUnterbruch',
        id: ausbildungUnterbruchId,
      },
      info: {
        type: 'TRANSLATABLE',
        title: 'shared.ausbildung-unterbrechen.dokumente.title',
        description: 'shared.ausbildung-unterbrechen.dokumente.description',
      },
      initialDokumente,
    } satisfies DokumentOptions;
  });

  constructor() {
    effect(() => {
      const ausbildungUnterbruchAntragId = this.ausbildungUnterbruchIdSig();
      if (!ausbildungUnterbruchAntragId) {
        return;
      }
      this.ausbildungStore.getAusbildungUnterbruchDokumente$({
        ausbildungUnterbruchAntragId,
      });
    });
  }

  deleteUnterbruch() {
    const ausbildungUnterbruchAntragId = this.ausbildungUnterbruchIdSig();
    if (!ausbildungUnterbruchAntragId) {
      return;
    }
    SharedUiConfirmDialogComponent.open<SharedTranslationKey>(this.dialog, {
      title: 'shared.ausbildung-unterbrechen.delete.dialog.title',
      message: 'shared.ausbildung-unterbrechen.delete.dialog.message',
      cancelText: 'shared.cancel',
      confirmText: 'shared.ausbildung-unterbrechen.delete',
    })
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          this.ausbildungStore.deleteAusbildungUnterbruchAntrag$({
            ausbildungUnterbruchAntragId,
            onSuccess: () => {
              this.globalNotificationStore.createSuccessNotification<SharedTranslationKey>(
                {
                  messageKey: 'shared.ausbildung-unterbrechen.delete.success',
                },
              );
              this.navigateBack();
            },
          });
        }
      });
  }

  unterbruchEinreichen() {
    const isMissingDokumente = this.isMissingDokumenteSig();
    const ausbildungUnterbruchAntragId = this.ausbildungUnterbruchIdSig();
    this.form.markAllAsTouched();
    this.formUtils.focusFirstInvalid(this.elementRef);
    if (
      !ausbildungUnterbruchAntragId ||
      this.form.invalid ||
      isMissingDokumente
    ) {
      return;
    }
    const values = convertTempFormToRealValues(this.form);
    this.ausbildungStore.einreichenAusbildungUnterbruchAntrag$({
      ausbildungUnterbruchAntragId,
      updateAusbildungUnterbruchAntragGS: values,
      onSuccess: () => {
        this.globalNotificationStore.createSuccessNotification<SharedTranslationKey>(
          {
            messageKey: 'shared.ausbildung-unterbrechen.create.success',
          },
        );
        this.navigateBack();
      },
    });
  }

  private navigateBack() {
    const fallId = this.fallIdSig();
    this.router.navigate(fallId ? ['/fall', fallId] : ['/']);
  }
}
