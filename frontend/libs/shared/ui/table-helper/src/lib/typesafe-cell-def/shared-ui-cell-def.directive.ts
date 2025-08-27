import { CdkCellDef } from '@angular/cdk/table';
import { Directive, Input } from '@angular/core';
import { MatCellDef, MatTableDataSource } from '@angular/material/table';
import { Observable } from 'rxjs';

/**
 * Cell definition for the mat-table that has a type-safe dataSource input for type inference
 *
 * @example
 * ```html
 * <td mat-cell *dvMatCellDef="let gesuchsperiode; dataSource: source">
 *   {{ gesuchsperiode | translatedProp: 'bezeichnung' : translate.getActiveLang() }}
 * </td>
 * ```
 */
@Directive({
  selector: '[dvMatCellDef]',
  standalone: true,
  providers: [
    { provide: CdkCellDef, useExisting: TypeSafeMatCellDefDirective },
  ],
})
export class TypeSafeMatCellDefDirective<T> extends MatCellDef {
  @Input({ required: true }) dvMatCellDefDataSource!:
    | T[]
    | Observable<T[]>
    | MatTableDataSource<T>;

  static ngTemplateContextGuard<T>(
    _: TypeSafeMatCellDefDirective<T>,
    ctx: unknown,
  ): ctx is { $implicit: T; index: number } {
    return true;
  }
}
