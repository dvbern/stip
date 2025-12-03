import { Provider } from '@angular/core';
import {
  MAT_DIALOG_DEFAULT_OPTIONS,
  MatDialogConfig,
} from '@angular/material/dialog';
import {
  MAT_FORM_FIELD_DEFAULT_OPTIONS,
  MatFormFieldDefaultOptions,
} from '@angular/material/form-field';

/**
 * Material default options with a custom form field appearance and default dialog configuration.
 */
export function provideMaterialDefaultOptions(
  options?: MatFormFieldDefaultOptions,
): Provider[] {
  return [
    {
      provide: MAT_DIALOG_DEFAULT_OPTIONS,
      useValue: {
        maxWidth: '95vw',
      } as MatDialogConfig,
    },
    {
      provide: MAT_FORM_FIELD_DEFAULT_OPTIONS,
      useValue: {
        appearance: 'outline',
        floatLabel: 'always',
        ...options,
      } as MatFormFieldDefaultOptions,
    },
  ];
}
