import { signal } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { FormControl } from '@angular/forms';

import { prepareSteuerjahrValidation } from './shared-util-validator-steuerdaten';

describe('prepareSteuerjahrValidation', () => {
  it.each([
    [false, 2024, 2021],
    [true, 2024, 2025],
    [false, 2025, 2025],
    [true, 2025, undefined],
    [false, undefined, 2025],
  ] as const)(
    'should set steuerjahrControl validators and be valid[%s] if current Jahr %s is <= Gesuchjahr %s',
    (valid, current, gesuchjahr) => {
      vitest.useFakeTimers();
      const viewSig = signal({
        gesuch: {
          gesuchsperiode: { gesuchsjahr: { technischesJahr: gesuchjahr } },
        },
      } as any);
      const steuerjahrControl = new FormControl<string | undefined>(undefined, {
        nonNullable: true,
      });
      steuerjahrControl.setValue(current?.toString());
      TestBed.runInInjectionContext(() => {
        prepareSteuerjahrValidation(steuerjahrControl, viewSig).createEffect();
      });
      vitest.runOnlyPendingTimers();
      expect(steuerjahrControl.valid).toBe(valid);
    },
  );
});
