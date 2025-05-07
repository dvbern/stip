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
import { RolesMap } from '@dv/shared/model/benutzer';
import { AppType, SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import {
  FallDashboardItem,
  GesuchDashboardItem,
  GesuchService,
} from '@dv/shared/model/gesuch';
import {
  canCreateAusbildung,
  canCurrentlyEdit,
} from '@dv/shared/model/permission-state';
import {
  CachedRemoteData,
  cachedPending,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';
import { dateFromMonthYearString } from '@dv/shared/util/validator-date';

type SozDashboardState = {
  dashboard: CachedRemoteData<FallDashboardItem[]>;
};

const initialState: SozDashboardState = {
  dashboard: initial(),
};

@Injectable()
export class SozDashboardStore extends signalStore(
  { protectedState: false },
  withState(initialState),
  withDevtools('SozDashboardStore'),
) {
  private gesuchService = inject(GesuchService);
  private appType = inject(SharedModelCompileTimeConfig).appType;
  private permissionStore = inject(PermissionStore);

  dashboardViewSig = computed<SharedModelGsDashboardView[] | undefined>(() => {
    const fallDashboardItems = this.dashboard.data();
    const activeAusbildungen: SharedModelGsAusbildungView[] = [];
    const inactiveAusbildungen: SharedModelGsAusbildungView[] = [];
    const rolesMap = this.permissionStore.rolesMapSig();

    fallDashboardItems?.forEach((fallItem) =>
      fallItem.ausbildungDashboardItems?.forEach(
        ({ gesuchs, ...ausbildung }) => {
          const hasMoreThanOneGesuche = (gesuchs?.length ?? 0) > 1;
          const filteredGesuchs = !gesuchs
            ? []
            : (gesuchs.map(
                toGesuchDashboardItemView({
                  appType: this.appType,
                  gesuchs,
                  rolesMap,
                  fallItem,
                  isAusbildungActive: ausbildung.status === 'AKTIV',
                  hasMoreThanOneGesuche,
                }),
              ) ?? []);

          const canEditAusbildung =
            !hasMoreThanOneGesuche &&
            filteredGesuchs[0]?.gesuchStatus === 'IN_BEARBEITUNG_GS';
          const canCurrentlyEditAusbildung = canCurrentlyEdit(
            this.appType,
            rolesMap,
            fallItem.delegierung,
          );

          (ausbildung.status !== 'AKTIV'
            ? inactiveAusbildungen
            : activeAusbildungen
          ).push({
            ...ausbildung,
            canDelete: canEditAusbildung && canCurrentlyEditAusbildung,
            ausbildungBegin: dateFromMonthYearString(
              ausbildung.ausbildungBegin,
            ),
            ausbildungEnd: dateFromMonthYearString(ausbildung.ausbildungEnd),
            gesuchs: filteredGesuchs,
          });
        },
      ),
    );

    return fallDashboardItems?.map((item) => ({
      fall: item.fall,
      delegierung: item.delegierung,
      canCreateAusbildung: canCreateAusbildung(
        this.appType,
        rolesMap,
        item.delegierung,
      ),
      notifications: item.notifications,
      hasActiveAusbildungen: true,
      activeAusbildungen,
      inactiveAusbildungen,
    }));
  });

  loadCachedSozDashboard$ = rxMethod<{ fallId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          dashboard: cachedPending(state.dashboard),
        }));
      }),
      switchMap((params) =>
        this.gesuchService
          .getSozMaDashboard$(params)
          .pipe(
            handleApiResponse((cachedSozDashboard) =>
              patchState(this, { dashboard: cachedSozDashboard }),
            ),
          ),
      ),
    ),
  );
}

const toGesuchDashboardItemView =
  (data: {
    fallItem: FallDashboardItem;
    appType: AppType;
    gesuchs: GesuchDashboardItem[];
    rolesMap: RolesMap;
    isAusbildungActive: boolean;
    hasMoreThanOneGesuche: boolean;
  }) =>
  (gesuch: GesuchDashboardItem, index: number): SharedModelGsGesuchView => {
    const {
      fallItem,
      appType,
      gesuchs,
      rolesMap,
      isAusbildungActive,
      hasMoreThanOneGesuche,
    } = data;
    const isErstgesuch = index === gesuchs.length - 1;
    const isLastGesuch = index === 0;
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
      format(Date.parse(gesuch.gesuchsperiode.gesuchsperiodeStart), 'yy'),
      format(Date.parse(gesuch.gesuchsperiode.gesuchsperiodeStopp), 'yy'),
    ].join('/');
    const canCurrentlyEditGesuch = canCurrentlyEdit(
      appType,
      rolesMap,
      fallItem.delegierung,
    );
    const canEdit =
      gesuch.gesuchStatus === 'IN_BEARBEITUNG_GS' && canCurrentlyEditGesuch;

    return {
      ...gesuch,
      isActive: isAusbildungActive && isLastGesuch,
      isErstgesuch,
      canEdit,
      canDelete: canEdit && hasMoreThanOneGesuche && canCurrentlyEditGesuch,
      canCreateAenderung:
        (gesuch.gesuchStatus == 'STIPENDIENANSPRUCH' ||
          gesuch.gesuchStatus == 'KEIN_STIPENDIENANSPRUCH') &&
        !gesuch.offeneAenderung &&
        canCurrentlyEditGesuch,
      einreichefristAbgelaufen,
      reduzierterBeitrag,
      einreichefristDays,
      yearRange,
    };
  };
