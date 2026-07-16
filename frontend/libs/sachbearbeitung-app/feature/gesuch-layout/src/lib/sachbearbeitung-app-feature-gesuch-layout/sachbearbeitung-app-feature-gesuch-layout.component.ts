import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  HostBinding,
  Injector,
  Signal,
  computed,
  effect,
  inject,
  runInInjectionContext,
  signal,
} from '@angular/core';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { MatDialog } from '@angular/material/dialog';
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
import { TranslocoDirective } from '@jsverse/transloco';
import { Store } from '@ngrx/store';
import { format } from 'date-fns';
import { filter, firstValueFrom, map, startWith } from 'rxjs';

import { SachbearbeitungAppTranslationKey } from '@dv/sachbearbeitung-app/assets/i18n';
import { GesuchStore } from '@dv/sachbearbeitung-app/data-access/gesuch';
import { SachbearbeitungAppUiGrundAuswahlDialogComponent } from '@dv/sachbearbeitung-app/ui/grund-auswahl-dialog';
import {
  SharedTranslationKey,
  translatableShared,
} from '@dv/shared/assets/i18n';
import { selectSharedDataAccessConfigsView } from '@dv/shared/data-access/config';
import { DarlehenStore } from '@dv/shared/data-access/darlehen';
import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import {
  SharedDataAccessGesuchEvents,
  selectRouteGesuchId,
  selectRouteTrancheId,
  selectSharedDataAccessGesuchCache,
} from '@dv/shared/data-access/gesuch';
import {
  AenderungChangeState,
  GesuchAenderungStore,
} from '@dv/shared/data-access/gesuch-aenderung';
import { GesuchHeaderStore } from '@dv/shared/data-access/gesuch-header';
import { SharedDialogTrancheErstellenComponent } from '@dv/shared/dialog/tranche-erstellen';
import { GlobalNotificationStore } from '@dv/shared/global/notification';
import { PermissionStore } from '@dv/shared/global/permission';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import {
  FreiwilligDarlehen,
  GesuchHeader,
  InBearbeitungSbReason,
  getTrancheRoute,
} from '@dv/shared/model/gesuch';
import { TRANCHE } from '@dv/shared/model/gesuch-form';
import { getGesuchPermissions } from '@dv/shared/model/permission-state';
import { urlAfterNavigationEnd } from '@dv/shared/model/router';
import {
  assertUnreachable,
  isDefined,
  lowercased,
} from '@dv/shared/model/type-util';
import {
  DarlehenCompleteStates,
  darlehenStatusMapping,
} from '@dv/shared/model/ui';
import {
  hideAktionenRoutes,
  notGesuchRoute,
} from '@dv/shared/model/ui-constants';
import { SharedPatternGesuchInfoBarComponent } from '@dv/shared/pattern/gesuch-info-bar';
import { SharedPatternGlobalHeaderPartsDirective } from '@dv/shared/pattern/global-header';
import { SharedUiKommentarDialogComponent } from '@dv/shared/ui/kommentar-dialog';
import { SharedUiVersionenMenuComponent } from '@dv/shared/ui/versionen-menu';
import {
  StatusUebergaengeMap,
  StatusUebergaengeOptions,
  StatusUebergang,
} from '@dv/shared/util/gesuch';
import { TabNavItem, getQueryParamValueSig } from '@dv/shared/util/navigation';
import { isPending } from '@dv/shared/util/remote-data';
import type { ExportView } from '@dv/shared/util-data-access/export-tranche';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-gesuch-layout',
  imports: [
    CommonModule,
    RouterOutlet,
    RouterLink,
    MatTabsModule,
    MatMenuModule,
    MatTooltipModule,
    SharedPatternGesuchInfoBarComponent,
    SharedPatternGlobalHeaderPartsDirective,
    TranslocoDirective,
    SharedUiVersionenMenuComponent,
    MatIconModule,
  ],
  templateUrl: './sachbearbeitung-app-feature-gesuch-layout.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureGesuchLayoutComponent {
  @HostBinding('class') klass = 'tw:px-6 tw:dv-pass-height';

  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private store = inject(Store);
  private gesuchHeaderStore = inject(GesuchHeaderStore);
  private permissionStore = inject(PermissionStore);
  private einreichenStore = inject(EinreichenStore);
  private config = inject(SharedModelCompileTimeConfig);
  private destroyRef = inject(DestroyRef);
  private dialog = inject(MatDialog);
  private injector = inject(Injector);
  private globalNotificationStore = inject(GlobalNotificationStore);
  private gesuchStore = inject(GesuchStore);
  private gesuchAenderungStore = inject(GesuchAenderungStore);
  private deploymentConfigSig = this.store.selectSignal(
    selectSharedDataAccessConfigsView,
  );

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

  routeUrlSig = toSignal(
    urlAfterNavigationEnd(this.router).pipe(
      map(() => this.router.routerState.snapshot.url),
      startWith(this.router.routerState.snapshot.url),
    ),
  );
  isActionRouteSig = computed(() => {
    const url = this.routeUrlSig();
    return !hideAktionenRoutes.some(
      (route) => url?.includes(`/${route}/`) || this.berechnungIdSig(),
    );
  });
  isGesuchRouteSig = computed(() => {
    const url = this.routeUrlSig();
    return !notGesuchRoute.some((route) => url?.includes(`/${route}/`));
  });
  isTrancheRouteSig = computed(() => {
    const url = this.routeUrlSig();
    return url?.includes(`/${getTrancheRoute('tranche')}/`);
  });
  isInfosRouteSig = computed(() => {
    const url = this.routeUrlSig();
    return url?.includes('/infos/');
  });
  isDarlehenRouteSig = computed(() => {
    const url = this.routeUrlSig();
    return url?.includes('/darlehen/');
  });

  isAenderungRouteSig = toSignal(
    urlAfterNavigationEnd(this.router).pipe(
      map((url) => url.includes(`/${getTrancheRoute('aenderung')}/`)),
    ),
  );
  isInitialRouteSig = toSignal(
    urlAfterNavigationEnd(this.router).pipe(
      map((url) => url.includes(`/${getTrancheRoute('initial')}/`)),
    ),
  );
  isEingereichtRouteSig = toSignal(
    urlAfterNavigationEnd(this.router).pipe(
      map((url) => url.includes(`/${getTrancheRoute('eingereicht')}/`)),
    ),
  );

  headerViewSig: Signal<{ isLoading: boolean } & Partial<GesuchHeader>> =
    this.gesuchHeaderStore.viewSig;

  gesuchInfoDataSig = computed(() => {
    const info = this.headerViewSig().gesuchInfo;
    if (!info) {
      return;
    }

    return {
      name: `${info.piaVorname} ${info.piaNachname}`,
      fallNummer: info.fallNummer,
      gesuchNummer: info.gesuchNummer,
      status: info.state.gesuchStatus,
      cannotFreigeben:
        !info.state.canFreigeben && info.state.gesuchStatus === 'IN_FREIGABE',
    };
  });

  tabsSig = computed<TabNavItem[]>(() => {
    const gesuchId = this.gesuchIdSig();
    const trancheId = this.trancheIdSig();
    const { gesuchInfo } = this.headerViewSig();
    const activePath = this.routeUrlSig();
    const berechnungId = this.berechnungIdSig();
    const originOrTrancheStep = this.originOrTrancheStepSig();
    const isIntitial = this.isInitialRouteSig();
    const isEingereicht = this.isEingereichtRouteSig();
    const isAenderung = this.isAenderungRouteSig();

    const trancheTyp = isIntitial
      ? 'initial'
      : isEingereicht
        ? 'eingereicht'
        : isAenderung
          ? 'aenderung'
          : 'tranche';

    if (!this.isGesuchRouteSig()) {
      return [];
    }

    const tabSegments = originOrTrancheStep.split('/').filter(Boolean);

    const gesuchTab = {
      active: !activePath?.includes('/verfuegung'),
      route: ['/gesuch', ...tabSegments, gesuchId, trancheTyp, trancheId],
      queryParams: { berechnungId, originStep: originOrTrancheStep },
      key: 'formular',
    };

    const verfuegungTab = {
      active: activePath?.includes('/verfuegung'),
      route: ['/gesuch/verfuegung', gesuchId, trancheTyp, trancheId],
      queryParams: { berechnungId, originStep: originOrTrancheStep },
      key: 'verfuegung',
    };

    if (gesuchInfo?.state.canGetBerechnung) {
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
      this.gesuchHeaderStore.viewSig()?.gesuchInfo?.state.canGetBerechnung;

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
    const akzeptierteAenderungen = aenderungen?.akzeptiert;
    const manuelleAenderungen = aenderungen?.manuell;
    const abgelehnteAenderungen = aenderungen?.abgelehnt;

    const allAenderungen = [
      ...(offeneAenderung ? [offeneAenderung] : []),
      ...(akzeptierteAenderungen ?? []),
      ...(manuelleAenderungen ?? []),
      ...(abgelehnteAenderungen ?? []),
    ];

    return allAenderungen.length > 0 ? allAenderungen[0] : undefined;
  });

  firstDarlehenIdSig = computed(() => {
    const darlehen = this.darlehenStore.darlehenListSbViewSig().list ?? [];

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

  isLoadingSig = computed(() => {
    return (
      isPending(this.gesuchHeaderStore.header()) ||
      isPending(this.gesuchStore.lastStatusChange())
    );
  });
  isAenderungUpdatingSig = computed(() => {
    return (
      this.isLoadingSig() ||
      isPending(this.gesuchAenderungStore.cachedGesuchAenderung())
    );
  });
  isExportingSig = signal(false);
  private gesuchCacheSig = this.store.selectSignal(
    selectSharedDataAccessGesuchCache,
  );
  aenderungActionsSig = computed(() => {
    const gesuchId = this.gesuchIdSig();
    const tranche = this.gesuchCacheSig().gesuch?.gesuchTrancheToWorkWith;
    const hasValidationErrors =
      !!this.einreichenStore.einreichenValidationResult().data?.validationErrors
        ?.length;

    const isVisible =
      !!gesuchId &&
      !!tranche &&
      tranche.typ === 'AENDERUNG' &&
      tranche.status === 'UEBERPRUEFEN';

    return {
      isVisible,
      gesuchId,
      trancheId: tranche?.id,
      hasValidationErrors,
    };
  });

  exportValuesSig = computed<ExportView | undefined>(() => {
    const { gesuch, isEditingAenderung } = this.gesuchCacheSig();
    const tranche = gesuch?.gesuchTrancheToWorkWith;
    const periode = gesuch?.gesuchsperiode;

    if (!gesuch || !tranche || !periode || !isDefined(isEditingAenderung)) {
      return undefined;
    }

    return {
      gesuch,
      tranche,
      isEditingAenderung,
      sachbearbeiter: gesuch.bearbeiter,
      periode: {
        bezeichnungDe: periode.bezeichnungDe,
        bezeichnungFr: periode.bezeichnungFr,
        year: format(Date.parse(periode.gesuchsperiodeStart), 'yy'),
        einreichefrist: periode.einreichefristNormal,
      },
    };
  });

  constructor() {
    effect(() => {
      const gesuchId = this.gesuchIdSig();
      const gesuchTrancheId = this.trancheIdSig();
      this.gesuchUpdatedSig();
      if (gesuchId && gesuchTrancheId) {
        this.darlehenStore.getAllDarlehenSb$({ gesuchId });
        this.gesuchHeaderStore.loadHeader$({ gesuchId });
        this.einreichenStore.validateEinreichen$({ gesuchTrancheId });
      }
    });
  }

  availableTrancheInteractionSig = computed(() => {
    const rolesMap = this.permissionStore.rolesMapSig();
    const gesuchStatus =
      this.gesuchHeaderStore.viewSig()?.gesuchInfo?.state.gesuchStatus;

    if (gesuchStatus === 'IN_BEARBEITUNG_SB' && rolesMap.V0_Sachbearbeiter) {
      return 'CREATE_TRANCHE';
    }

    return null;
  });

  statusUebergaengeOptionsSig = computed(() => {
    const rolesMap = this.permissionStore.rolesMapSig();
    const validations =
      this.einreichenStore.validationViewSig().invalidFormularProps.validations;

    const {
      gesuchStatus,
      canFreigeben,
      canTriggerManuellPruefen,
      canBearbeitungAbschliessen,
      inBearbeitungSbReason,
      canSBInitAenderung,
    } = this.gesuchHeaderStore.viewSig()?.gesuchInfo?.state ?? {};

    if (!gesuchStatus) {
      return {
        list: [],
        isNotEmpty: false,
      };
    }

    const { permissions } = getGesuchPermissions(
      { gesuchStatus },
      this.config.appType,
      rolesMap,
    );

    const hasValidationErrors = !!validations.errors?.length;
    const hasValidationWarnings = !!validations.warnings?.length;

    const statusAbhaengigUebergange = StatusUebergaengeMap[gesuchStatus] ?? [];

    const flagAbhaengigUebergange: StatusUebergang[] = [];
    if (canSBInitAenderung) {
      flagAbhaengigUebergange.push('BEREIT_FUER_BEARBEITUNG_AS_AENDERUNG');
    }
    if (canTriggerManuellPruefen) {
      flagAbhaengigUebergange.push('STATUS_PRUEFUNG_AUSLOESEN');
    }

    const list = [...statusAbhaengigUebergange, ...flagAbhaengigUebergange]
      .filter(isDefined)
      .map((status) => ({
        ...StatusUebergaengeOptions[status]({
          permissions,
          canFreigeben: !!canFreigeben,
          hasAcceptedAllDokuments: !!canBearbeitungAbschliessen,
          isInvalid: hasValidationErrors || hasValidationWarnings,
        }),
        name: getUebergangName(status, inBearbeitungSbReason),
      }))
      .filter((uebergang) =>
        uebergang.allowedFor.some((role) => rolesMap[role]),
      );

    return {
      list,
      isNotEmpty: !!list?.length,
    };
  });

  actionMenuOptionsSig = computed(() => {
    const statusUebergaenge = this.statusUebergaengeOptionsSig();
    const availableTrancheInteraction = this.availableTrancheInteractionSig();
    const canExport = !!this.exportValuesSig();
    const showGesuchActions =
      this.isGesuchRouteSig() &&
      !this.isEingereichtRouteSig() &&
      !this.isInitialRouteSig();
    const showAenderungActions = !!this.isAenderungRouteSig();
    const aenderungActions = this.aenderungActionsSig();

    const hasAenderungMenuActions =
      showAenderungActions &&
      aenderungActions.isVisible &&
      !!aenderungActions.gesuchId &&
      !!aenderungActions.trancheId;
    const hasGesuchMenuActions =
      showGesuchActions &&
      (availableTrancheInteraction || statusUebergaenge.isNotEmpty);
    const hasWorkflowActions = hasAenderungMenuActions || hasGesuchMenuActions;
    const isActionMenuDisabled =
      (!hasWorkflowActions && !canExport) ||
      this.isLoadingSig() ||
      this.isExportingSig();

    return {
      statusUebergaenge,
      availableTrancheInteraction,
      canExport,
      aenderungActions,
      hasAenderungMenuActions,
      hasGesuchMenuActions,
      hasWorkflowActions,
      isActionMenuDisabled,
    };
  });

  setStatusUebergang(
    nextStatus: StatusUebergang,
    gesuchId?: string,
    gesuchTrancheId?: string,
  ) {
    if (!gesuchId || !gesuchTrancheId) {
      return;
    }

    switch (nextStatus) {
      case 'SET_TO_BEARBEITUNG':
        this.gesuchStore.setStatus$[nextStatus]({
          gesuchTrancheId,
          onSuccess: () => {
            this.einreichenStore.validateSteps$({ gesuchTrancheId });
          },
        });
        break;
      case 'ANSPRUCH_PRUEFEN':
      case 'BEARBEITUNG_ABSCHLIESSEN':
      case 'STATUS_PRUEFUNG_AUSLOESEN':
        this.gesuchStore.setStatus$[nextStatus]({
          gesuchTrancheId,
          onSuccess: () => {
            this.einreichenStore.validateSteps$({ gesuchTrancheId });
          },
        });
        break;
      case 'BEREIT_FUER_BEARBEITUNG':
      case 'VERFUEGT':
      case 'VERSENDET':
        this.gesuchStore.setStatus$[nextStatus]({ gesuchTrancheId });
        break;
      case 'BEREIT_FUER_BEARBEITUNG_AS_AENDERUNG':
        SharedUiKommentarDialogComponent.open<
          SachbearbeitungAppTranslationKey | SharedTranslationKey
        >(this.dialog, {
          titleKey:
            'sachbearbeitung-app.header.status-uebergang.BEREIT_FUER_BEARBEITUNG_AS_AENDERUNG.title',
          messageKey:
            'sachbearbeitung-app.header.status-uebergang.BEREIT_FUER_BEARBEITUNG_AS_AENDERUNG.message',
          placeholderKey:
            'sachbearbeitung-app.header.status-uebergang.BEREIT_FUER_BEARBEITUNG_AS_AENDERUNG.placeholder',
          confirmKey: 'shared.ui.yes',
        })
          .afterClosed()
          .pipe(takeUntilDestroyed(this.destroyRef))
          .subscribe((result) => {
            if (result) {
              this.gesuchStore.setStatus$.BEREIT_FUER_BEARBEITUNG_AS_AENDERUNG({
                gesuchTrancheId,
                text: result.kommentar,
              });
            }
          });
        break;
      case 'SET_TO_DATENSCHUTZBRIEF_DRUCKBEREIT':
        SharedUiKommentarDialogComponent.open<SachbearbeitungAppTranslationKey>(
          this.dialog,
          {
            titleKey: `sachbearbeitung-app.header.status-uebergang.SET_TO_DATENSCHUTZBRIEF_DRUCKBEREIT.title`,
            messageKey: `sachbearbeitung-app.header.status-uebergang.SET_TO_DATENSCHUTZBRIEF_DRUCKBEREIT.message`,
            placeholderKey: `sachbearbeitung-app.header.status-uebergang.SET_TO_DATENSCHUTZBRIEF_DRUCKBEREIT.placeholder`,
            confirmKey: `sachbearbeitung-app.header.status-uebergang.SET_TO_DATENSCHUTZBRIEF_DRUCKBEREIT.confirm`,
          },
        )
          .afterClosed()
          .pipe(takeUntilDestroyed(this.destroyRef))
          .subscribe((result) => {
            if (result) {
              this.gesuchStore.setStatus$.SET_TO_DATENSCHUTZBRIEF_DRUCKBEREIT({
                gesuchTrancheId,
                text: result.kommentar,
              });
            }
          });
        break;
      case 'ZURUECK_ZU_BEREIT_FUER_BEARBEITUNG':
        SharedUiKommentarDialogComponent.openOptional<SachbearbeitungAppTranslationKey>(
          this.dialog,
          {
            titleKey: `sachbearbeitung-app.header.status-uebergang.BEREIT_FUER_BEARBEITUNG.title`,
            messageKey: `sachbearbeitung-app.header.status-uebergang.BEREIT_FUER_BEARBEITUNG.message`,
            placeholderKey: `sachbearbeitung-app.header.status-uebergang.BEREIT_FUER_BEARBEITUNG.placeholder`,
            confirmKey: `sachbearbeitung-app.header.status-uebergang.BEREIT_FUER_BEARBEITUNG.confirm`,
          },
        )
          .afterClosed()
          .pipe(takeUntilDestroyed(this.destroyRef))
          .subscribe((result) => {
            if (result) {
              this.gesuchStore.setStatus$.ZURUECK_ZU_BEREIT_FUER_BEARBEITUNG({
                gesuchTrancheId,
                text: result.kommentar,
              });
            }
          });
        break;
      case 'ZURUECKWEISEN_OR_UNDO':
        SharedUiKommentarDialogComponent.open<SachbearbeitungAppTranslationKey>(
          this.dialog,
          {
            titleKey: `sachbearbeitung-app.header.status-uebergang.ZURUECKWEISEN.title`,
            messageKey: `sachbearbeitung-app.header.status-uebergang.ZURUECKWEISEN.message`,
            placeholderKey: `sachbearbeitung-app.header.status-uebergang.ZURUECKWEISEN.placeholder`,
            confirmKey: `sachbearbeitung-app.header.status-uebergang.ZURUECKWEISEN.confirm`,
          },
        )
          .afterClosed()
          .pipe(takeUntilDestroyed(this.destroyRef))
          .subscribe((result) => {
            if (result) {
              this.gesuchStore.setStatus$.ZURUECKWEISEN_OR_UNDO({
                gesuchTrancheId,
                text: result.kommentar,
                onSuccess: (newGesuchTrancheId, trancheTyp) => {
                  this.router.navigate([
                    'gesuch',
                    'info',
                    gesuchId,
                    lowercased(trancheTyp),
                    newGesuchTrancheId,
                  ]);
                  this.einreichenStore.validateSteps$({ gesuchTrancheId });
                },
              });
            }
          });
        break;
      case 'NEGATIVE_VERFUEGUNG_ERSTELLEN':
        SachbearbeitungAppUiGrundAuswahlDialogComponent.open(this.dialog, {
          titleKey: `sachbearbeitung-app.header.status-uebergang.${nextStatus}.title`,
          labelKey: `sachbearbeitung-app.header.status-uebergang.${nextStatus}.label`,
          messageKey: `sachbearbeitung-app.header.status-uebergang.${nextStatus}.message`,
          confirmKey: `sachbearbeitung-app.header.status-uebergang.${nextStatus}.confirm`,
          allowedTypes:
            this.deploymentConfigSig().deploymentConfig?.allowedMimeTypes,
        })
          .afterClosed()
          .pipe(takeUntilDestroyed(this.destroyRef))
          .subscribe((result) => {
            if (result) {
              switch (result.type) {
                case 'manuell': {
                  this.gesuchStore.createManuelleVerfuegung$({
                    gesuchTrancheId,
                    fileUpload: result.verfuegungUpload,
                    kommentar: result.kommentar,
                  });
                  break;
                }
                case 'grund': {
                  this.gesuchStore.setStatus$[nextStatus]({
                    gesuchTrancheId,
                    grundId: result.entityId,
                    kanton: result.kanton,
                  });
                  break;
                }
                default:
                  assertUnreachable(result);
              }
            }
          });
        break;
      default:
        assertUnreachable(nextStatus);
    }
  }

  createTranche() {
    const gesuchId = this.gesuchIdSig();
    const { gesuchInfo } = this.gesuchHeaderStore.header().data ?? {};
    if (!gesuchId || !gesuchInfo) return;

    SharedDialogTrancheErstellenComponent.open(this.dialog, {
      type: 'createTranche',
      gesuchId,
      minDate: new Date(gesuchInfo.startDate),
      maxDate: new Date(gesuchInfo.endDate),
    })
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe();
  }

  async exportTranche() {
    const exportValues = this.exportValuesSig();
    if (!exportValues) {
      return;
    }

    this.isExportingSig.set(true);

    try {
      const module = await import('@dv/shared/util-data-access/export-tranche');
      const exportTrancheService = runInInjectionContext(this.injector, () =>
        inject(module.SharedExportTrancheService),
      );

      await exportTrancheService.exportTranche(exportValues);
    } catch {
      this.globalNotificationStore.createNotification({
        type: 'ERROR',
        messageKey: translatableShared('shared.form.tranche.export.error'),
      });
    }

    this.isExportingSig.set(false);
  }

  async changeAenderungState(
    aenderungId: string,
    target: AenderungChangeState,
    gesuchId: string,
  ) {
    let comment = undefined;
    if (target === 'ABGELEHNT') {
      comment = (
        await firstValueFrom(
          SharedUiKommentarDialogComponent.open(this.dialog, {
            titleKey: 'shared.dialog.gesuch-aenderung.ABGELEHNT.title',
            messageKey: 'shared.dialog.gesuch-aenderung.ABGELEHNT.description',
            labelKey: 'shared.dialog.gesuch-aenderung.ABGELEHNT.comment.label',
            placeholderKey: 'shared.nothing',
            confirmKey: 'shared.form.send',
          }).afterClosed(),
        )
      )?.kommentar;

      if (!comment) {
        return;
      }
    }

    this.gesuchAenderungStore.changeAenderungState$({
      aenderungId,
      target,
      comment: comment ?? '',
      gesuchId,
      onSuccess: (trancheId) => {
        const routesMap = {
          AKZEPTIERT: ['gesuch', 'info', gesuchId, 'tranche', trancheId],
          ABGELEHNT: ['gesuch', 'info', gesuchId, 'tranche', trancheId],
          MANUELLE_AENDERUNG: ['gesuch', 'info', gesuchId],
        } satisfies Record<AenderungChangeState, unknown>;

        this.store.dispatch(SharedDataAccessGesuchEvents.loadGesuch());
        this.router.navigate(routesMap[target]);
      },
    });
  }
}

const getUebergangName = (
  uebergang: StatusUebergang,
  bearbeitungStatus?: InBearbeitungSbReason,
):
  | Exclude<StatusUebergang, 'ZURUECKWEISEN_OR_UNDO'>
  | 'ZURUECKWEISEN'
  | 'UNDO'
  | null => {
  switch (uebergang) {
    case 'ZURUECKWEISEN_OR_UNDO': {
      switch (bearbeitungStatus) {
        case 'INITIAL':
          return 'ZURUECKWEISEN';
        case 'AENDERUNG':
          return 'UNDO';
        default:
          return null;
      }
    }
    default:
      return uebergang;
  }
};
