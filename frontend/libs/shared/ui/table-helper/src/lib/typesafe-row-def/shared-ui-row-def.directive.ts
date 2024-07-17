import { CdkRowDef } from '@angular/cdk/table';
import { Directive, Input } from '@angular/core';
import { MatRowDef, MatTableDataSource } from '@angular/material/table';
import { Observable } from 'rxjs';

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
  // eslint-disable-next-line @angular-eslint/no-input-rename
  @Input({ alias: 'dvMatRowDefColumns' }) override columns!: string[];

  static ngTemplateContextGuard<T>(
    _: TypeSafeMatRowDefDirective<T>,
    ctx: unknown,
  ): ctx is { $implicit: T; index: number } {
    return true;
  }
}
