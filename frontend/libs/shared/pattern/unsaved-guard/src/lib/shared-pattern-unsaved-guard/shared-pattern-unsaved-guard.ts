import { inject } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { CanDeactivateFn } from '@angular/router';
import { map } from 'rxjs';

import {
  ComponentWithForm,
  hasUnsavedChanges,
} from '@dv/shared/util/unsaved-changes';

import { SharedPatternConfirmDialogComponent } from '../../components/confirm-dialog/confirm-dialog.component';

export const checkUnsavedChanges: CanDeactivateFn<ComponentWithForm> = (
  component: ComponentWithForm,
) => {
  if (hasUnsavedChanges(component)) {
    const dialog = inject(MatDialog);

    const dialogRef = dialog.open(SharedPatternConfirmDialogComponent);
    return dialogRef.afterClosed().pipe(map((confirmed) => confirmed ?? false));
  }
  return true;
};
