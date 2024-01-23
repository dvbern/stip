import { CanDeactivateFn } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { inject } from '@angular/core';

import {
  ComponentWithForm,
  hasUnsavedChanges,
} from '@dv/shared/util/unsaved-changes';

import { SharedPatternConfirmDialogComponent } from '../../components/confirm-dialog/confirm-dialog.component';
import { map } from 'rxjs';

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
