import {
  DestroyRef,
  Directive,
  OnInit,
  inject,
  input,
  output,
} from '@angular/core';
import { takeUntilDestroyed, toObservable } from '@angular/core/rxjs-interop';
import { FormControl, FormGroup } from '@angular/forms';
import { MatAutocomplete } from '@angular/material/autocomplete';
import { Subject, combineLatest, filter, map } from 'rxjs';

import { Plz } from '@dv/shared/model/gesuch';
import { isSuccess } from '@dv/shared/util/remote-data';
import { PlzOrtLookupService } from '@dv/shared/util-data-access/plz-ort-lookup';

@Directive({
  selector: '[dvPlzOrtAutocomplete]',
  standalone: true,
})
export class SharedUiPlzOrtAutocompleteDirective implements OnInit {
  private plzOrtLookupService = inject(PlzOrtLookupService);
  private destroyRef = inject(DestroyRef);
  private plzLookup$ = toObservable(
    this.plzOrtLookupService.getCachedPlzLookup(),
  ).pipe(filter(({ plz }) => isSuccess(plz)));

  autocompleteSig = input.required<MatAutocomplete>();
  plzFormSig = input.required<
    FormGroup<{
      plz: FormControl<string | undefined>;
      ort: FormControl<string | undefined>;
    }>
  >();
  onTouched$ = new Subject<void>();
  plzLookupValuesSig = output<Plz[]>();

  ngOnInit() {
    // Subscribe to the optionSelected event of the autocomplete input and set the postal code and city values
    this.autocompleteSig()
      ?.optionSelected.pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((event) => this.setValues(event.option.value));

    // Combine the postal code form control value changes with the postal code lookup data
    combineLatest([
      this.plzFormSig().controls.plz.valueChanges.pipe(
        map((value) => {
          if (value && value.length > 1) {
            return value;
          }
          return undefined;
        }),
      ),
      this.plzLookup$,
    ])
      .pipe(
        // Map the postal code value to a list of matching postal codes
        map(([plz, plzLookup]) =>
          !plz
            ? []
            : (plzLookup.list?.fuzzyPlz
                .search(plz.toString(), { limit: 15 })
                ?.map((r) => r.item) ?? []),
        ),
        takeUntilDestroyed(this.destroyRef),
      )
      .subscribe((values) => {
        // Emit the matching postal code values
        this.plzLookupValuesSig.emit(values);
      });
  }

  // Set the postal code and city values based on the selected option
  private setValues(option: Plz) {
    this.plzFormSig().controls.plz.setValue(option.plz.toString());
    this.plzFormSig().controls.ort.setValue(option.ort);
  }
}
