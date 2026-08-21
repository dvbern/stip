import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  computed,
  effect,
  inject,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatTabsModule } from '@angular/material/tabs';
import { MatTooltipModule } from '@angular/material/tooltip';
import {
  ActivatedRoute,
  Router,
  RouterLink,
  RouterOutlet,
} from '@angular/router';
import { Store } from '@ngrx/store';
import { filter, map, startWith } from 'rxjs';

import { DarlehenStore } from '@dv/shared/data-access/darlehen';
import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import {
  selectRouteGesuchId,
  selectRouteTrancheId,
  selectSharedDataAccessGesuchCache,
} from '@dv/shared/data-access/gesuch';
import { GesuchHeaderStore } from '@dv/shared/data-access/gesuch-header';
import {
  SharedModelCompileTimeConfig,
  onlyBusinessAppConfig,
} from '@dv/shared/model/config';
import { FreiwilligDarlehen, getTrancheRoute } from '@dv/shared/model/gesuch';
import { TRANCHE } from '@dv/shared/model/gesuch-form';
import { byAppConfig } from '@dv/shared/model/permission-state';
import {
  createUrlChecksSig,
  urlAfterNavigationEnd,
} from '@dv/shared/model/router';
import { isDefined } from '@dv/shared/model/type-util';
import {
  DarlehenCompleteStates,
  darlehenStatusMapping,
} from '@dv/shared/model/ui';
import { notGesuchRoute } from '@dv/shared/model/ui-constants';
import { SharedPatternGesuchInfoBarComponent } from '@dv/shared/pattern/gesuch-info-bar';
import { SharedPatternInfoBarActionsComponent } from '@dv/shared/pattern/info-bar-actions';
import { SharedUiAdvTranslocoDirective } from '@dv/shared/ui/adv-transloco-directive';
import {
  SharedUiIfSachbearbeiterDirective,
  SharedUiIfTypeOneOfDirective,
} from '@dv/shared/ui/if-app-type';
import { SharedUiVersionenMenuComponent } from '@dv/shared/ui/versionen-menu';
import { TabNavItem, getQueryParamValueSig } from '@dv/shared/util/navigation';
import { getYearRangeFrom } from '@dv/shared/util/validator-date';
import { isInOneOfGivenStatus } from '@dv/shared/util-fn/gesuch-util';

@Component({
  selector: 'dv-shared-feature-gesuch-layout',
  imports: [
    CommonModule,
    RouterOutlet,
    RouterLink,
    MatTabsModule,
    MatMenuModule,
    MatTooltipModule,
    MatIconModule,
    SharedPatternGesuchInfoBarComponent,
    SharedUiVersionenMenuComponent,
    SharedUiAdvTranslocoDirective,
    SharedPatternInfoBarActionsComponent,
    SharedUiIfSachbearbeiterDirective,
    SharedUiIfTypeOneOfDirective,
  ],
  templateUrl: './shared-feature-gesuch-layout.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchLayoutComponent {
  @HostBinding('class') klass = 'tw:dv-pass-height tw:dv-container';

  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private store = inject(Store);
  private gesuchHeaderStore = inject(GesuchHeaderStore);
  private einreichenStore = inject(EinreichenStore);

  config = inject(SharedModelCompileTimeConfig);
  businessAppConfig = onlyBusinessAppConfig(this.config.app);
  darlehenStore = inject(DarlehenStore);
  gesuchIdSig = this.store.selectSignal(selectRouteGesuchId);
  trancheIdSig = this.store.selectSignal(selectRouteTrancheId);

  berechnungIdSig = getQueryParamValueSig(this.route, 'berechnungId');

  private originStepSig = getQueryParamValueSig(this.route, 'originStep');
  originOrTrancheStepSig = computed(() => {
    return this.originStepSig() || TRANCHE.route;
  });
  tabRouteSegmentsSig = computed(() => {
    const originOrTrancheStep = this.originOrTrancheStepSig();
    return originOrTrancheStep.split('/').filter(Boolean);
  });

  routeChecksSig = createUrlChecksSig(
    this.router,
    `infos`,
    'darlehen',
    `${getTrancheRoute('tranche')}`,
    `${getTrancheRoute('aenderung')}`,
    `${getTrancheRoute('initial')}`,
    `${getTrancheRoute('eingereicht')}`,
  );

  isGesuchRouteSig = computed(() => {
    const routes = this.routeChecksSig();
    return !notGesuchRoute.some((route) => routes.matched.includes(route));
  });

  routeUrlSig = toSignal(
    urlAfterNavigationEnd(this.router).pipe(
      map(() => this.router.routerState.snapshot.url),
      startWith(this.router.routerState.snapshot.url),
    ),
  );

  headerViewSig = this.gesuchHeaderStore.viewSig;

  gesuchInfoDataSig = computed(() => {
    const { gesuchInfo, latestVerfuegtAt } = this.headerViewSig();
    if (!gesuchInfo) {
      return;
    }

    return {
      name: `${gesuchInfo.piaVorname} ${gesuchInfo.piaNachname}`,
      ausbildungsjahr: getYearRangeFrom(
        gesuchInfo.startDate,
        gesuchInfo.endDate,
      ),
      verfuegtAt: isInOneOfGivenStatus(gesuchInfo.state.gesuchStatus, [
        'STIPENDIENANSPRUCH',
        'KEIN_STIPENDIENANSPRUCH',
      ])
        ? latestVerfuegtAt
        : null,
      fallNummer: gesuchInfo.fallNummer,
      gesuchNummer: gesuchInfo.gesuchNummer,
      status: gesuchInfo.state.gesuchStatus,
      cannotFreigeben:
        !gesuchInfo.state.canFreigeben &&
        gesuchInfo.state.gesuchStatus === 'IN_FREIGABE',
    };
  });

  tabsSig = computed<TabNavItem[]>(() => {
    const gesuchId = this.gesuchIdSig();
    const trancheId = this.trancheIdSig();
    const { latestVerfuegungId, canGetBerechnung } = this.headerViewSig();
    const activePath = this.routeUrlSig();
    const berechnungId = this.berechnungIdSig();
    const originOrTrancheStep = this.originOrTrancheStepSig();
    const routes = this.routeChecksSig();
    const isGesuchRoute = this.isGesuchRouteSig();
    const isIntitial = routes.isInitial;
    const isEingereicht = routes.isEingereicht;
    const isAenderung = routes.isAenderung;

    const trancheTyp = isIntitial
      ? 'initial'
      : isEingereicht
        ? 'eingereicht'
        : isAenderung
          ? 'aenderung'
          : 'tranche';

    if (!isGesuchRoute) {
      return [];
    }

    const tabSegments = originOrTrancheStep.split('/').filter(Boolean);

    const gesuchTab = {
      active: !activePath?.includes('/verfuegung'),
      route: ['/gesuch', ...tabSegments, gesuchId, trancheTyp, trancheId],
      queryParams: { berechnungId, originStep: originOrTrancheStep },
      key: 'formular' as const,
    };

    const appTypeBasedQueryParams =
      this.config.app.view === 'gesuchsteller'
        ? {
            latestVerfuegungId,
          }
        : {};

    const verfuegungTab = {
      active: activePath?.includes('/verfuegung'),
      route: ['/gesuch/verfuegung', gesuchId, trancheTyp, trancheId],
      queryParams: {
        ...appTypeBasedQueryParams,
        berechnungId,
        originStep: originOrTrancheStep,
      },
      key: 'verfuegung' as const,
    };

    if (canGetBerechnung) {
      return [gesuchTab, verfuegungTab];
    }

    return [gesuchTab];
  });

  private gesuchUpdatedSig = toSignal(
    this.store.select(selectSharedDataAccessGesuchCache).pipe(
      map(({ gesuch }) => gesuch),
      filter(isDefined),
    ),
  );

  canViewBerechnungSig = computed(() => {
    const canViewBerechnung =
      this.gesuchHeaderStore.viewSig()?.canGetBerechnung;

    return canViewBerechnung;
  });
  isBeschwerdeHaengigSig = computed(() => {
    const beschwerdeHaengig =
      this.gesuchHeaderStore.viewSig()?.gesuchInfo?.state.beschwerdeHaengig;
    return beschwerdeHaengig;
  });
  tranchenSig = this.gesuchHeaderStore.getRelativeTranchenViewSig(
    this.gesuchIdSig,
  );

  firstAenderungSig = computed(() => {
    const aenderungen = this.headerViewSig().aenderungs;
    const offeneAenderung = aenderungen?.offen;
    const eingereichteAenderung = aenderungen?.eingereicht;
    const akzeptierteAenderungen = aenderungen?.akzeptiert;
    const manuelleAenderungen = aenderungen?.manuell;
    const abgelehnteAenderungen = aenderungen?.abgelehnt;

    const allAenderungen = [
      ...(offeneAenderung ? [offeneAenderung] : []),
      ...(eingereichteAenderung ? [eingereichteAenderung] : []),
      ...(akzeptierteAenderungen ?? []),
      ...(manuelleAenderungen ?? []),
      ...(abgelehnteAenderungen ?? []),
    ];

    return allAenderungen.length > 0 ? allAenderungen[0] : undefined;
  });

  firstDarlehenIdSig = computed(() => {
    const darlehen = this.darlehenStore.darlehenListViewSig().list ?? [];

    const byType = darlehen.reduce(
      (acc, darlehen) => {
        if (!darlehen.status) {
          return acc;
        }

        const statusKey = darlehenStatusMapping[darlehen.status];

        if (!acc[statusKey]) {
          acc[statusKey] = [];
        }

        acc[statusKey].push(darlehen);
        return acc;
      },
      {} as Record<DarlehenCompleteStates, FreiwilligDarlehen[]>,
    );

    const orderedDarlehen = [
      ...(byType.open ?? []),
      ...(byType.accepted ?? []),
      ...(byType.rejected ?? []),
    ];

    return orderedDarlehen.length > 0 ? orderedDarlehen[0].id : undefined;
  });

  constructor() {
    effect(() => {
      const gesuchId = this.gesuchIdSig();
      const gesuchTrancheId = this.trancheIdSig();
      this.gesuchUpdatedSig();
      if (gesuchId && gesuchTrancheId) {
        this.gesuchHeaderStore.loadHeader$({ gesuchId });
        this.einreichenStore.validateEinreichen$({ gesuchTrancheId });
      }
    });

    effect(() => {
      const { gesuchInfo } = this.headerViewSig();
      this.gesuchUpdatedSig();
      if (gesuchInfo?.fallId) {
        byAppConfig(this.config.app, {
          gesuchsteller: () =>
            this.darlehenStore.getAllDarlehenGs$({ fallId: gesuchInfo.fallId }),
          sachbearbeiter: () =>
            this.darlehenStore.getAllDarlehen$({ fallId: gesuchInfo.fallId }),
        });
      }
    });
  }
}
