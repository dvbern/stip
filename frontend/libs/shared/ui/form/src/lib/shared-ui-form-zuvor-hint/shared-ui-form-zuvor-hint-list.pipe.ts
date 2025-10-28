import { Pipe, PipeTransform } from '@angular/core';

import { getChangesForList } from '@dv/shared/util-fn/gesuch-util';

/**
 * Checks if the List obtained from {@link getChangesForList} contains the given entry.
 *
 * @example
 * <div>
 *  ＠if (listChanges | dvZuvorListContains: $index) {
 *    ...
 *  }
 * </div>
 */
@Pipe({
  name: 'dvZuvorListContains',
  standalone: true,
})
export class SharedUiFormZuvorHintListPipe implements PipeTransform {
  transform(
    value: ReturnType<typeof getChangesForList>,
    index: number,
  ): boolean {
    return (
      Object.keys(
        value?.changesByIndex[index] ??
          value?.newEntriesByIdentifier[index] ??
          {},
      ).length > 0
    );
  }
}
