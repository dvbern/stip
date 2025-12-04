import { Injectable, computed, inject } from '@angular/core';
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
  Ausbildungsgang,
  FallDashboardItem,
  GesuchDashboardItem,
  GesuchService,
} from '@dv/shared/model/gesuch';
import { getNotificationTranslationKey } from '@dv/shared/model/nachricht';
import {
  getGesuchPermissions,
  getTranchePermissions,
  isNotReadonly,
} from '@dv/shared/model/permission-state';
import {
  CachedRemoteData,
  cachedPending,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';
import { dateFromMonthYearString } from '@dv/shared/util/validator-date';

type DashboardState = {
  dashboard: CachedRemoteData<FallDashboardItem>;
};

const initialState: DashboardState = {
  dashboard: initial(),
};

@Injectable()
export class DashboardStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private gesuchService = inject(GesuchService);
  private appType = inject(SharedModelCompileTimeConfig).appType;
  private permissionStore = inject(PermissionStore);

  dashboardViewSig = computed<SharedModelGsDashboardView | undefined>(() => {
    const fallDashboardItem = this.dashboard.data();
    if (!fallDashboardItem) {
      return undefined;
    }
    const activeAusbildungen: SharedModelGsAusbildungView[] = [];
    const inactiveAusbildungen: SharedModelGsAusbildungView[] = [];
    const rolesMap = this.permissionStore.rolesMapSig();

    fallDashboardItem.ausbildungDashboardItems?.forEach(
      ({ gesuchs, ...ausbildung }) => {
        const hasMoreThanOneGesuche = (gesuchs?.length ?? 0) > 1;
        const filteredGesuchs = !gesuchs
          ? []
          : (gesuchs.map(
              toGesuchDashboardItemView({
                appType: this.appType,
                gesuchs,
                rolesMap,
                fallItem: fallDashboardItem,
                isAusbildungActive: ausbildung.status === 'AKTIV',
                hasMoreThanOneGesuche,
              }),
            ) ?? []);

        const canEditAusbildung =
          !hasMoreThanOneGesuche &&
          filteredGesuchs[0]?.gesuchStatus === 'IN_BEARBEITUNG_GS';
        const canCurrentlyEditAusbildung = isNotReadonly(
          this.appType,
          rolesMap,
          fallDashboardItem.delegierung,
        );
        const alternativeBezeichnung = `${ausbildung.alternativeAusbildungsstaette} - ${ausbildung.alternativeAusbildungsgang}`;
        const getBezeichnung = (
          lang: 'De' | 'Fr',
          ausbildungsgang?: Ausbildungsgang,
        ) => {
          const name = ausbildungsgang?.ausbildungsstaette?.[`name${lang}`];
          const bezeichnung =
            ausbildungsgang?.abschluss?.[`bezeichnung${lang}`];
          return `${name} - ${bezeichnung}`;
        };

        (ausbildung.status !== 'AKTIV'
          ? inactiveAusbildungen
          : activeAusbildungen
        ).push({
          ...ausbildung,
          bezeichnungDe: ausbildung.ausbildungNichtGefunden
            ? alternativeBezeichnung
            : getBezeichnung('De', ausbildung.ausbildungsgang),
          bezeichnungFr: ausbildung.ausbildungNichtGefunden
            ? alternativeBezeichnung
            : getBezeichnung('Fr', ausbildung.ausbildungsgang),
          canDelete: canEditAusbildung && canCurrentlyEditAusbildung,
          ausbildungBegin: dateFromMonthYearString(ausbildung.ausbildungBegin),
          ausbildungEnd: dateFromMonthYearString(ausbildung.ausbildungEnd),
          gesuchs: filteredGesuchs,
        });
      },
    );

    return {
      fall: fallDashboardItem.fall,
      delegierung: fallDashboardItem.delegierung,
      canCreateAusbildung: isNotReadonly(
        this.appType,
        rolesMap,
        fallDashboardItem.delegierung,
      ),
      notifications: fallDashboardItem.notifications.map((notification) => ({
        ...notification,
        translationKey: getNotificationTranslationKey(notification),
      })),
      hasActiveAusbildungen: activeAusbildungen.length > 0,
      activeAusbildungen,
      inactiveAusbildungen,
    };
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

  loadSozialdienstDashboard$ = rxMethod<{ fallId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          dashboard: cachedPending(state.dashboard),
        }));
      }),
      switchMap((params) =>
        this.gesuchService
          .getSozialdienstMitarbeiterDashboard$(params)
          .pipe(
            handleApiResponse((dashboard) => patchState(this, { dashboard })),
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
    const canCurrentlyEditGesuch = isNotReadonly(
      appType,
      rolesMap,
      fallItem.delegierung,
    );
    const gesuchPermission = getGesuchPermissions(gesuch, appType, rolesMap);
    const aenderungPermission = gesuch.offeneAenderung
      ? getTranchePermissions(
          { gesuchTrancheToWorkWith: gesuch.offeneAenderung },
          appType,
          rolesMap,
        )
      : null;
    const canEdit =
      !!gesuchPermission.permissions.canWrite && canCurrentlyEditGesuch;

    return {
      ...gesuch,
      fallId: fallItem.fall.id,
      isActive: isAusbildungActive && isLastGesuch,
      isErstgesuch,
      canEdit,
      canDelete: canEdit && hasMoreThanOneGesuche && canCurrentlyEditGesuch,
      canCreateAenderung:
        (gesuch.gesuchStatus === 'STIPENDIENANSPRUCH' ||
          gesuch.gesuchStatus === 'KEIN_STIPENDIENANSPRUCH') &&
        !gesuch.offeneAenderung &&
        canCurrentlyEditGesuch,
      canDeleteAenderung:
        !!aenderungPermission?.permissions.canWrite && canCurrentlyEditGesuch,
      einreichefristAbgelaufen,
      reduzierterBeitrag,
      einreichefristDays,
      yearRange,
    };
  };
