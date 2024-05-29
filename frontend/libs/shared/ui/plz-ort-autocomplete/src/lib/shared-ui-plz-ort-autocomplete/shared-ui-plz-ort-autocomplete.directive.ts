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

import { PlzOrtLookup } from '@dv/shared/model/plz-ort-lookup';
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
  plzLookupValuesSig = output<PlzOrtLookup[]>();

  ngOnInit() {
    this.autocompleteSig()
      ?.optionSelected.pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((event) => this.setValues(event.option.value));

    combineLatest([
      this.plzFormSig().controls.plz.valueChanges,
      this.plzLookup$,
    ])
      .pipe(
        map(([plz, plzLookup]) =>
          !plz
            ? []
            : plzLookup.list?.fuzzyPlz
                .search(plz.toString())
                .map((r) => r.item) ?? [],
        ),
        takeUntilDestroyed(this.destroyRef),
      )
      .subscribe((values) => {
        this.plzLookupValuesSig.emit(values);
      });
  }

  private setValues(option: PlzOrtLookup) {
    this.plzFormSig().controls.plz.setValue(option.plz.toString());
    this.plzFormSig().controls.ort.setValue(option.ort);
  }
}
