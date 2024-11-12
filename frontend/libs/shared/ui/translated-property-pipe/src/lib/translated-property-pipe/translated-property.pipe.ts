import { Pipe, PipeTransform } from '@angular/core';

import {
  NameOfTranslated,
  OnlyKeysWithDeOrFr,
  getTranslatableProp,
} from '@dv/shared/model/type-util';

@Pipe({
  standalone: true,
  name: 'translatedProp',
})
export class TranslatedPropertyPipe implements PipeTransform {
  transform<T extends { [key in K]: unknown }, K extends OnlyKeysWithDeOrFr<T>>(
    value: Partial<T> | undefined,
    property: NameOfTranslated<K>,
    language: string,
  ): T[K] | null {
    return getTranslatableProp(value, property, language);
  }
}
