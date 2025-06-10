import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DoCheck,
  Input,
  input,
  signal,
} from '@angular/core';
import {
  FormControl,
  FormGroup,
  FormsModule,
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { TranslatePipe } from '@ngx-translate/core';

import { Adresse, Plz } from '@dv/shared/model/gesuch';
import { Language } from '@dv/shared/model/language';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiZuvorHintDirective,
} from '@dv/shared/ui/form';
import { SharedUiLandAutocompleteComponent } from '@dv/shared/ui/land-autocomplete';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import { SharedUiPlzOrtAutocompleteDirective } from '@dv/shared/ui/plz-ort-autocomplete';
import { convertTempFormToRealValues } from '@dv/shared/util/form';

type AddresseFormGroup = FormGroup<{
  coAdresse: FormControl<string | undefined>;
  strasse: FormControl<string | undefined>;
  hausnummer: FormControl<string | undefined>;
  plzOrt: FormGroup<{
    plz: FormControl<string | undefined>;
    ort: FormControl<string | undefined>;
  }>;
  landId: FormControl<string | undefined>;
}>;

@Component({
  selector: 'dv-shared-ui-form-address',
  imports: [
    CommonModule,
    TranslatePipe,
    FormsModule,
    ReactiveFormsModule,
    MatAutocompleteModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiPlzOrtAutocompleteDirective,
    SharedUiZuvorHintDirective,
    SharedUiMaxLengthDirective,
    SharedUiLandAutocompleteComponent,
  ],
  templateUrl: './shared-ui-form-address.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiFormAddressComponent implements DoCheck {
  @Input({ required: true }) group!: AddresseFormGroup;
  languageSig = input.required<Language>();
  @Input() changes?: Partial<Adresse>;
  plzValues?: Plz[];

  touchedSig = signal(false);

  static buildAddressFormGroup(fb: NonNullableFormBuilder): AddresseFormGroup {
    return fb.group({
      coAdresse: [<string | undefined>undefined, []],
      strasse: [<string | undefined>undefined, [Validators.required]],
      hausnummer: [<string | undefined>undefined, []],
      plzOrt: fb.group({
        plz: [<string | undefined>undefined, [Validators.required]],
        ort: [<string | undefined>undefined, [Validators.required]],
      }),
      landId: [
        <string | undefined>undefined,
        {
          validators: Validators.required,
        },
      ],
    });
  }

  static getRealValues(form: AddresseFormGroup) {
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    const { plzOrt: _, ...values } = convertTempFormToRealValues(form, [
      'strasse',
      'landId',
    ]);
    const plzOrt = convertTempFormToRealValues(form.controls.plzOrt, [
      'plz',
      'ort',
    ]);
    return {
      ...values,
      plz: plzOrt.plz,
      ort: plzOrt.ort,
    };
  }

  static patchForm(
    form: AddresseFormGroup,
    values: {
      coAdresse?: string;
      strasse?: string;
      hausnummer?: string;
      plz?: string;
      ort?: string;
      landId?: string;
    },
  ) {
    form.patchValue({
      coAdresse: values.coAdresse,
      strasse: values.strasse,
      hausnummer: values.hausnummer,
      plzOrt: {
        plz: values.plz,
        ort: values.ort,
      },
      landId: values.landId,
    });
  }

  ngDoCheck(): void {
    if (!this.group) {
      return;
    }

    if (this.group.untouched) {
      this.touchedSig.set(false);
    }

    if (this.group.touched) {
      this.touchedSig.set(true);
    }
  }
}
