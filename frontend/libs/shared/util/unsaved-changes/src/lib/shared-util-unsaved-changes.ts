import { Signal } from '@angular/core';
import { AbstractControl } from '@angular/forms';
import { EMPTY, Observable, distinctUntilChanged, map, merge } from 'rxjs';

export type ComponentWithForm =
  | {
      form: AbstractControl;
    }
  | {
      formSig: Signal<AbstractControl>;
    }
  | { hasUnsavedChanges: boolean };

export const hasUnsavedChanges = (component: ComponentWithForm) => {
  if ('form' in component) {
    return component.form.dirty;
  }
  if ('formSig' in component) {
    return component.formSig().dirty;
  }
  if ('hasUnsavedChanges' in component) {
    return component.hasUnsavedChanges;
  }
  throw new Error('No form or unsaved indicator found');
};

/**
 * Observe unsaved changes of a form.
 *
 * @param form The form to observe
 * @param resetEvent The reset event of the form
 * @param moreResetEvents More reset events of the form, for example cancel or delete
 */
export const observeUnsavedChanges = (
  form: AbstractControl,
  resetEvent?: Observable<unknown>,
  ...moreResetEvents: Observable<unknown>[]
) => {
  return merge(
    form.valueChanges.pipe(map(() => form.dirty)),
    resetEvent
      ? merge(resetEvent, ...moreResetEvents).pipe(map(() => false))
      : EMPTY,
  ).pipe(distinctUntilChanged());
};
