import { Pipe, PipeTransform, inject } from '@angular/core';

import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';

@Pipe({
  name: 'prefixAppType',
  standalone: true,
})
export class SharedUiPrefixAppTypePipe implements PipeTransform {
  private config = inject(SharedModelCompileTimeConfig);

  transform(value: `$type.${string}`): string {
    return `${value.replace('$type', this.config.appType)}`;
  }
}
