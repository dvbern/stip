import { CdkRowDef } from '@angular/cdk/table';
import { Directive, Input, Predicate, input } from '@angular/core';
import { MatRowDef, MatTableDataSource } from '@angular/material/table';
import { Observable } from 'rxjs';

import { isDefined } from '@dv/shared/model/type-util';

/**
 * Row definition for the mat-table that has a type-safe dataSource input for type inference
 *
 * @example
 * ```html
 * <tr
 *   mat-row
 *   *dvMatRowDef="let data; dataSource: dataSourceSig(); columns: displayedColumns"
 * ></tr>
 * ```
 */
@Directive({
  selector: '[dvMatRowDef]',
  standalone: true,
  providers: [{ provide: CdkRowDef, useExisting: TypeSafeMatRowDefDirective }],
})
export class TypeSafeMatRowDefDirective<T> extends MatRowDef<T> {
  @Input({ required: true }) dvMatRowDefDataSource!:
    | T[]
    | Observable<T[]>
    | MatTableDataSource<T>;
  @Input({ alias: 'dvMatRowDefColumns' }) override columns!: string[];
  onlyIfFnSig = input<Predicate<T> | undefined>(undefined, { alias: 'onlyIf' });

  static ngTemplateContextGuard<T>(
    directive: TypeSafeMatRowDefDirective<T>,
    ctx: unknown,
  ): ctx is { $implicit: T; index: number } {
    if (!isCorrectContext<T>(ctx)) {
      return false;
    }
    return directive.onlyIfFnSig()?.(ctx.$implicit) ?? true;
  }
}

const isCorrectContext = <T>(
  ctx: unknown,
): ctx is { $implicit: T; index: number } => {
  return (
    isDefined(ctx) &&
    typeof ctx === 'object' &&
    '$implicit' in ctx &&
    'index' in ctx
  );
};
