import { Pipe, PipeTransform, inject } from '@angular/core';

import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';

@Pipe({
  name: 'replaceAppConfig',
  standalone: true,
})
export class SharedUiReplaceAppConfigPipe implements PipeTransform {
  private config = inject(SharedModelCompileTimeConfig);

  transform(
    value: `${string}.$type.${string}` | `$type.${string}` | `${string}.$type`,
  ): string {
    return `${value.replace('$type', this.config.app.keyPrefix)}`;
  }
}
