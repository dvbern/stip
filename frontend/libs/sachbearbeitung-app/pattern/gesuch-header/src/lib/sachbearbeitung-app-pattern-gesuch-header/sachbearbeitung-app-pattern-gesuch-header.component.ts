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

import { SachbearbeitungAppTranslationKey } from '@dv/sachbearbeitung-app/assets/i18n';
import { GesuchStore } from '@dv/sachbearbeitung-app/data-access/gesuch';
import { SachbearbeitungAppUiAdvTranslocoDirective } from '@dv/sachbearbeitung-app/ui/adv-transloco-directive';
import { SachbearbeitungAppUiGrundAuswahlDialogComponent } from '@dv/sachbearbeitung-app/ui/grund-auswahl-dialog';
import { SharedTranslationKey } from '@dv/shared/assets/i18n';
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
  InBearbeitungSbReason,
  aenderungRoutes,
  getTrancheRoute,
} from '@dv/shared/model/gesuch';
import { getGesuchPermissions } from '@dv/shared/model/permission-state';
import { urlAfterNavigationEnd } from '@dv/shared/model/router';
import {
  assertUnreachable,
  isDefined,
  lowercased,
} from '@dv/shared/model/type-util';
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
  historizedSig = computed(() => {
    const {
      abgelehnt: abgelehnteAenderungen,
      akzeptiert: akzeptierteAenderungen,
      offen: offeneAenderung,
    } = this.gesuchHeaderStore.viewSig().aenderungs ?? {};
    const initial = this.gesuchHeaderStore.viewSig().initial;
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
      // TODO: Temporary solution until Header is changed
      versions: this.gesuchHeaderStore.viewSig().versions,
    };
  });
  isLoadingSig = computed(() => {
    return (
      isPending(this.gesuchHeaderStore.header()) ||
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
      const gesuchId = this.gesuchIdSig();
      this.gesuchUpdatedSig();
      if (gesuchId) {
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
      inBearbeitungSbReason,
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
      ?.map((status) => ({
        ...StatusUebergaengeOptions[status]({
          permissions,
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
