import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  EventEmitter,
  Output,
  computed,
  effect,
  inject,
} from '@angular/core';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { MatChipsModule } from '@angular/material/chips';
import { MatDialog } from '@angular/material/dialog';
import { MatMenuModule } from '@angular/material/menu';
import { MatTooltipModule } from '@angular/material/tooltip';
import {
  ActivatedRoute,
  Router,
  RouterLink,
  RouterLinkActive,
} from '@angular/router';
import { Store } from '@ngrx/store';
import { filter, map } from 'rxjs';

import { GesuchStore } from '@dv/sachbearbeitung-app/data-access/gesuch';
import { SachbearbeitungAppUiAdvTranslocoDirective } from '@dv/sachbearbeitung-app/ui/adv-transloco-directive';
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
import { aenderungRoutes, getTrancheRoute } from '@dv/shared/model/gesuch';
import { getGesuchPermissions } from '@dv/shared/model/permission-state';
import { urlAfterNavigationEnd } from '@dv/shared/model/router';
import { assertUnreachable, isDefined } from '@dv/shared/model/type-util';
import {
  SharedPatternAppHeaderComponent,
  SharedPatternAppHeaderPartsDirective,
} from '@dv/shared/pattern/app-header';
import { SharedUiDarlehenMenuComponent } from '@dv/shared/ui/darlehen-menu';
import { SharedUiKommentarDialogComponent } from '@dv/shared/ui/kommentar-dialog';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import {
  StatusUebergaengeMap,
  StatusUebergaengeOptions,
  StatusUebergang,
} from '@dv/shared/util/gesuch';
import { isPending } from '@dv/shared/util/remote-data';

@Component({
  selector: 'dv-sachbearbeitung-app-pattern-gesuch-header',
  imports: [
    CommonModule,
    RouterLink,
    RouterLinkActive,
    MatChipsModule,
    MatMenuModule,
    MatTooltipModule,
    SharedPatternAppHeaderComponent,
    SharedPatternAppHeaderPartsDirective,
    SharedUiLoadingComponent,
    SharedUiDarlehenMenuComponent,
    SachbearbeitungAppUiAdvTranslocoDirective,
  ],
  templateUrl: './sachbearbeitung-app-pattern-gesuch-header.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppPatternGesuchHeaderComponent {
  private store = inject(Store);
  private router = inject(Router);
  private destroyRef = inject(DestroyRef);
  private dialog = inject(MatDialog);
  private einreichenStore = inject(EinreichenStore);
  private permissionStore = inject(PermissionStore);
  private gesuchStore = inject(GesuchStore);
  private gesuchHeaderStore = inject(GesuchHeaderStore);
  private config = inject(SharedModelCompileTimeConfig);

  private deploymentConfigSig = this.store.selectSignal(
    selectSharedDataAccessConfigsView,
  );
  route = inject(ActivatedRoute);
  darlehenStore = inject(DarlehenStore);

  @Output() openSidenav = new EventEmitter<void>();

  gesuchIdSig = this.store.selectSignal(selectRouteId);

  gesuchTrancheIdSig = this.store.selectSignal(selectRouteTrancheId);
  revisionSig = this.store.selectSignal(selectRevision);

  private gesuchUpdatedSig = toSignal(
    this.store.select(selectSharedDataAccessGesuchCache).pipe(
      map(({ gesuch }) => gesuch),
      filter(isDefined),
    ),
  );

  isTrancheRouteSig = toSignal(
    urlAfterNavigationEnd(this.router).pipe(
      map((url) => url.includes(`/${getTrancheRoute('tranche')}/`)),
    ),
  );
  isInitialRouteSig = toSignal(
    urlAfterNavigationEnd(this.router).pipe(
      map((url) => url.includes(`/${getTrancheRoute('initial')}/`)),
    ),
  );
  isAenderungRouteSig = toSignal(
    urlAfterNavigationEnd(this.router).pipe(
      map((url) => aenderungRoutes.some((route) => url.includes(`/${route}/`))),
    ),
  );

  canViewBerechnungSig = computed(() => {
    const canViewBerechnung =
      this.gesuchHeaderStore.viewSbSig()?.stateInfo?.canGetBerechnung;

    return canViewBerechnung;
  });
  isBeschwerdeHaengigSig = computed(() => {
    const beschwerdeHaengig =
      this.gesuchHeaderStore.viewSbSig()?.stateInfo?.beschwerdeHaengig;
    return beschwerdeHaengig;
  });
  tranchenSig = this.gesuchHeaderStore.getRelativeTranchenViewSbSig(
    this.gesuchIdSig,
  );
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
  isLoadingSig = computed(() => {
    return (
      isPending(this.gesuchHeaderStore.headerSb()) ||
      isPending(this.gesuchStore.lastStatusChange())
    );
  });

  isInfosRouteSig = computed(() => {
    const isActive = this.router.isActive('infos', {
      paths: 'subset',
      fragment: 'ignored',
      matrixParams: 'ignored',
      queryParams: 'ignored',
    });
    return isActive;
  });

  constructor() {
    effect(() => {
      const gesuchTrancheId = this.gesuchTrancheIdSig();
      this.gesuchUpdatedSig();
      if (gesuchTrancheId) {
        this.gesuchHeaderStore.loadHeaderSb$({ gesuchTrancheId });
      }
    });
  }

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
