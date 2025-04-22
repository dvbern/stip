import { Pipe, PipeTransform } from '@angular/core';

import {
  CachedRemoteData,
  RemoteData,
  isInitial,
  isPending,
  isPendingWithoutCache,
} from '@dv/shared/util/remote-data';

/**
 * Pipe to check if a RemoteData is pending or initial.
 *
 * It is possible to ignore the initial state by passing the `ignoreInitial` option.
 *
 * @example
 * if (someRdSig() | rdIsPending) { ...
 *
 * @example
 * if (someRdSig() | rdIsPending: { ignoreInitial: true }) { ...
 */
@Pipe({
  standalone: true,
  name: 'rdIsPending',
})
export class SharedUiRdIsPendingPipe implements PipeTransform {
  transform<T>(
    value: RemoteData<T> | CachedRemoteData<T>,
    opts?: { ignoreInitial?: boolean },
  ): boolean {
    return (!opts?.ignoreInitial && isInitial(value)) || isPending(value);
  }
}

/**
 * Pipe to check if a RemoteData is pending without cache or initial.
 *
 * It is possible to ignore the initial state by passing the `ignoreInitial` option.
 *
 * @example
 * if (someRdSig() | rdIsPendingWithoutCache) { ...
 *
 * @example
 * if (someRdSig() | rdIsPendingWithoutCache: { ignoreInitial: true }) { ...
 */
@Pipe({
  standalone: true,
  name: 'rdIsPendingWithoutCache',
})
export class SharedUiRdIsPendingWithoutCachePipe implements PipeTransform {
  transform<T>(
    value: CachedRemoteData<T>,
    opts?: { ignoreInitial?: boolean },
  ): boolean {
    return (
      (!opts?.ignoreInitial && isInitial(value)) || isPendingWithoutCache(value)
    );
  }
}
