import { Pipe, PipeTransform, inject } from '@angular/core';
import { toObservable } from '@angular/core/rxjs-interop';
// eslint-disable-next-line @nx/enforce-module-boundaries
import { Store } from '@ngrx/store';
import { Observable, combineLatestWith, map } from 'rxjs';

// eslint-disable-next-line @nx/enforce-module-boundaries
import { selectLanguage } from '@dv/shared/data-access/language';
import { LandLookupService } from '@dv/shared/util-data-access/land-lookup';

@Pipe({
  name: 'translatedLand',
})
export class SharedUiTranslatedLandPipe implements PipeTransform {
  private store = inject(Store);
  private landLookupService = inject(LandLookupService);
  private language$ = this.store.select(selectLanguage);
  private laender$ = toObservable(this.landLookupService.getCachedLandLookup());

  transform(value: string | undefined): Observable<string> {
    return this.laender$.pipe(
      combineLatestWith(this.language$),
      map(([laender, language]) => {
        const land = laender?.find((l) => l.id === value);
        if (land) {
          return land[`${language}Kurzform`] || land['deKurzform']; // Fallback to German if no translation exists
        } else {
          return '';
        }
      }),
    );
  }
}
