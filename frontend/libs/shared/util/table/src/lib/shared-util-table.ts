import { Signal, effect } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { AbstractControl, FormGroup } from '@angular/forms';
import { PageEvent } from '@angular/material/paginator';
import { Sort, SortDirection } from '@angular/material/sort';
import { ActivatedRoute, Router } from '@angular/router';
import { debounceTime, merge } from 'rxjs';

import { SortOrder } from '@dv/shared/model/gesuch';
import { SortAndPageInputs } from '@dv/shared/model/table';
import { isDefined } from '@dv/shared/model/type-util';
import { DEFAULT_PAGE_SIZE, INPUT_DELAY } from '@dv/shared/model/ui-constants';

export const restrictNumberParam =
  (restriction: { max: number; min: number }) =>
  (value: string | undefined) => {
    if (!isDefined(value)) {
      return undefined;
    }
    if (+value > restriction.max) {
      return restriction.max;
    }
    return +value < restriction.min ? restriction.min : +value;
  };

export const makeEmptyStringPropertiesNull = <T extends object>(obj: T): T => {
  return Object.entries(obj).reduce(
    (acc, [key, value]) => ({
      ...acc,
      [key]: value === '' ? null : value,
    }),
    {} as T,
  );
};

const sortMap = {
  ['']: undefined,
  asc: 'ASCENDING',
  desc: 'DESCENDING',
} satisfies Record<SortDirection, SortOrder | undefined>;
export const inverseSortMap = {
  ASCENDING: 'asc',
  DESCENDING: 'desc',
} satisfies Record<SortOrder, SortDirection>;

export const sortList =
  (router: Router, relativeTo: ActivatedRoute) => (event: Sort) => {
    router.navigate(['.'], {
      relativeTo,
      queryParams: {
        sortColumn: event.active,
        sortOrder: sortMap[event.direction],
      },
      queryParamsHandling: 'merge',
      replaceUrl: true,
    });
  };

export const paginateList =
  (router: Router, relativeTo: ActivatedRoute) => (event: PageEvent) => {
    router.navigate(['.'], {
      relativeTo,
      queryParams: {
        page: event.pageIndex,
        pageSize: event.pageSize,
      },
      queryParamsHandling: 'merge',
      replaceUrl: true,
    });
  };

export const getSortAndPageInputs = <T>(inputs: SortAndPageInputs<T>) => {
  return {
    sortColumn: inputs.sortColumn(),
    sortOrder: inputs.sortOrder(),
    page: inputs.page() ?? 0,
    pageSize: inputs.pageSize() ?? DEFAULT_PAGE_SIZE,
  };
};

export const limitPageToNumberOfEntriesEffect = <T>(
  inputs: SortAndPageInputs<T>,
  totalEntriesSig: Signal<number | undefined>,
  router: Router,
  relativeTo: ActivatedRoute,
) => {
  return effect(() => {
    const { page, pageSize } = getSortAndPageInputs(inputs);
    const totalEntries = totalEntriesSig();

    if (page && pageSize && totalEntries && page * pageSize > totalEntries) {
      router.navigate(['.'], {
        relativeTo,
        queryParams: {
          page: Math.ceil(totalEntries / pageSize) - 1,
        },
        queryParamsHandling: 'merge',
        replaceUrl: true,
      });
    }
  });
};

export const partiallyDebounceFormValueChangesSig = <
  const T extends { [k in string]: AbstractControl },
  const K extends keyof T,
>(
  form: FormGroup<T>,
  excludedFields: K[],
) => {
  const debouncedFields$ = (Object.keys(form.value) as K[])
    .map((key) => {
      const field = form.controls[key.toString()];
      if (!field) {
        console.warn(`Field ${key.toString()} not found in form controls.`);
        return null;
      }

      return excludedFields.includes(key)
        ? field.valueChanges
        : field.valueChanges.pipe(debounceTime(INPUT_DELAY));
    })
    .filter(isDefined);
  return toSignal(merge(...debouncedFields$), { equal: () => false });
};
