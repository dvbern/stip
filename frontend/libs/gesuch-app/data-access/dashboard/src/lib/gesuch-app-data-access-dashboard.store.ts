import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { differenceInDays, endOfDay, format, isAfter } from 'date-fns';
import { pipe, switchMap, tap } from 'rxjs';

import { PermissionStore } from '@dv/shared/global/permission';
import {
  SharedModelGsAusbildungView,
  SharedModelGsDashboardView,
  SharedModelGsGesuchView,
} from '@dv/shared/model/ausbildung';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import { FallDashboardItem, GesuchService } from '@dv/shared/model/gesuch';
import {
  canCurrentlyUpdateGesuch,
  getGesuchPermissions,
} from '@dv/shared/model/permission-state';
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
  private appType = inject(SharedModelCompileTimeConfig).appType;
  private permissionStore = inject(PermissionStore);

  dashboardViewSig = computed<SharedModelGsDashboardView[] | undefined>(() => {
    const fallDashboardItems = this.dashboard.data();
    const activeAusbildungen: SharedModelGsAusbildungView[] = [];
    const inactiveAusbildungen: SharedModelGsAusbildungView[] = [];
    const rolesMap = this.permissionStore.rolesMapSig();

    fallDashboardItems?.forEach((item) =>
      item.ausbildungDashboardItems?.forEach(({ gesuchs, ...ausbildung }) => {
        const hasMoreThanOneGesuche = (gesuchs?.length ?? 0) > 1;
        const filteredGesuchs = !gesuchs
          ? []
          : (gesuchs.map((gesuch, index) => {
              const isErstgesuch = index === gesuchs.length - 1;
              const isLastGesuch = index === 0;
              const einreichefristAbgelaufen = isAfter(
                new Date(),
                endOfDay(
                  new Date(gesuch.gesuchsperiode.einreichefristReduziert),
                ),
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
              const canEdit = gesuch.gesuchStatus === 'IN_BEARBEITUNG_GS';
              const permissions = getGesuchPermissions(
                gesuch,
                this.appType,
                rolesMap,
              );

              return {
                ...gesuch,
                isActive: ausbildung.status === 'AKTIV' && isLastGesuch,
                isErstgesuch,
                canEdit,
                canDelete: canEdit && hasMoreThanOneGesuche,
                canCreateAenderung:
                  (gesuch.gesuchStatus == 'STIPENDIENANSPRUCH' ||
                    gesuch.gesuchStatus == 'KEIN_STIPENDIENANSPRUCH') &&
                  !gesuch.offeneAenderung &&
                  canCurrentlyUpdateGesuch(
                    permissions,
                    this.appType,
                    rolesMap,
                    item.delegierung,
                  ),
                einreichefristAbgelaufen,
                reduzierterBeitrag,
                einreichefristDays,
                yearRange,
              } satisfies SharedModelGsGesuchView;
            }) ?? []);

        (ausbildung.status !== 'AKTIV'
          ? inactiveAusbildungen
          : activeAusbildungen
        ).push({
          ...ausbildung,
          canDelete:
            !hasMoreThanOneGesuche &&
            filteredGesuchs[0]?.gesuchStatus === 'IN_BEARBEITUNG_GS',
          ausbildungBegin: dateFromMonthYearString(ausbildung.ausbildungBegin),
          ausbildungEnd: dateFromMonthYearString(ausbildung.ausbildungEnd),
          gesuchs: filteredGesuchs,
        });
      }),
    );

    return fallDashboardItems?.map((item) => ({
      fall: item.fall,
      delegierung: item.delegierung,
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
