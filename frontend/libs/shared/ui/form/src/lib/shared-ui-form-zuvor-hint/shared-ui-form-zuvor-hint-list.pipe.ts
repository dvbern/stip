import { Pipe, PipeTransform } from '@angular/core';

import { getChangesForList } from '@dv/shared/util-fn/gesuch-util';

/**
 * Checks if the List obtained from {@link getChangesForList} contains the given entry.
 *
 * @example
 * <div>
 *  ＠if (listChanges | dvZuvorListContains: someEntryWithId) {
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
    entry: { id?: string },
  ): boolean {
    return !!(
      entry.id &&
      (value?.changesByIdentifier?.[entry.id] || value?.newEntries?.[entry.id])
    );
  }
}
