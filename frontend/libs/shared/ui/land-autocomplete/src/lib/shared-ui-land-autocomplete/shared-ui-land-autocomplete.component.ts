import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
  input,
} from '@angular/core';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import {
  FormBuilder,
  FormControl,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { TranslatePipe } from '@ngx-translate/core';
import { switchMap } from 'rxjs';

import { Land } from '@dv/shared/model/gesuch';
import { Language } from '@dv/shared/model/language';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiZuvorHintDirective,
} from '@dv/shared/ui/form';
import { LandLookupService } from '@dv/shared/util-data-access/land-lookup';

@Component({
  selector: 'dv-shared-ui-land-autocomplete',
  standalone: true,
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
    SharedUiZuvorHintDirective,
  ],
  templateUrl: './shared-ui-land-autocomplete.component.html',
  styleUrl: './shared-ui-land-autocomplete.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiLandAutocompleteComponent {
  private landLookupService = inject(LandLookupService);
  private fb = inject(FormBuilder);
  private laenderSig = this.landLookupService.getCachedLandLookup();

  landIdControlSig = input.required<FormControl<string | undefined>>();
  zuvorHintValueSig = input<string | undefined>(undefined);
  languageSig = input.required<Language>();
  isOpen = false;
  control = this.fb.nonNullable.control<Land | string | undefined>(undefined);

  private controlChangeSig = toSignal(this.control.valueChanges);
  private landIdValueSig = toSignal(
    toObservable(this.landIdControlSig).pipe(
      switchMap((control) => control.valueChanges),
    ),
  );

  private statusChangesSig = toSignal(
    toObservable(this.landIdControlSig).pipe(
      switchMap((control) => control.statusChanges),
    ),
  );

  languageDisplayFieldSig = computed(() => {
    const language = this.languageSig();

    switch (language) {
      case 'de':
        return 'deKurzform';
      case 'fr':
        return 'frKurzform';
      default:
        console.warn(
          `Unsupported language: ${language}. Defaulting to 'deKurzform'.`,
        );
        return 'deKurzform';
    }
  });

  languageDisplayFieldFNSig = computed(() => {
    const languageDisplayField = this.languageDisplayFieldSig();

    return (land: Land | undefined): string => {
      return land ? land[languageDisplayField] : '';
    };
  });

  laenderValuesSig = computed(() => {
    const landInputVal = this.controlChangeSig();
    const laender = this.laenderSig()?.filter((l) => l.eintragGueltig);
    const languageDisplayField = this.languageDisplayFieldSig();

    if (laender) {
      if (typeof landInputVal === 'string') {
        return laender
          .filter((land) =>
            land[languageDisplayField]
              .toLowerCase()
              .includes(landInputVal.toLowerCase()),
          )
          .map((land) => ({
            ...land,
            displayValue: land[languageDisplayField],
          }));
      }
      if (isLand(landInputVal)) {
        this.landIdControlSig().setValue(landInputVal.id);
        return [];
      }
    }

    return [];
  });

  zuvorHintLandNameSig = computed(() => {
    const zuvorHintValue = this.zuvorHintValueSig();
    const languageDisplayField = this.languageDisplayFieldSig();

    if (zuvorHintValue) {
      return this.laenderSig()?.find((land) => land.id === zuvorHintValue)?.[
        languageDisplayField
      ];
    }
    return undefined;
  });

  constructor() {
    effect(() => {
      // either this or the other is working, but not both (situation defined at constructor vs not in soz admin component)
      const landId = this.landIdValueSig();
      const landIdControl = this.landIdControlSig().value;

      if (landId) {
        const land = this.laenderSig()?.find((l) => l.id === landId);
        if (land) {
          this.control.setValue(land);
        } else {
          this.control.setValue(landId);
        }
      } else {
        this.control.setValue(undefined);
      }
    });

    effect(() => {
      const status = this.statusChangesSig();
      if (status === 'DISABLED') {
        this.control.disable({ emitEvent: false });
      } else {
        this.control.enable({ emitEvent: false });
      }
    });

    effect(() => {
      if (this.landIdControlSig().hasValidator(Validators.required)) {
        this.control.addValidators(Validators.required);
        this.control.updateValueAndValidity();
      }
    });
  }

  // needed so moving focus to the overlay does not count as blur and clear the control
  toggleOpen() {
    this.isOpen = !this.isOpen;
  }

  // clear the control if the value is not a valid Land
  onBlur() {
    const value = this.control.value;
    if (!isLand(value) && !this.isOpen) {
      this.control.setValue(undefined);
    }
  }
}

const isLand = (value: Land | string | undefined): value is Land =>
  typeof value === 'object' && value !== null && 'id' in value;
