import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DoCheck,
  Input,
  OnChanges,
  SimpleChanges,
  inject,
  signal,
} from '@angular/core';
import {
  FormControl,
  FormGroup,
  FormsModule,
  NonNullableFormBuilder,
  ReactiveFormsModule,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { TranslatePipe } from '@ngx-translate/core';
import { BehaviorSubject } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';

import { Adresse, Land, Plz } from '@dv/shared/model/gesuch';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiZuvorHintDirective,
} from '@dv/shared/ui/form';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import { SharedUiPlzOrtAutocompleteDirective } from '@dv/shared/ui/plz-ort-autocomplete';
import { SharedUiTranslateChangePipe } from '@dv/shared/ui/translate-change';
import { SharedUtilCountriesService } from '@dv/shared/util/countries';
import { convertTempFormToRealValues } from '@dv/shared/util/form';

type AddresseFormGroup = FormGroup<{
  coAdresse: FormControl<string | undefined>;
  strasse: FormControl<string | undefined>;
  hausnummer: FormControl<string | undefined>;
  plzOrt: FormGroup<{
    plz: FormControl<string | undefined>;
    ort: FormControl<string | undefined>;
  }>;
  land: FormControl<Land | undefined>;
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
    SharedUiTranslateChangePipe,
    SharedUiMaxLengthDirective,
  ],
  templateUrl: './shared-ui-form-address.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiFormAddressComponent implements DoCheck, OnChanges {
  @Input({ required: true }) group!: AddresseFormGroup;
  @Input({ required: true }) laender!: Land[];
  @Input({ required: true }) language!: string;
  @Input() changes?: Partial<Adresse>;

  private countriesService = inject(SharedUtilCountriesService);
  private laender$ = new BehaviorSubject<Land[]>([]);

  translatedLaender$ = this.laender$.pipe(
    switchMap((laender) => this.countriesService.getCountryList(laender)),
    map((translatedLaender) =>
      translatedLaender.filter(
        (translatedLand) => translatedLand.code !== 'STATELESS',
      ),
    ),
  );
  plzValues?: Plz[];

  touchedSig = signal(false);

  static buildAddressFormGroup(
    fb: NonNullableFormBuilder,
    validators?: ValidatorFn | ValidatorFn[],
  ): AddresseFormGroup {
    return fb.group(
      {
        coAdresse: [<string | undefined>undefined, []],
        strasse: [<string | undefined>undefined, [Validators.required]],
        hausnummer: [<string | undefined>undefined, []],
        plzOrt: fb.group({
          plz: [<string | undefined>undefined, [Validators.required]],
          ort: [<string | undefined>undefined, [Validators.required]],
        }),
        land: [
          <Land | undefined>undefined,
          {
            validators: Validators.required,
          },
        ],
      },
      {
        validators: validators ?? null,
      },
    );
  }

  static getRealValues(form: AddresseFormGroup) {
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    const { plzOrt: _, ...values } = convertTempFormToRealValues(form, [
      'strasse',
      'land',
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
      land?: Land;
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
      land: values.land,
    });
  }

  trackByIndex(index: number) {
    return index;
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

  ngOnChanges(changes: SimpleChanges) {
    if (changes['laender']?.currentValue) {
      this.laender$.next(changes['laender'].currentValue);
    }
  }
}
