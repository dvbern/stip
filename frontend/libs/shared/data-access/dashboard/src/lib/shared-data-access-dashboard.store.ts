import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { differenceInDays, endOfDay, isAfter } from 'date-fns';
import { pipe, switchMap, tap } from 'rxjs';

import { PermissionStore } from '@dv/shared/global/permission';
import {
  SharedModelGsAusbildungView,
  SharedModelGsDashboardView,
  SharedModelGsGesuchView,
} from '@dv/shared/model/ausbildung';
import { RolesMap } from '@dv/shared/model/benutzer';
import {
  AppConfig,
  SharedModelCompileTimeConfig,
} from '@dv/shared/model/config';
import {
  Ausbildungsgang,
  FallDashboardItem,
  GesuchDashboardItem,
  GesuchService,
} from '@dv/shared/model/gesuch';
import {
  byAppConfig,
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
import {
  dateFromMonthYearString,
  getYearRangeFrom,
} from '@dv/shared/util/validator-date';

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
  private config = inject(SharedModelCompileTimeConfig);
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
                appConfig: this.config.app,
                gesuchs,
                rolesMap,
                fallItem: fallDashboardItem,
                isAusbildungActive: ausbildung.status === 'AKTIV',
                hasPendingAusbildungUnterbruchAntrag:
                  ausbildung.hasPendingAusbildungUnterbruchAntrag,
                hasMoreThanOneGesuche,
              }),
            ) ?? []);

        const canEditAusbildung =
          !hasMoreThanOneGesuche &&
          filteredGesuchs[0]?.gesuchStatus === 'IN_BEARBEITUNG_GS';
        const canCurrentlyEditAusbildung = isNotReadonly(
          this.config.app,
          rolesMap,
          fallDashboardItem.currentDelegierung,
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
      earliestActiveGesuchPeriodeStart:
        fallDashboardItem.earliestActiveGesuchPeriodeStart,
      currentDelegierung: fallDashboardItem.currentDelegierung,
      canCreateAusbildung: isNotReadonly(
        this.config.app,
        rolesMap,
        fallDashboardItem.currentDelegierung,
      ),
      hasActiveAusbildungen: activeAusbildungen.length > 0,
      activeAusbildungen,
      inactiveAusbildungen,
    };
  });

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  loadDashboard$(params: { fallId: string }) {
    return byAppConfig(this.config.app, {
      gesuchsteller: () => this.loadDashboardGS$(), //todo-KSTIP-3643: after merge of KSTIP-3676 consider changing backend to allways use fallid instead of logged in current user?
      // todo-KSTIP-3643: add after merge of KSTIP-3676
      // 'sozialdienst-app': () => this.loadDashboardSoz$(params),
      sachbearbeiter: () => {
        throw new Error('Not implemented for this AppType');
      },
    });
  }

  private loadDashboardGS$ = rxMethod<void>(
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

  private loadDashboardSoz$ = rxMethod<{ fallId: string }>(
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
    appConfig: AppConfig;
    gesuchs: GesuchDashboardItem[];
    rolesMap: RolesMap;
    isAusbildungActive: boolean;
    hasPendingAusbildungUnterbruchAntrag: boolean;
    hasMoreThanOneGesuche: boolean;
  }) =>
  (gesuch: GesuchDashboardItem, index: number): SharedModelGsGesuchView => {
    const {
      fallItem,
      appConfig,
      gesuchs,
      rolesMap,
      isAusbildungActive,
      hasPendingAusbildungUnterbruchAntrag,
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
    const { gesuchsperiodeStart, gesuchsperiodeStopp } = gesuch.gesuchsperiode;
    const yearRange = getYearRangeFrom(
      gesuchsperiodeStart,
      gesuchsperiodeStopp,
    );
    const canCurrentlyEditGesuch = isNotReadonly(
      appConfig,
      rolesMap,
      fallItem.currentDelegierung,
    );
    const gesuchPermission = getGesuchPermissions(gesuch, appConfig, rolesMap);
    const aenderungPermission = gesuch.offeneAenderung
      ? getTranchePermissions(
          { gesuchTrancheToWorkWith: gesuch.offeneAenderung },
          appConfig,
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
      canDeleteAenderung:
        !!aenderungPermission?.permissions.canWrite && canCurrentlyEditGesuch,
      canCreateAenderung: gesuch.canCreateAenderung && canCurrentlyEditGesuch,
      hasPendingAusbildungUnterbruchAntrag,
      einreichefristAbgelaufen,
      reduzierterBeitrag,
      einreichefristDays,
      yearRange,
    };
  };
