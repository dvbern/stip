import { DOCUMENT } from '@angular/common';
import { ApplicationRef, ElementRef, Injectable, inject } from '@angular/core';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';
import { Subject, concat, from, of } from 'rxjs';
import { filter, map, switchMap, take } from 'rxjs/operators';

import { fromFormatedNumber } from '@dv/shared/util/maskito-util';
import {
  ComponentWithForm,
  hasUnsavedChanges,
} from '@dv/shared/util/unsaved-changes';
import { isDefined } from '@dv/shared/util-fn/type-guards';

type OnlyString<T> = T extends string ? T : never;

@Injectable({
  providedIn: 'root',
})
export class SharedUtilFormService {
  private focusInput$ = new Subject<ElementRef<HTMLElement>>();
  private appRef = inject(ApplicationRef);
  private translate = inject(TranslateService);
  private wndw = inject(DOCUMENT, { optional: true })?.defaultView;

  constructor() {
    this.focusInput$
      .pipe(
        switchMap((parent) =>
          // Material components seem to take some time until they are marked as invalid
          this.appRef.isStable.pipe(
            filter((isStable) => isStable),
            take(1),
            map(() => parent),
          ),
        ),
        takeUntilDestroyed(),
      )
      .subscribe((parent) => {
        const input = parent.nativeElement.querySelector<HTMLElement>(
          'input.ng-invalid, .mat-mdc-select-invalid, .mat-mdc-radio-group.ng-invalid input[type=radio]',
        );
        if (input) {
          input.focus();
          input.scrollIntoView({ block: 'center' });
        }
      });
  }

  /**
   * Register a form for unsaved changes check
   *
   * It creates a handler for the beforeunload event and removes it when the component is destroyed
   *
   * @param component The target component that has a form property or an unsaved indicator
   */
  registerFormForUnsavedCheck(component: ComponentWithForm) {
    const wndw = this.wndw;
    // Prevent accessing window directly because there are cases where it is not available, for example in SSR
    // even if currently no such feature is currently planned
    if (isDefined(wndw)) {
      const handler = (e: BeforeUnloadEvent) => {
        const unsaved = hasUnsavedChanges(component);
        const message = unsaved
          ? // Browsers show their own message, but just in case we try to set our own message
            this.translate.instant('shared.dialog.unsavedChanges.text')
          : null;
        if (message) {
          e.returnValue = message;
        }
        return message;
      };
      // Register the before close event handler
      wndw.addEventListener('beforeunload', handler);
      concat(
        // Create a never ending observable to keep the handler alive until the component is destroyed
        from(new Promise(() => undefined)).pipe(takeUntilDestroyed()),
        // continue with a instantly ending observable to remove the handler once the previous observable is completed
        of({}),
      ).subscribe(() => {
        wndw.removeEventListener('beforeunload', handler);
      });
    }
  }

  focusFirstInvalid(elementRef: ElementRef<HTMLElement>) {
    this.focusInput$.next(elementRef);
  }
  /**
   * Create a converter for a form that has formatted number strings
   *
   * It returns 2 functions, one to convert API values to string and one to convert string values to API values
   *
   * @example
   * const converter = this.formUtils.createNumberConverter(
   *   this.form,
   *   [
   *     'einkommen',
   *     'wohnkosten',
   *   ]
   * );
   *
   * this.form.patchValue(converter.toString(apiValues));
   *
   * store.saveValues$(converter.toNumber());
   */
  createNumberConverter<
    T extends { [k: string]: AbstractControl<string | null> },
    K extends OnlyString<keyof T>,
  >(group: FormGroup<T>, numberFields: K[]) {
    return {
      toNumber: () =>
        numberFields.reduce(
          (acc, key) => {
            return { ...acc, [key]: fromFormatedNumber(group.get(key)?.value) };
          },
          {} as Record<K, number>,
        ),
      toString: (values: { [key in K]?: number }) =>
        numberFields.reduce(
          (acc, key) => {
            return { ...acc, [key]: values[key]?.toString() };
          },
          {} as Record<K, string>,
        ),
    };
  }

  /**
   * Used to set the disabled state of the given control
   */
  setDisabledState(
    control: FormControl,
    isDisabled: boolean,
    clearOnDisable?: boolean,
    options?: { emitEvent: boolean },
  ) {
    if (isDisabled) {
      if (clearOnDisable) {
        control.reset();
      }
      control.disable(options);
    } else {
      control.enable(options);
    }
  }

  /**
   * Used to add or remove the required validators, validity checks are also triggered afterwards
   */
  setRequired(control: FormControl, required: boolean) {
    if (required) {
      control.addValidators(Validators.required);
    } else {
      control.removeValidators(Validators.required);
    }
    control.updateValueAndValidity();
  }

  /**
   * Convert the value changes from a given control into a signal
   */
  signalFromChanges<R>(
    control: FormControl<R>,
  ): ReturnType<typeof toSignal<R | undefined>>;
  /**
   * Convert the value changes from a given control into a signal with default values
   */
  signalFromChanges<R>(
    control: FormControl<R>,
    opts: { useDefault: boolean },
  ): ReturnType<typeof toSignal<R, R>>;
  signalFromChanges<R>(
    control: FormControl<R>,
    opts?: { useDefault?: boolean },
  ) {
    return opts?.useDefault
      ? toSignal<R, R>(control.valueChanges, {
          initialValue: control.defaultValue,
        })
      : toSignal<R>(control.valueChanges);
  }
}
