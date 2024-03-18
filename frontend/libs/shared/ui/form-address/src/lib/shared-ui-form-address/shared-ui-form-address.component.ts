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
  Validators,
} from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { TranslateModule } from '@ngx-translate/core';
import { BehaviorSubject } from 'rxjs';
import { switchMap } from 'rxjs/operators';

import { Land } from '@dv/shared/model/gesuch';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { SharedUiFormCountryComponent } from '@dv/shared/ui/form-country';
import { SharedUtilCountriesService } from '@dv/shared/util/countries';
import { convertTempFormToRealValues } from '@dv/shared/util/form';

type AddresseFormGroup = FormGroup<{
  coAdresse: FormControl<string | undefined>;
  strasse: FormControl<string | undefined>;
  hausnummer: FormControl<string | undefined>;
  plz: FormControl<string | undefined>;
  ort: FormControl<string | undefined>;
  land: FormControl<Land | undefined>;
}>;

@Component({
  selector: 'dv-shared-ui-form-address',
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    SharedUiFormFieldDirective,
    SharedUiFormCountryComponent,
    SharedUiFormMessageErrorDirective,
  ],
  templateUrl: './shared-ui-form-address.component.html',
  styleUrls: ['./shared-ui-form-address.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiFormAddressComponent implements DoCheck, OnChanges {
  @Input({ required: true }) group!: AddresseFormGroup;
  @Input({ required: true }) laender!: Land[];
  @Input({ required: true }) language!: string;

  private countriesService = inject(SharedUtilCountriesService);
  private laender$ = new BehaviorSubject<Land[]>([]);

  translatedLaender$ = this.laender$.pipe(
    switchMap((laender) => this.countriesService.getCountryList(laender)),
  );

  touchedSig = signal(false);

  static buildAddressFormGroup(fb: NonNullableFormBuilder): AddresseFormGroup {
    return fb.group({
      coAdresse: [<string | undefined>undefined, []],
      strasse: [<string | undefined>undefined, [Validators.required]],
      hausnummer: [<string | undefined>undefined, []],
      plz: [<string | undefined>undefined, [Validators.required]],
      ort: [<string | undefined>undefined, [Validators.required]],
      land: [
        <Land | undefined>undefined,
        {
          validators: Validators.required,
        },
      ],
    });
  }

  static getRealValues(form: AddresseFormGroup) {
    return convertTempFormToRealValues(form, ['strasse', 'plz', 'ort', 'land']);
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
