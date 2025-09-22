import { Injectable, inject } from '@angular/core';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { MatPaginatorIntl } from '@angular/material/paginator';
import { TranslocoService } from '@jsverse/transloco';
import { filter, startWith, switchMap } from 'rxjs';

import {
  SharedTranslationKey,
  translatableShared,
} from '@dv/shared/assets/i18n';

type TranslatableProperties = keyof Pick<
  SharedUtilPaginatorTranslation,
  | 'itemsPerPageLabel'
  | 'firstPageLabel'
  | 'lastPageLabel'
  | 'nextPageLabel'
  | 'previousPageLabel'
>;

export const paginatorTranslationProvider = () => ({
  provide: MatPaginatorIntl,
  useClass: SharedUtilPaginatorTranslation,
});

@Injectable()
export class SharedUtilPaginatorTranslation extends MatPaginatorIntl {
  private translateService = inject(TranslocoService);
  private labelMap: Record<TranslatableProperties, SharedTranslationKey> = {
    itemsPerPageLabel: 'shared.table.paginator.itemsPerPage',
    nextPageLabel: 'shared.table.paginator.nextPage',
    lastPageLabel: 'shared.table.paginator.lastPage',
    previousPageLabel: 'shared.table.paginator.previousPage',
    firstPageLabel: 'shared.table.paginator.firstPage',
  };
  private translationsInitializedSig = toSignal(
    this.translateService.events$.pipe(
      filter(({ type }) => type === 'translationLoadSuccess'),
    ),
  );

  constructor() {
    super();

    this.translateService.langChanges$
      .pipe(
        takeUntilDestroyed(),
        startWith({}),
        switchMap(() =>
          this.translateService.selectTranslate(Object.values(this.labelMap)),
        ),
      )
      .subscribe((translation) => {
        Object.entries(this.labelMap).forEach(([key, value]) => {
          this[key as unknown as TranslatableProperties] = translation[value];
        });
        this.changes.next();
      });
  }

  override getRangeLabel = (
    page: number,
    pageSize: number,
    length: number,
  ): string => {
    if (!this.translationsInitializedSig()) {
      return '';
    }

    return (
      this.translateService.translate(
        translatableShared('shared.table.paginator.range'),
        {
          page: page + 1,
          pages: Math.ceil(length / pageSize),
        },
      ) ?? ''
    );
  };
}
