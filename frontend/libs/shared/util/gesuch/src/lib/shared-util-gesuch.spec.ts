import { Signal, signal } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { TestBed } from '@angular/core/testing';

import {
  getLatestGesuchIdFromGesuch$,
  getLatestTrancheIdFromGesuch$,
  getLatestTrancheIdFromGesuchOnUpdate$,
} from './shared-util-gesuch';

type TestParam<Fn extends (arg: Signal<any>) => unknown> = ReturnType<
  Parameters<Fn>[0]
>;

describe('getLatestGesuchId', () => {
  beforeEach(() => {
    jest.useFakeTimers();
  });

  it('should emit the gesuch id once it is available', () => {
    const gesuchId1 = '123';
    const gesuchId2 = '456';
    const viewSig = signal<TestParam<typeof getLatestGesuchIdFromGesuch$>>({
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

  it('should emit the tranche id once it is available', () => {
    const trancheId1 = '123';
    const trancheId2 = '456';
    const viewSig = signal<TestParam<typeof getLatestTrancheIdFromGesuch$>>({
      trancheId: null,
    });

    TestBed.runInInjectionContext(() => {
      const gesuchIdSig = toSignal(getLatestTrancheIdFromGesuch$(viewSig));
      jest.runOnlyPendingTimers();

      expect(gesuchIdSig()).toBeUndefined();

      viewSig.set({ trancheId: trancheId1 });
      jest.runOnlyPendingTimers();
      expect(gesuchIdSig()).toBe(trancheId1);

      viewSig.set({ trancheId: null });
      jest.runOnlyPendingTimers();
      expect(gesuchIdSig()).toBe(trancheId1);

      viewSig.set({ trancheId: trancheId2 });
      jest.runOnlyPendingTimers();
      expect(gesuchIdSig()).toBe(trancheId2);
    });
  });

  it('should emit the tranche id once it is available and then after the gesuch has been updated', () => {
    const trancheId1 = '123';
    const trancheId2 = '456';
    const viewSig = signal<
      TestParam<typeof getLatestTrancheIdFromGesuchOnUpdate$>
    >({
      lastUpdate: null,
    });

    TestBed.runInInjectionContext(() => {
      const gesuchIdSig = toSignal(
        getLatestTrancheIdFromGesuchOnUpdate$(viewSig),
      );
      jest.runOnlyPendingTimers();

      expect(gesuchIdSig()).toBeUndefined();

      viewSig.set({
        trancheId: trancheId1,
        lastUpdate: '2024-01-07T08:00:00.000Z',
      });
      jest.runOnlyPendingTimers();
      expect(gesuchIdSig()).toBe(trancheId1);

      viewSig.set({
        trancheId: null,
        lastUpdate: '2024-01-07T08:00:00.000Z',
      });
      jest.runOnlyPendingTimers();
      expect(gesuchIdSig()).toBe(trancheId1);

      viewSig.set({
        trancheId: trancheId1,
        lastUpdate: '2024-01-07T09:00:00.000Z',
      });
      jest.runOnlyPendingTimers();
      expect(gesuchIdSig()).toBe(trancheId1);

      viewSig.set({
        trancheId: trancheId2,
        lastUpdate: '2024-01-07T09:00:00.000Z',
      });
      jest.runOnlyPendingTimers();
      expect(gesuchIdSig()).toBe(trancheId2);
    });
  });
});
