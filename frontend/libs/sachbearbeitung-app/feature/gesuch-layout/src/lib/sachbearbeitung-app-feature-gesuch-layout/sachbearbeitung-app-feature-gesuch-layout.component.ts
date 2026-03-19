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

import { GesuchStore } from '@dv/sachbearbeitung-app/data-access/gesuch';
import { SachbearbeitungAppUiGrundAuswahlDialogComponent } from '@dv/sachbearbeitung-app/ui/grund-auswahl-dialog';
import { selectSharedDataAccessConfigsView } from '@dv/shared/data-access/config';
import { DarlehenStore } from '@dv/shared/data-access/darlehen';
import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import {
  selectRevision,
  selectRouteGesuchId,
  selectRouteTrancheId,
  selectSharedDataAccessGesuchCache,
} from '@dv/shared/data-access/gesuch';
import { GesuchHeaderStore } from '@dv/shared/data-access/gesuch-header';
import { SharedDialogTrancheErstellenComponent } from '@dv/shared/dialog/tranche-erstellen';
import { PermissionStore } from '@dv/shared/global/permission';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import {
  GesuchHeader,
  aenderungRoutes,
  getTrancheRoute,
} from '@dv/shared/model/gesuch';
import { getGesuchPermissions } from '@dv/shared/model/permission-state';
import { urlAfterNavigationEnd } from '@dv/shared/model/router';
import { assertUnreachable, isDefined } from '@dv/shared/model/type-util';
import { SharedPatternGesuchInfoBarComponent } from '@dv/shared/pattern/gesuch-info-bar';
import { SharedPatternGlobalHeaderPartsDirective } from '@dv/shared/pattern/global-header';
import { SharedUiAenderungenMenuComponent } from '@dv/shared/ui/aenderungen-menu';
import { SharedUiDarlehenMenuComponent } from '@dv/shared/ui/darlehen-menu';
import { SharedUiKommentarDialogComponent } from '@dv/shared/ui/kommentar-dialog';
import { SharedUiVersionenMenuComponent } from '@dv/shared/ui/versionen-menu';
import {
  StatusUebergaengeMap,
  StatusUebergaengeOptions,
  StatusUebergang,
} from '@dv/shared/util/gesuch';
import { isPending } from '@dv/shared/util/remote-data';

@Component({
  imports: [
    CommonModule,
    RouterOutlet,
    RouterLink,
    MatTabsModule,
    MatMenuModule,
    MatTooltipModule,
    SharedUiAenderungenMenuComponent,
    SharedPatternGesuchInfoBarComponent,
    SharedUiDarlehenMenuComponent,
    MatChip,
    SharedPatternGlobalHeaderPartsDirective,
    SharedUiAenderungenMenuComponent,
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
  revisionSig = this.store.selectSignal(selectRevision);

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

  noActionRoutes = ['aenderung', 'initial', 'infos', 'darlehen'];
  isActionRouteSig = computed(() => {
    const url = this.routeUrlSig();
    return !this.noActionRoutes.some(
      (route) => url?.includes(`/${route}/`) || this.berechnungIdSig(),
    );
  });

  noGesuchActiveRoutes = ['aenderung', 'infos', 'darlehen'];
  isGesuchRouteSig = computed(() => {
    const url = this.routeUrlSig();
    return !this.noGesuchActiveRoutes.some((route) =>
      url?.includes(`/${route}/`),
    );
  });
  isInfosRouteSig = computed(() => {
    const url = this.routeUrlSig();
    return url?.includes('/infos/');
  });

  // delete
  isAenderungOrInitialRouteSig = toSignal(
    urlAfterNavigationEnd(this.router).pipe(
      map((url) => aenderungRoutes.some((route) => url.includes(`/${route}/`))),
    ),
  );

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

  // firstCurrentTranchenSig = computed(() => {
  //   return this.headerViewSig().currentTranches?.[0];
  // });

  tabsSig = computed(() => {
    const gesuchId = this.gesuchIdSig();
    const trancheId = this.trancheIdSig();
    const { gesuchInfo } = this.headerViewSig();
    const activePath = this.routeUrlSig();
    const berechnungId = this.berechnungIdSig();

    if (!this.isGesuchRouteSig()) {
      return [];
    }

    // todo-after-merge: use correct tranche in KSTIP-2856 => solve routing issue with params in versionendropdown
    // use active url for route params!
    const gesuchTab = {
      active: !activePath?.includes('/verfuegung'),
      route: ['/gesuch', gesuchId, 'tranche', trancheId],
      queryParams: { berechnungId },
      name: 'formular',
    };

    // todo-after-merge: implement verfuegungId as param to route directly to a verfuegung
    // todo: typesafe!
    const verfuegungTab = {
      active: activePath?.includes('/verfuegung'),
      route: ['/gesuch/verfuegung', gesuchId, 'tranche', trancheId],
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
  // aenderungenSig = computed(() => {
  //   const {
  //     abgelehnt: abgelehnteAenderungen,
  //     akzeptiert: akzeptierteAenderungen,
  //     offen: offeneAenderung,
  //   } = this.gesuchHeaderStore.viewSig().aenderungs ?? {};
  //   const initial = this.gesuchHeaderStore.viewSig().initial;
  //   if (
  //     !abgelehnteAenderungen?.length &&
  //     !akzeptierteAenderungen?.length &&
  //     !initial &&
  //     !offeneAenderung
  //   ) {
  //     return null;
  //   }
  //   return {
  //     abgelehnteAenderungen,
  //     akzeptierteAenderungen,
  //     initial,
  //     offeneAenderung,
  //   };
  // });

  isLoadingSig = computed(() => {
    return (
      isPending(this.gesuchHeaderStore.header()) ||
      isPending(this.gesuchStore.lastStatusChange())
    );
  });

  constructor() {
    // log effect
    // effect(() => {
    //   // console.log('trancheTyp', this.trancheTypSig());
    //   console.log('isAenderungRoute', this.isAenderungRouteSig());
    //   console.log('isGesuchRoute', this.isGesuchRouteSig());
    // });

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
      case 'SET_TO_DATENSCHUTZBRIEF_DRUCKBEREIT':
        SharedUiKommentarDialogComponent.open(this.dialog, {
          titleKey: `sachbearbeitung-app.header.status-uebergang.SET_TO_DATENSCHUTZBRIEF_DRUCKBEREIT.title`,
          messageKey: `sachbearbeitung-app.header.status-uebergang.SET_TO_DATENSCHUTZBRIEF_DRUCKBEREIT.message`,
          placeholderKey: `sachbearbeitung-app.header.status-uebergang.SET_TO_DATENSCHUTZBRIEF_DRUCKBEREIT.placeholder`,
          confirmKey: `sachbearbeitung-app.header.status-uebergang.SET_TO_DATENSCHUTZBRIEF_DRUCKBEREIT.confirm`,
        })
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
        SharedUiKommentarDialogComponent.openOptional(this.dialog, {
          titleKey: `sachbearbeitung-app.header.status-uebergang.BEREIT_FUER_BEARBEITUNG.title`,
          messageKey: `sachbearbeitung-app.header.status-uebergang.BEREIT_FUER_BEARBEITUNG.message`,
          placeholderKey: `sachbearbeitung-app.header.status-uebergang.BEREIT_FUER_BEARBEITUNG.placeholder`,
          confirmKey: `sachbearbeitung-app.header.status-uebergang.BEREIT_FUER_BEARBEITUNG.confirm`,
        })
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
        SharedUiKommentarDialogComponent.open(this.dialog, {
          titleKey: `sachbearbeitung-app.header.status-uebergang.ZURUECKWEISEN.title`,
          messageKey: `sachbearbeitung-app.header.status-uebergang.ZURUECKWEISEN.message`,
          placeholderKey: `sachbearbeitung-app.header.status-uebergang.ZURUECKWEISEN.placeholder`,
          confirmKey: `sachbearbeitung-app.header.status-uebergang.ZURUECKWEISEN.confirm`,
        })
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
