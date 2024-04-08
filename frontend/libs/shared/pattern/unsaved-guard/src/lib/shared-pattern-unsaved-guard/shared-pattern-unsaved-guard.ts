import { Type, inject } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { CanDeactivateFn, Route } from '@angular/router';
import { map } from 'rxjs';

import { SharedUiConfirmDialogComponent } from '@dv/shared/ui/confirm-dialog';
import {
  ComponentWithForm,
  hasUnsavedChanges,
} from '@dv/shared/util/unsaved-changes';

export const checkUnsavedChanges =
  <T extends ComponentWithForm>(): CanDeactivateFn<T> =>
  (component: T) => {
    if (hasUnsavedChanges(component)) {
      const dialog = inject(MatDialog);

      const dialogRef = SharedUiConfirmDialogComponent.open(dialog, {
        title: 'shared.dialog.unsavedChanges.title',
        message: 'shared.dialog.unsavedChanges.text',
        confirmText: 'shared.dialog.unsavedChanges.confirm',
        cancelText: 'shared.dialog.unsavedChanges.cancel',
      });
      return dialogRef
        .afterClosed()
        .pipe(map((confirmed) => confirmed ?? false));
    }
    return true;
  };

export const routeWithUnsavedChangesGuard = <
  C extends ComponentWithForm,
  T extends Route & {
    component: Type<C>;
    canDeactivate?: never;
  },
>(
  config: T,
  moreCanDeactivate?: CanDeactivateFn<C>[],
): Route => {
  return {
    ...config,
    canDeactivate: [checkUnsavedChanges<C>(), ...(moreCanDeactivate ?? [])],
  };
};
