import { signal } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { TestBed } from '@angular/core/testing';

import {
  getLatestGesuchIdFromGesuch$,
  getLatestGesuchIdFromGesuchOnUpdate$,
} from './shared-util-gesuch';

describe('getLatestGesuchId', () => {
  beforeEach(() => {
    jest.useFakeTimers();
  });

  it('should emit the gesuch id once it is available', () => {
    const gesuchId1 = '123';
    const gesuchId2 = '456';
    const viewSig = signal<any>({
      gesuch: null,
    });

    TestBed.runInInjectionContext(() => {
      const gesuchIdSig = toSignal(getLatestGesuchIdFromGesuch$(viewSig));
      jest.runOnlyPendingTimers();

      expect(gesuchIdSig()).toBeUndefined();

      viewSig.set({ gesuch: { id: gesuchId1 } });
      jest.runOnlyPendingTimers();
      expect(gesuchIdSig()).toBe(gesuchId1);

      viewSig.set({ gesuch: null });
      jest.runOnlyPendingTimers();
      expect(gesuchIdSig()).toBe(gesuchId1);

      viewSig.set({ gesuch: { id: gesuchId2 } });
      jest.runOnlyPendingTimers();
      expect(gesuchIdSig()).toBe(gesuchId2);
    });
  });

  it('should emit the gesuch id once it is available and then after the gesuch has been updated', () => {
    const gesuchId1 = '123';
    const gesuchId2 = '456';
    const viewSig = signal<any>({
      gesuch: null,
      lastUpdate: null,
    });

    TestBed.runInInjectionContext(() => {
      const gesuchIdSig = toSignal(
        getLatestGesuchIdFromGesuchOnUpdate$(viewSig),
      );
      jest.runOnlyPendingTimers();

      expect(gesuchIdSig()).toBeUndefined();

      viewSig.set({
        gesuch: { id: gesuchId1 },
        lastUpdate: '2024-01-07T08:00:00.000Z',
      });
      jest.runOnlyPendingTimers();
      expect(gesuchIdSig()).toBe(gesuchId1);

      viewSig.set({
        gesuch: { id: null },
        lastUpdate: '2024-01-07T08:00:00.000Z',
      });
      jest.runOnlyPendingTimers();
      expect(gesuchIdSig()).toBe(gesuchId1);

      viewSig.set({
        gesuch: { id: gesuchId1 },
        lastUpdate: '2024-01-07T09:00:00.000Z',
      });
      jest.runOnlyPendingTimers();
      expect(gesuchIdSig()).toBe(gesuchId1);

      viewSig.set({
        gesuch: { id: gesuchId2 },
        lastUpdate: '2024-01-07T09:00:00.000Z',
      });
      jest.runOnlyPendingTimers();
      expect(gesuchIdSig()).toBe(gesuchId2);
    });
  });
});
