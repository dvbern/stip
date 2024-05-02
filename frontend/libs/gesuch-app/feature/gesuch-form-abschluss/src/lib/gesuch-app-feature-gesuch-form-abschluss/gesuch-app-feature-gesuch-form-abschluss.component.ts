import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  OnInit,
  inject,
} from '@angular/core';
import { takeUntilDestroyed, toObservable } from '@angular/core/rxjs-interop';
import { MatDialog } from '@angular/material/dialog';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';
import { combineLatest, distinctUntilChanged, filter, map } from 'rxjs';

import {
  GesuchAppDataAccessAbschlussApiEvents,
  selectGesuchAppDataAccessAbschlusssView,
} from '@dv/gesuch-app/data-access/abschluss';
import { SharedEventGesuchFormAbschluss } from '@dv/shared/event/gesuch-form-abschluss';
import {
  GENERIC_REQUIRED_ERROR,
  ValidationError,
} from '@dv/shared/model/error';
import { SharedUiConfirmDialogComponent } from '@dv/shared/ui/confirm-dialog';
import { getLatestGesuchIdFromGesuchOnUpdate$ } from '@dv/shared/util/gesuch';
import { isDefined } from '@dv/shared/util-fn/type-guards';

export const DEFAULT_ERROR_KEY = 'shared.gesuch.validation.generic';

@Component({
  selector: 'dv-gesuch-app-feature-gesuch-form-abschluss',
  standalone: true,
  imports: [CommonModule, TranslateModule],
  templateUrl: './gesuch-app-feature-gesuch-form-abschluss.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GesuchAppFeatureGesuchFormAbschlussComponent implements OnInit {
  private store = inject(Store);
  private dialog = inject(MatDialog);
  destroyRef = inject(DestroyRef);

  viewSig = this.store.selectSignal(selectGesuchAppDataAccessAbschlusssView);

  constructor() {
    // validate form only if no formErrors form validatePages are present
    combineLatest([
      getLatestGesuchIdFromGesuchOnUpdate$(this.viewSig).pipe(
        filter(isDefined),
      ),
      toObservable(this.viewSig).pipe(
        map((view) => view.canCheck),
        distinctUntilChanged(),
        filter((canCheck) => !!canCheck),
        takeUntilDestroyed(),
      ),
    ]).subscribe(([gesuchId]) => {
      this.store.dispatch(
        GesuchAppDataAccessAbschlussApiEvents.check({
          gesuchId,
        }),
      );
    });
  }

  ngOnInit(): void {
    this.store.dispatch(SharedEventGesuchFormAbschluss.init());
  }

  abschliessen(gesuchId: string) {
    const dialogRef = SharedUiConfirmDialogComponent.open(this.dialog, {
      title: 'shared.form.abschluss.dialog.title',
      message: 'shared.form.abschluss.dialog.text',
      confirmText: 'shared.form.abschluss.abschliessen',
      cancelText: 'shared.cancel',
    });

    dialogRef
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((confirmed) => {
        if (confirmed) {
          this.store.dispatch(
            GesuchAppDataAccessAbschlussApiEvents.gesuchAbschliessen({
              gesuchId,
            }),
          );
        }
      });
  }
}

/**
 * Returns a translation key and params for a given validation error
 *
 * It handles 4 cases:
 * 1. If the messageTemplate is GENERIC_REQUIRED_ERROR, it returns the translation key and the section name
 *    derived from the propertyPath or the DEFAULT_ERROR_KEY if the path is not known
 * 2. If the messageTemplate is found in the translations, it is returned as the key
 * 3. If the messageTemplate is not found and the message is not empty, the message is returned as the key
 * 4. If the messageTemplate is not found and the message is empty, null is returned
 */
export const getTranslationFromValidation =
  (translations: Record<string, string>) =>
  (
    validationError: ValidationError,
  ): { key: string; params?: Record<string, string> } | null => {
    if (validationError.messageTemplate === GENERIC_REQUIRED_ERROR) {
      const path = validationError.propertyPath?.split('.') ?? [];
      if (path.length === 3) {
        const sectionKey = `shared.${path[path.length - 1]}.title`;
        if (sectionKey in translations) {
          return {
            key: `shared.gesuch.validation.${validationError.messageTemplate}`,
            params: { section: translations[sectionKey] },
          };
        }
      }
      return {
        key: DEFAULT_ERROR_KEY,
      };
    }

    const key = `shared.gesuch.validation.${validationError.messageTemplate}`;
    if (key in translations) {
      return { key };
    }

    const message =
      validationError.message.length > 0 ? validationError.message : null;
    return message ? { key: message } : null;
  };
