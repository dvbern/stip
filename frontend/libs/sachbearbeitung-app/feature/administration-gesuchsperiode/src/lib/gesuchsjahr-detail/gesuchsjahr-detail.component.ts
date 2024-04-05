import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  effect,
  inject,
  input,
} from '@angular/core';
import {
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import {
  MatDatepicker,
  MatDatepickerApply,
  MatDatepickerInput,
  MatDatepickerToggle,
} from '@angular/material/datepicker';
import {
  MatError,
  MatFormFieldModule,
  MatHint,
} from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { MaskitoModule } from '@maskito/angular';
import { TranslateModule } from '@ngx-translate/core';

import { GesuchsperiodeStore } from '@dv/sachbearbeitung-app/data-access/gesuchsperiode';
import { GueltigkeitStatus } from '@dv/shared/model/gesuch';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { GesuchAppUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';
import {
  SharedUtilFormService,
  convertTempFormToRealValues,
} from '@dv/shared/util/form';

@Component({
  standalone: true,
  imports: [
    CommonModule,
    MaskitoModule,
    MatError,
    MatFormFieldModule,
    MatHint,
    MatInput,
    TranslateModule,
    ReactiveFormsModule,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    GesuchAppUiStepFormButtonsComponent,
    MatDatepicker,
    MatDatepickerToggle,
    MatDatepickerInput,
    MatDatepickerApply,
  ],
  templateUrl: './gesuchsjahr-detail.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GesuchsjahrDetailComponent {
  private formBuilder = inject(NonNullableFormBuilder);
  private formUtils = inject(SharedUtilFormService);
  private elementRef = inject(ElementRef);

  store = inject(GesuchsperiodeStore);
  id = input<string | undefined>();
  form = this.formBuilder.group({
    bezeichnungDe: [<string | null>null, [Validators.required]],
    bezeichnungFr: [<string | null>null, [Validators.required]],
    technischesJahr: [<string | null>null, [Validators.required]],
    gueltigkeitStatus: [<GueltigkeitStatus>'ENTWURF'],
  });

  constructor() {
    effect(() => {
      const gesuchsjahr = this.store.currentGesuchsJahr().data;
      this.form.patchValue({
        ...gesuchsjahr,
        technischesJahr: gesuchsjahr?.technischesJahr.toString(),
      });
    });
    effect(() => {
      const id = this.id();

      if (id) {
        this.store.loadGesuchsjahr$(id);
      }
    });
  }

  handleSave() {
    this.form.markAllAsTouched();
    this.formUtils.focusFirstInvalid(this.elementRef);
    if (!this.form.valid) {
      return;
    }
    const value = convertTempFormToRealValues(this.form, 'all');
    this.store.saveGesuchsjahr$({
      gesuchsjahrId: this.id(),
      gesuchsjahrDaten: { ...value, technischesJahr: +value.technischesJahr },
    });
  }
}
