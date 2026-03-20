import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  HostBinding,
  Signal,
  computed,
  effect,
  inject,
} from '@angular/core';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { MatChip } from '@angular/material/chips';
import { MatDialog } from '@angular/material/dialog';
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
import { filter, map, startWith } from 'rxjs';

import { SachbearbeitungAppTranslationKey } from '@dv/sachbearbeitung-app/assets/i18n';
import { GesuchStore } from '@dv/sachbearbeitung-app/data-access/gesuch';
import { SachbearbeitungAppUiGrundAuswahlDialogComponent } from '@dv/sachbearbeitung-app/ui/grund-auswahl-dialog';
import { SharedTranslationKey } from '@dv/shared/assets/i18n';
import { selectSharedDataAccessConfigsView } from '@dv/shared/data-access/config';
import { DarlehenStore } from '@dv/shared/data-access/darlehen';
import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import {
  selectRouteGesuchId,
  selectRouteTrancheId,
  selectSharedDataAccessGesuchCache,
} from '@dv/shared/data-access/gesuch';
import { GesuchHeaderStore } from '@dv/shared/data-access/gesuch-header';
import { SharedDialogTrancheErstellenComponent } from '@dv/shared/dialog/tranche-erstellen';
import { PermissionStore } from '@dv/shared/global/permission';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import {
  FreiwilligDarlehen,
  GesuchHeader,
  getTrancheRoute,
} from '@dv/shared/model/gesuch';
import { getGesuchPermissions } from '@dv/shared/model/permission-state';
import { urlAfterNavigationEnd } from '@dv/shared/model/router';
import { assertUnreachable, isDefined } from '@dv/shared/model/type-util';
import {
  DarlehenCompleteStates,
  darlehenStatusMapping,
} from '@dv/shared/model/ui';
import {
  noActionRoutes,
  noGesuchActiveRoutes,
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
import { TabNavItem } from '@dv/shared/util/navigation';
import { isPending } from '@dv/shared/util/remote-data';

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
    MatChip,
    SharedPatternGlobalHeaderPartsDirective,
    TranslocoDirective,
    SharedUiVersionenMenuComponent,
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
  private gesuchStore = inject(GesuchStore);
  private deploymentConfigSig = this.store.selectSignal(
    selectSharedDataAccessConfigsView,
  );

  darlehenStore = inject(DarlehenStore);
  gesuchIdSig = this.store.selectSignal(selectRouteGesuchId);
  trancheIdSig = this.store.selectSignal(selectRouteTrancheId);

  berechnungIdSig = toSignal(
    this.route.queryParamMap.pipe(
      map((params) => params.get('berechnungId') ?? undefined),
    ),
  );

  routeUrlSig = toSignal(
    urlAfterNavigationEnd(this.router).pipe(
      map(() => this.router.routerState.snapshot.url),
      startWith(this.router.routerState.snapshot.url),
    ),
  );

  isActionRouteSig = computed(() => {
    const url = this.routeUrlSig();
    return !noActionRoutes.some(
      (route) => url?.includes(`/${route}/`) || this.berechnungIdSig(),
    );
  });

  isGesuchRouteSig = computed(() => {
    const url = this.routeUrlSig();
    return !noGesuchActiveRoutes.some((route) => url?.includes(`/${route}/`));
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

  headerViewSig: Signal<{ isLoading: boolean } & Partial<GesuchHeader>> =
    this.gesuchHeaderStore.viewSig;

  gesuchstellerNameSig = computed(() => {
    const info = this.headerViewSig().gesuchInfo;
    return info ? `${info.piaVorname} ${info.piaNachname}` : '';
  });

  tabsSig = computed<TabNavItem[]>(() => {
    const gesuchId = this.gesuchIdSig();
    const trancheId = this.trancheIdSig();
    const { gesuchInfo } = this.headerViewSig();
    const activePath = this.routeUrlSig();
    const berechnungId = this.berechnungIdSig();
    const isIntitial = this.isInitialRouteSig();
    const isAenderung = this.isAenderungRouteSig();

    // todo-review: @scph oder lieber mit trancheSetting und ngrx store?
    const trancheTyp = isIntitial
      ? 'initial'
      : isAenderung
        ? 'aenderung'
        : 'tranche';

    if (!this.isGesuchRouteSig()) {
      return [];
    }

    const gesuchTab = {
      active: !activePath?.includes('/verfuegung'),
      route: ['/gesuch', gesuchId, trancheTyp, trancheId],
      queryParams: { berechnungId },
      name: 'formular',
    };

    const verfuegungTab = {
      active: activePath?.includes('/verfuegung'),
      route: ['/gesuch/verfuegung', gesuchId, trancheTyp, trancheId],
      queryParams: { berechnungId },
      name: 'verfuegung',
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

  firstAenderungIdSig = computed(() => {
    const aenderungen = this.headerViewSig().aenderungs;
    const offeneAenderung = aenderungen?.offen;
    const akzeptierteAenderungen = aenderungen?.akzeptiert;
    const abgelehnteAenderungen = aenderungen?.abgelehnt;

    const allAenderungen = [
      ...(offeneAenderung ? [offeneAenderung] : []),
      ...(akzeptierteAenderungen ?? []),
      ...(abgelehnteAenderungen ?? []),
    ];

    return allAenderungen.length > 0 ? allAenderungen[0].id : undefined;
  });

  firstDarlehenIdSig = computed(() => {
    const darlehen = this.darlehenStore.darlehenListSbViewSig().list ?? [];

    const byType = darlehen.reduce(
      (acc, darlehen) => {
        const statusKey = darlehenStatusMapping[darlehen.status!];

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

  constructor() {
    effect(() => {
      const gesuchId = this.gesuchIdSig();
      this.gesuchUpdatedSig();
      if (gesuchId) {
        this.darlehenStore.getAllDarlehenSb$({ gesuchId });
        this.gesuchHeaderStore.loadHeader$({ gesuchId });
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
      canTriggerManuellPruefen,
      canBearbeitungAbschliessen,
    } = this.gesuchHeaderStore.viewSig()?.gesuchInfo?.state ?? {};

    if (!gesuchStatus) {
      return {};
    }

    const { permissions } = getGesuchPermissions(
      { gesuchStatus },
      this.config.appType,
      rolesMap,
    );

    const hasValidationErrors = !!validations.errors?.length;
    const hasValidationWarnings = !!validations.warnings?.length;
    const list = StatusUebergaengeMap[gesuchStatus]
      ?.concat(canTriggerManuellPruefen ? ['STATUS_PRUEFUNG_AUSLOESEN'] : [])
      ?.map((status) =>
        StatusUebergaengeOptions[status]({
          permissions,
          hasAcceptedAllDokuments: !!canBearbeitungAbschliessen,
          isInvalid: hasValidationErrors || hasValidationWarnings,
        }),
      )
      .filter((uebergang) =>
        uebergang.allowedFor.some((role) => rolesMap[role]),
      );

    return {
      list,
      isNotEmpty: !!list?.length,
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
      case 'ANSPRUCH_PRUEFEN':
      case 'BEARBEITUNG_ABSCHLIESSEN':
      case 'STATUS_PRUEFUNG_AUSLOESEN':
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
      case 'ZURUECKWEISEN':
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
              this.gesuchStore.setStatus$.ZURUECKWEISEN({
                gesuchTrancheId,
                text: result.kommentar,
                onSuccess: (newGesuchTrancheId) => {
                  this.router.navigate([
                    'gesuch',
                    'info',
                    gesuchId,
                    'tranche',
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
}
