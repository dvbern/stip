import { CommonModule } from '@angular/common';
import {
  Component,
  DOCUMENT,
  DestroyRef,
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
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { TranslocoDirective, TranslocoPipe } from '@jsverse/transloco';
import { Store } from '@ngrx/store';
import { filter, map, startWith } from 'rxjs';

import { GesuchStore } from '@dv/sachbearbeitung-app/data-access/gesuch';
import { SachbearbeitungAppUiGrundAuswahlDialogComponent } from '@dv/sachbearbeitung-app/ui/grund-auswahl-dialog';
import { selectSharedDataAccessConfigsView } from '@dv/shared/data-access/config';
import { DarlehenStore } from '@dv/shared/data-access/darlehen';
import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import {
  selectRevision,
  selectRouteId,
  selectRouteTrancheId,
  selectSharedDataAccessGesuchCache,
} from '@dv/shared/data-access/gesuch';
import { GesuchHeaderStore } from '@dv/shared/data-access/gesuch-header';
import { SharedDialogTrancheErstellenComponent } from '@dv/shared/dialog/tranche-erstellen';
import { PermissionStore } from '@dv/shared/global/permission';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import {
  GesuchHeaderSb,
  aenderungRoutes,
  getTrancheRoute,
} from '@dv/shared/model/gesuch';
import { getGesuchPermissions } from '@dv/shared/model/permission-state';
import { urlAfterNavigationEnd } from '@dv/shared/model/router';
import { assertUnreachable, isDefined } from '@dv/shared/model/type-util';
import { SharedPatternGesuchInfoBarComponent } from '@dv/shared/pattern/gesuch-info-bar';
import { SharedUiDarlehenMenuComponent } from '@dv/shared/ui/darlehen-menu';
import { SharedUiKommentarDialogComponent } from '@dv/shared/ui/kommentar-dialog';
import {
  StatusUebergaengeMap,
  StatusUebergaengeOptions,
  StatusUebergang,
} from '@dv/shared/util/gesuch';
import { isPending } from '@dv/shared/util/remote-data';

// const ALL_TABS = ['formular', 'verfuegung'] as const;

@Component({
  imports: [
    CommonModule,
    RouterOutlet,
    RouterLink,
    MatTabsModule,
    TranslocoPipe,
    MatMenuModule,
    MatTooltipModule,
    SharedPatternGesuchInfoBarComponent,
    TranslocoDirective, // todo: use the right one
    SharedUiDarlehenMenuComponent,
    MatChip,
  ],
  templateUrl: './sachbearbeitung-app-pattern-gesuch-layout.component.html',
})
export class SachbearbeitungAppPatternGesuchLayoutComponent {
  private router = inject(Router);
  private wndw = inject(DOCUMENT, { optional: true })?.defaultView;
  private store = inject(Store);
  private gesuchHeaderStore = inject(GesuchHeaderStore);
  private permissionStore = inject(PermissionStore);
  private einreichenStore = inject(EinreichenStore);
  private config = inject(SharedModelCompileTimeConfig);
  private destroyRef = inject(DestroyRef);
  private dialog = inject(MatDialog);
  private gesuchStore = inject(GesuchStore);

  darlehenStore = inject(DarlehenStore);
  gesuchIdSig = this.store.selectSignal(selectRouteId); // todo: take from input instead, so no store needed?
  gesuchTrancheIdSig = this.store.selectSignal(selectRouteTrancheId); // todo: take from input instead, so no store needed?

  // todo: change to use signal inputs!
  isAenderungRouteSig = toSignal(
    urlAfterNavigationEnd(this.router).pipe(
      map((url) => aenderungRoutes.some((route) => url.includes(`/${route}/`))),
    ),
  );
  isInitialRouteSig = toSignal(
    urlAfterNavigationEnd(this.router).pipe(
      map((url) => url.includes(`/${getTrancheRoute('initial')}/`)),
    ),
  );
  isBeschwerdeHaengigSig = computed(() => {
    const beschwerdeHaengig =
      this.gesuchHeaderStore.viewSbSig()?.stateInfo?.beschwerdeHaengig;
    return beschwerdeHaengig;
  });

  revisionSig = this.store.selectSignal(selectRevision);

  historizedSig = computed(() => {
    const {
      abgelehnteAenderungen,
      akzeptierteAenderungen,
      initial,
      offeneAenderung,
    } = this.gesuchHeaderStore.viewSbSig().historized ?? {};
    if (
      !abgelehnteAenderungen?.length &&
      !akzeptierteAenderungen?.length &&
      !initial &&
      !offeneAenderung
    ) {
      return null;
    }
    return {
      abgelehnteAenderungen,
      akzeptierteAenderungen,
      initial,
      offeneAenderung,
    };
  });

  gesuchInfoSig = computed(() => {
    const cache = this.store.selectSignal(selectSharedDataAccessGesuchCache)();

    return cache;
  });

  private deploymentConfigSig = this.store.selectSignal(
    selectSharedDataAccessConfigsView,
  );

  private gesuchUpdatedSig = toSignal(
    this.store.select(selectSharedDataAccessGesuchCache).pipe(
      map(({ gesuch }) => gesuch),
      filter(isDefined),
    ),
  );

  isLoadingSig = computed(() => {
    return isPending(this.gesuchHeaderStore.headerSb());
    //  ||
    // isPending(this.gesuchStore.lastStatusChange()) //needed?
  });

  headerViewSbSig: Signal<{ isLoading: boolean } & Partial<GesuchHeaderSb>> =
    this.gesuchHeaderStore.viewSbSig;

  // todo: rework with active
  activeTabSig = toSignal(
    urlAfterNavigationEnd(this.router).pipe(
      map(() => this.wndw?.location.pathname),
      startWith(this.wndw?.location.pathname),
    ),
  );

  tabsSig = computed(() => {
    const cache = this.gesuchInfoSig();
    const { stateInfo } = this.headerViewSbSig();

    const activePath = this.activeTabSig();

    const gesuchTab = {
      active: activePath?.endsWith('formular'),
      route: 'formular',
      name: 'formular',
    };

    const verfuegungTab = {
      active: activePath?.endsWith('verfuegung'),
      route: ['verfuegung', cache?.gesuch?.id],
      name: 'verfuegung',
    };

    if (stateInfo?.canGetBerechnung) {
      return [gesuchTab, verfuegungTab];
    }

    return [gesuchTab];
  });

  statusUebergaengeOptionsSig = computed(() => {
    const rolesMap = this.permissionStore.rolesMapSig();
    const validations =
      this.einreichenStore.validationViewSig().invalidFormularProps.validations;

    const {
      gesuchStatus,
      canTriggerManuellPruefen,
      canBearbeitungAbschliessen,
    } = this.gesuchHeaderStore.viewSbSig()?.stateInfo ?? {};

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

  availableTrancheInteractionSig = computed(() => {
    const rolesMap = this.permissionStore.rolesMapSig();
    const gesuchStatus =
      this.gesuchHeaderStore.viewSbSig()?.stateInfo?.gesuchStatus;

    if (gesuchStatus === 'IN_BEARBEITUNG_SB' && rolesMap.V0_Sachbearbeiter) {
      return 'CREATE_TRANCHE';
    } else {
      return null;
    }
  });

  constructor() {
    effect(() => {
      const gesuchId = this.gesuchIdSig();

      if (gesuchId) {
        this.darlehenStore.getAllDarlehenSb$({ gesuchId });
      }
    });

    effect(() => {
      const gesuchTrancheId = this.gesuchTrancheIdSig();
      this.gesuchUpdatedSig();
      if (gesuchTrancheId) {
        this.gesuchHeaderStore.loadHeaderSb$({ gesuchTrancheId });
      }
    });
  }

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
    const id = this.gesuchIdSig();
    const { periodeStart, periodeEnd } =
      this.gesuchHeaderStore.headerSb().data ?? {};
    if (!id || !periodeStart || !periodeEnd) return;

    SharedDialogTrancheErstellenComponent.open(this.dialog, {
      type: 'createTranche',
      id,
      minDate: new Date(periodeStart),
      maxDate: new Date(periodeEnd),
    })
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe();
  }
}
