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

@Pipe({
  standalone: true,
  name: 'rdIsPendingWithoutCache',
})
export class SharedUiRdIsPendingWithoutCachePipe implements PipeTransform {
  transform<T>(value: CachedRemoteData<T>): boolean {
    return isPendingWithoutCache(value);
  }
}
