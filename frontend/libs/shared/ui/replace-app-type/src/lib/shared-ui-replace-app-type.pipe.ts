import { Pipe, PipeTransform, inject } from '@angular/core';

import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';

@Pipe({
  name: 'replaceAppType',
  standalone: true,
})
export class SharedUiReplaceAppTypePipe implements PipeTransform {
  private config = inject(SharedModelCompileTimeConfig);

  transform(
    value: `${string}.$type.${string}` | `$type.${string}` | `${string}.$type`,
  ): string {
    return `${value.replace('$type', this.config.appType)}`;
  }
}
