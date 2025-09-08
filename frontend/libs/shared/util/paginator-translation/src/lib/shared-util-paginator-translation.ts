import { Injectable, inject } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatPaginatorIntl } from '@angular/material/paginator';
import { TranslocoService } from '@jsverse/transloco';
import { startWith, switchMap } from 'rxjs';

type TranslatableProperties = keyof Pick<
  SharedUtilPaginatorTranslation,
  | 'itemsPerPageLabel'
  | 'firstPageLabel'
  | 'lastPageLabel'
  | 'nextPageLabel'
  | 'previousPageLabel'
  | 'rangeLabel'
>;

export const paginatorTranslationProvider = () => ({
  provide: MatPaginatorIntl,
  useClass: SharedUtilPaginatorTranslation,
});

@Injectable()
export class SharedUtilPaginatorTranslation extends MatPaginatorIntl {
  private translateService = inject(TranslocoService);
  public rangeLabel?: string;
  private labelMap: Record<TranslatableProperties, string> = {
    itemsPerPageLabel: 'shared.table.paginator.itemsPerPage',
    nextPageLabel: 'shared.table.paginator.nextPage',
    lastPageLabel: 'shared.table.paginator.lastPage',
    previousPageLabel: 'shared.table.paginator.previousPage',
    firstPageLabel: 'shared.table.paginator.firstPage',
    rangeLabel: 'shared.table.paginator.range',
  };

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
    const rangeLabel = this.rangeLabel;
    if (!rangeLabel) {
      return '';
    }
    return (
      this.translateService.translate(rangeLabel, {
        page: page + 1,
        pages: Math.ceil(length / pageSize),
      }) ?? ''
    );
  };
}
