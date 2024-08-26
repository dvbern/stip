import { Pipe, PipeTransform } from '@angular/core';

import {
  CachedRemoteData,
  RemoteData,
  isPending,
  isPendingWithoutCache,
} from '@dv/shared/util/remote-data';

@Pipe({
  standalone: true,
  name: 'rdIsPending',
})
export class SharedUiRdIsPendingPipe implements PipeTransform {
  transform<T>(value: RemoteData<T> | CachedRemoteData<T>): boolean {
    return isPending(value);
  }
}

/**
 * Only returns `true` if the `data` field is `undefined` and `remote data` is pending.
 * Else if the `data` field is not `undefined` and `remote data` is pending, it returns `false`.
 *
 * Useful to not show a spinner if the data is already there but the request is still pending.
 */
@Pipe({
  standalone: true,
  name: 'rdIsPendingWithoutCache',
})
export class SharedUiRdIsPendingWithoutCachePipe implements PipeTransform {
  transform<T>(value: CachedRemoteData<T>): boolean {
    return isPendingWithoutCache(value);
  }
}
