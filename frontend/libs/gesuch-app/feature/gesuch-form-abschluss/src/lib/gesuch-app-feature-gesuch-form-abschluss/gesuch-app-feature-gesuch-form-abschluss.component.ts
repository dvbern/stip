import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  computed,
  inject,
} from '@angular/core';
import {
  takeUntilDestroyed,
  toObservable,
  toSignal,
} from '@angular/core/rxjs-interop';
import { NgbAlert } from '@ng-bootstrap/ng-bootstrap';
import { Store } from '@ngrx/store';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { combineLatest, distinctUntilChanged, filter, map } from 'rxjs';

import {
  GesuchAppDataAccessAbschlussApiEvents,
  selectGesuchAppDataAccessAbschlusssView,
} from '@dv/gesuch-app/data-access/abschluss';
import { SharedDataAccessStammdatenApiEvents } from '@dv/shared/data-access/stammdaten';
import { SharedEventGesuchFormAbschluss } from '@dv/shared/event/gesuch-form-abschluss';
import {
  GENERIC_REQUIRED_ERROR,
  ValidationError,
} from '@dv/shared/model/error';
import { getLatestGesuchIdFromGesuchOnUpdate$ } from '@dv/shared/util/gesuch';
import { onTranslationChanges$ } from '@dv/shared/util-fn/translation-helper';
import { isDefined } from '@dv/shared/util-fn/type-guards';

export const DEFAULT_ERROR_KEY = 'shared.gesuch.validation.generic';
@Component({
  selector: 'dv-gesuch-app-feature-gesuch-form-abschluss',
  standalone: true,
  imports: [CommonModule, TranslateModule, NgbAlert],
  templateUrl: './gesuch-app-feature-gesuch-form-abschluss.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GesuchAppFeatureGesuchFormAbschlussComponent implements OnInit {
  private store = inject(Store);
  private translate = inject(TranslateService);
  private translationsSig = toSignal(onTranslationChanges$(this.translate));

  viewSig = this.store.selectSignal(selectGesuchAppDataAccessAbschlusssView);
  validationMessagesSig = computed(() => {
    const validations = this.viewSig().validations;
    const translations = this.translationsSig()?.translations;

    if (!validations || !translations) {
      return [];
    }

    const parsedValidations = validations.map(
      getTranslationFromValidation(translations),
    );
    return parsedValidations.some((v) => v === null)
      ? [{ key: DEFAULT_ERROR_KEY }]
      : parsedValidations;
  });
  defaultErrorKey = DEFAULT_ERROR_KEY;

  constructor() {
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
    this.store.dispatch(SharedDataAccessStammdatenApiEvents.init());
  }

  abschliessen(gesuchId: string) {
    this.store.dispatch(
      GesuchAppDataAccessAbschlussApiEvents.gesuchAbschliessen({ gesuchId }),
    );
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
