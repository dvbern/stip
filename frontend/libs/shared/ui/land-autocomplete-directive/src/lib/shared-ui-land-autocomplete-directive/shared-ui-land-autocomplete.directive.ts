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
import { Subject, combineLatest, filter, map, tap } from 'rxjs';

import { Land } from '@dv/shared/model/gesuch';
import { isDefined } from '@dv/shared/model/type-util';
import { isSuccess } from '@dv/shared/util/remote-data';
import { LandLookupService } from '@dv/shared/util-data-access/land-lookup';

@Directive({
  selector: '[dvLandAutocomplete]',
  standalone: true,
})
export class SharedUiLandAutocompleteDirective implements OnInit {
  private destroyRef = inject(DestroyRef);
  private landLookupService = inject(LandLookupService);
  private landLookup$ = toObservable(
    this.landLookupService.getCachedLandLookup(),
  );

  autocompleteSig = input.required<MatAutocomplete>();
  landControlSig = input.required<FormControl<string | Land | undefined>>();
  onTouched$ = new Subject<void>();
  landLookupValuesSig = output<Land[] | undefined>();

  ngOnInit() {
    // this.autocompleteSig()
    //   ?.optionSelected.pipe(takeUntilDestroyed(this.destroyRef))
    //   .subscribe((event) => {
    //     // Set the value of the land control to the selected land's ID
    //     this.landControlSig().setValue(event.option.value.id);
    //   });

    combineLatest([
      this.landControlSig().valueChanges.pipe(
        filter((value) => isDefined(value)),
        tap((x) => console.log('Land control value changed:', x)),
        map((value) => trimLandValue(value)),
      ),
      this.landLookup$.pipe(
        map((laender) => laender?.filter((land) => land.eintragGueltig)),
      ),
    ])
      .pipe(
        map(([filterStr, laender]) => {
          return laender?.filter((land) =>
            land.deKurzform.toLowerCase().includes(filterStr.toLowerCase()),
          );
        }),
        takeUntilDestroyed(this.destroyRef),
      )
      .subscribe((values) => {
        this.landLookupValuesSig.emit(values);
      });
  }
}

const trimLandValue = (value: string | Land) => {
  if (typeof value === 'string') {
    return value.trim();
  }
  return value.deKurzform;
};
