import { InputSignal, InputSignalWithTransform } from '@angular/core';

import { SortOrder } from '@dv/shared/model/gesuch';

export interface SortAndPageInputs<SortType> {
  sortColumn: InputSignalWithTransform<
    SortType | undefined,
    SortType | undefined
  >;
  sortOrder: InputSignal<SortOrder | undefined>;
  page: InputSignalWithTransform<number | undefined, string | undefined>;
  pageSize: InputSignalWithTransform<number | undefined, string | undefined>;
}
