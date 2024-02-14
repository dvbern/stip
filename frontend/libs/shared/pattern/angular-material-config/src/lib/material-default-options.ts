import {
  MAT_FORM_FIELD_DEFAULT_OPTIONS,
  MatFormFieldDefaultOptions,
} from '@angular/material/form-field';

export function provideMaterialDefaultOptions(
  options?: MatFormFieldDefaultOptions,
) {
  return [
    {
      provide: MAT_FORM_FIELD_DEFAULT_OPTIONS,
      useValue: {
        appearance: 'outline',
        floatLabel: 'always',
        hideRequiredMarker: true,
        ...options,
      } as MatFormFieldDefaultOptions,
    },
  ];
}
