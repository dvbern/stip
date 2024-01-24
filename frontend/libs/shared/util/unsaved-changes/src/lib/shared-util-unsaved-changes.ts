import { AbstractControl } from '@angular/forms';
import { Observable, distinctUntilChanged, map, merge } from 'rxjs';

export type ComponentWithForm =
  | {
      form: AbstractControl;
    }
  | { hasUnsavedChanges: boolean };

export const hasUnsavedChanges = (component: ComponentWithForm) => {
  return 'form' in component
    ? component.form.dirty
    : component.hasUnsavedChanges;
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
  resetEvent: Observable<unknown>,
  ...moreResetEvents: Observable<unknown>[]
) => {
  return merge(
    form.valueChanges.pipe(map(() => form.dirty)),
    merge(resetEvent, ...moreResetEvents).pipe(map(() => false)),
  ).pipe(distinctUntilChanged());
};
