/* eslint-disable @angular-eslint/no-input-rename */
import { DatePipe } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  computed,
  effect,
  inject,
  input,
  signal,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import {
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatDialog } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Store } from '@ngrx/store';
import { addDays } from 'date-fns';

import { SharedTranslationKey } from '@dv/shared/assets/i18n';
import { AusbildungStore } from '@dv/shared/data-access/ausbildung';
import { selectSharedDataAccessConfigsView } from '@dv/shared/data-access/config';
import { GlobalNotificationStore } from '@dv/shared/global/notification';
import { SharedUiAdvTranslocoDirective } from '@dv/shared/ui/adv-transloco-directive';
import { SharedUiFileUploadComponent } from '@dv/shared/ui/file-upload';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { SharedUiInfoContainerComponent } from '@dv/shared/ui/info-container';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import { provideDvDateAdapter } from '@dv/shared/util/date-adapter';
import {
  SharedUtilFormService,
  convertTempFormToRealValues,
} from '@dv/shared/util/form';
import { getQueryParamValueSig } from '@dv/shared/util/navigation';
import { toBackendLocalDate } from '@dv/shared/util/validator-date';

@Component({
  selector: 'dv-shared-feature-ausbildung-unterbrechung',
  imports: [
    DatePipe,
    RouterLink,
    ReactiveFormsModule,
    MatInputModule,
    MatDatepickerModule,
    SharedUiMaxLengthDirective,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiAdvTranslocoDirective,
    SharedUiInfoContainerComponent,
    SharedUiFileUploadComponent,
  ],
  templateUrl: './shared-feature-ausbildung-unterbrechung.component.html',
  providers: [provideDvDateAdapter()],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureAusbildungUnterbrechungComponent {
  private store = inject(Store);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private elementRef = inject(ElementRef);
  private ausbildungStore = inject(AusbildungStore);
  private dialog = inject(MatDialog);
  private globalNotificationStore = inject(GlobalNotificationStore);
  private formBuilder = inject(NonNullableFormBuilder);

  private formUtils = inject(SharedUtilFormService);
  deploymentConfigSig = this.store.selectSignal(
    selectSharedDataAccessConfigsView,
  );

  ausbildungIdSig = input<string | undefined>(undefined, {
    alias: 'ausbildungId',
  });
  limitsSig = this.ausbildungStore.ausbildungUnterbruchLimits;
  previousPageSig = getQueryParamValueSig(this.route, 'previousPage');
  form = this.formBuilder.group({
    startDate: [<string | undefined>undefined, Validators.required],
    endDate: [<string | undefined>undefined, Validators.required],
    kommentarGS: [<string | undefined>undefined, Validators.required],
    fileUploads: [<File[] | undefined>undefined, Validators.required],
  });
  selectedFileSig = signal<File | undefined>(undefined);
  private startDateChangedSig = toSignal(
    this.form.controls.startDate.valueChanges,
    {
      initialValue: this.form.controls.startDate.value,
    },
  );
  oneDayAfterStartDateSig = computed(() => {
    const gueltigAb = this.startDateChangedSig();
    if (!gueltigAb) {
      return null;
    }
    return addDays(gueltigAb, 1);
  });

  constructor() {
    this.formUtils.registerFormForUnsavedCheck(this);
    effect(() => {
      const ausbildungId = this.ausbildungIdSig();
      if (!ausbildungId) {
        return;
      }
      this.ausbildungStore.getAusbildungUnterbruchLimits$({
        ausbildungId,
      });
    });
  }

  unterbruchEinreichen() {
    this.form.markAllAsTouched();
    this.formUtils.focusFirstInvalid(this.elementRef);
    const ausbildungId = this.ausbildungIdSig();
    const fileUpload = this.selectedFileSig();
    if (this.form.invalid || !ausbildungId || !fileUpload) {
      return;
    }
    const values = convertTempFormToRealValues(this.form);
    this.ausbildungStore.createAusbildungUnterbruchAntragGs$({
      ...values,
      fileUpload,
      ausbildungId,
      startDate: toBackendLocalDate(values.startDate),
      endDate: toBackendLocalDate(values.endDate),
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
    const previousPage = this.previousPageSig();
    this.form.markAsPristine();
    this.router.navigate(previousPage ? [previousPage] : ['/']);
  }
}
