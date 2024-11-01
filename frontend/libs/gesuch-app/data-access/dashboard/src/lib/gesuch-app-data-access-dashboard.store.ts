import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { differenceInDays, endOfDay, format, isAfter } from 'date-fns';
import { pipe, switchMap, tap } from 'rxjs';

import {
  SharedModelGsAusbildungView,
  SharedModelGsDashboardView,
} from '@dv/shared/model/ausbildung';
import { FallDashboardItem, GesuchService } from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';
import { dateFromMonthYearString } from '@dv/shared/util/validator-date';

type DashboardState = {
  dashboard: CachedRemoteData<FallDashboardItem[]>;
};

const initialState: DashboardState = {
  dashboard: initial(),
};

@Injectable()
export class DashboardStore extends signalStore(
  { protectedState: false },
  withState(initialState),
  withDevtools('DashboardStore'),
) {
  private gesuchService = inject(GesuchService);

  dashboardViewSig = computed<SharedModelGsDashboardView[] | undefined>(() => {
    const fallDashboardItems = this.dashboard.data();
    const activeAusbildungen: SharedModelGsAusbildungView[] = [];
    const inactiveAusbildungen: SharedModelGsAusbildungView[] = [];
    fallDashboardItems?.forEach((item) =>
      item.ausbildungDashboardItems?.forEach((ausbildung) => {
        const gesuchs =
          ausbildung.gesuchs?.map((gesuch) => {
            const einreichefristAbgelaufen = isAfter(
              new Date(),
              endOfDay(new Date(gesuch.gesuchsperiode.einreichefristReduziert)),
            );
            const reduzierterBeitrag = isAfter(
              new Date(),
              endOfDay(new Date(gesuch.gesuchsperiode.einreichefristNormal)),
            );
            const einreichefristDays = differenceInDays(
              endOfDay(
                new Date(
                  reduzierterBeitrag
                    ? gesuch.gesuchsperiode.einreichefristReduziert
                    : gesuch.gesuchsperiode.einreichefristNormal,
                ),
              ),
              new Date(),
            );
            const yearRange = [
              format(
                Date.parse(gesuch.gesuchsperiode.gesuchsperiodeStart),
                'yy',
              ),
              format(
                Date.parse(gesuch.gesuchsperiode.gesuchsperiodeStopp),
                'yy',
              ),
            ].join('/');

            return {
              ...gesuch,
              einreichefristAbgelaufen,
              reduzierterBeitrag,
              einreichefristDays,
              yearRange,
            };
          }) ?? [];

        (ausbildung.status !== 'AKTIV'
          ? inactiveAusbildungen
          : activeAusbildungen
        ).push({
          ...ausbildung,
          ausbildungBegin: dateFromMonthYearString(ausbildung.ausbildungBegin),
          ausbildungEnd: dateFromMonthYearString(ausbildung.ausbildungEnd),
          gesuchs,
        });
      }),
    );

    return fallDashboardItems?.map((item) => ({
      fall: item.fall,
      notifications: item.notifications,
      hasActiveAusbildungen: true,
      activeAusbildungen,
      inactiveAusbildungen,
    }));
  });

  loadDashboard$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          dashboard: cachedPending(state.dashboard),
        }));
      }),
      switchMap(() =>
        this.gesuchService
          .getGsDashboard$()
          .pipe(
            handleApiResponse((dashboard) => patchState(this, { dashboard })),
          ),
      ),
    ),
  );
}
