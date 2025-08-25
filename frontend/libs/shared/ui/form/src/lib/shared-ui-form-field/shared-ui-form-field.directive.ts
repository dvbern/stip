import {
  AfterViewInit,
  ChangeDetectorRef,
  DestroyRef,
  Directive,
  DoCheck,
  contentChildren,
  effect,
  inject,
} from '@angular/core';
import { takeUntilDestroyed, toObservable } from '@angular/core/rxjs-interop';
import { FormControlDirective, FormControlName } from '@angular/forms';
import { MatFormField } from '@angular/material/form-field';
import { Subject, combineLatest, of } from 'rxjs';
import { distinctUntilChanged, map, startWith } from 'rxjs/operators';

import { SharedUiFormMessageErrorDirective } from '../shared-ui-form-message/shared-ui-form-message-error.directive';
import { SharedUiFormReadonlyDirective } from '../shared-ui-form-readonly/shared-ui-form-readonly.directive';

@Directive({
  selector: '[dvSharedUiFormField]',
  standalone: true,
})
export class SharedUiFormFieldDirective implements DoCheck, AfterViewInit {
  errorMessagesSig = contentChildren(SharedUiFormMessageErrorDirective, {
    descendants: true,
  });
  touchedStateDuringCheck$ = new Subject<boolean | null>();

  // Can be used on a MatFormField or FormControlName component/directive, useful for radio-groups for example
  matFormField = inject(MatFormField, { optional: true });
  selfControlName = inject(FormControlName, { optional: true });
  selfControlDirective = inject(FormControlDirective, { optional: true });
  readonlyChecker = inject(SharedUiFormReadonlyDirective, { optional: true });
  changeDetector = inject(ChangeDetectorRef);

  private destroyRef = inject(DestroyRef);
  private errorMessages$ = toObservable(this.errorMessagesSig);
  private get nullableControl() {
    return (
      this.matFormField?._control?.ngControl ??
      this.selfControlName ??
      this.selfControlDirective
    );
  }
  private get control() {
    if (!this.nullableControl) {
      throw new Error(
        'No ngControl was found, please make sure that there is a MatFormFieldControl',
      );
    }
    return this.nullableControl;
  }

  constructor() {
    const checker = this.readonlyChecker;
    if (checker && this.matFormField) {
      const formField = this.matFormField;
      effect(() => {
        const previous = formField.subscriptSizing;
        if (checker.isReadonly()) {
          formField.subscriptSizing = 'dynamic';
        } else {
          formField.subscriptSizing = 'fixed';
        }
        if (previous !== formField.subscriptSizing) {
          this.changeDetector.markForCheck();
        }
      });
    }
  }

  ngDoCheck(): void {
    if (!this.nullableControl) {
      return;
    }

    this.touchedStateDuringCheck$.next(this.control.touched);
  }

  ngAfterViewInit() {
    const touched$ = this.touchedStateDuringCheck$.pipe(
      startWith(null),
      distinctUntilChanged(),
    );
    const validityHasChanged$ =
      this.control.statusChanges?.pipe(
        startWith(null),
        map(() => this.control.status),
      ) ?? of(null);
    combineLatest([validityHasChanged$, this.errorMessages$, touched$])
      .pipe(
        map((values) => [...values, this.control.touched] as const),
        takeUntilDestroyed(this.destroyRef),
      )
      .subscribe(([, errorMesages, touchedOnCheck, touched]) => {
        if (touchedOnCheck === false || !touched) {
          errorMesages?.forEach((m) => m.hide());
        } else {
          errorMesages?.forEach((m) => {
            if (m.errorKey && this.control.errors?.[m.errorKey]) {
              m.show();
            } else {
              m.hide();
            }
          });
        }
      });
  }
}
