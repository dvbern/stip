import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  computed,
  inject,
} from '@angular/core';
import {
  FormControl,
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { TranslateModule } from '@ngx-translate/core';

import { AusbildungUpdate, AusbildungsPensum } from '@dv/shared/model/gesuch';
import { convertTempFormToRealValues } from '@dv/shared/util/form';
import { onMonthYearInputBlur } from '@dv/shared/util/validator-date';
import { AusbildungService } from '@dv/shared/util-data-access/ausbildung';

@Component({
  selector: 'dv-shared-ui-ausbildung',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TranslateModule,
    MatFormFieldModule,
    MatAutocompleteModule,
    MatSelectModule,
  ],
  templateUrl: './shared-ui-ausbildung.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiAusbildungComponent {
  private formBuilder = inject(NonNullableFormBuilder);
  private ausbildungService = inject(AusbildungService);

  save = new EventEmitter<AusbildungUpdate>();

  form = this.formBuilder.group({
    ausbildungsort: [<string | undefined>undefined, [Validators.required]],
    isAusbildungAusland: [false, []],
    ausbildungsstaette: [<string | undefined>undefined, [Validators.required]],
    ausbildungsgang: [<string | undefined>undefined, [Validators.required]],
    fachrichtung: [<string | null>null, [Validators.required]],
    ausbildungNichtGefunden: [false, []],
    alternativeAusbildungsgang: [<string | undefined>undefined],
    alternativeAusbildungsstaette: [<string | undefined>undefined],
    ausbildungBegin: ['', []],
    ausbildungEnd: ['', []],
    pensum: this.formBuilder.control<AusbildungsPensum | null>(null, {
      validators: Validators.required,
    }),
  });
  languageSig = this.ausbildungService.getLanguagesSig();
  ausbildungsgangOptionsSig = computed(() => {
    return [] as any[];
    // return this.ausbildungService.getAusbildungsgangOptionsSig();
  });

  handleGangChangedByUser() {
    this.form.controls.fachrichtung.reset();
    this.form.controls.ausbildungsort.reset();
  }

  handleManuellChangedByUser() {
    this.form.controls.ausbildungsstaette.reset();
    this.form.controls.alternativeAusbildungsstaette.reset();
    this.form.controls.ausbildungsgang.reset();
    this.form.controls.alternativeAusbildungsgang.reset();
    this.form.controls.fachrichtung.reset();
    this.form.controls.ausbildungsort.reset();
  }

  onDateBlur(ctrl: FormControl) {
    return onMonthYearInputBlur(ctrl, new Date(), this.languageSig());
  }

  handleSave() {
    this.form.markAllAsTouched();

    if (this.form.invalid) {
      return;
    }

    const formValues = convertTempFormToRealValues(this.form, [
      'fachrichtung',
      'pensum',
    ]);

    this.save.emit(formValues);
  }
}
