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
import { TranslocoPipe } from '@jsverse/transloco';
import { Store } from '@ngrx/store';
import { filter, map } from 'rxjs';

import { GesuchStore } from '@dv/sachbearbeitung-app/data-access/gesuch';
import { SachbearbeitungAppUiGrundAuswahlDialogComponent } from '@dv/sachbearbeitung-app/ui/grund-auswahl-dialog';
import { selectSharedDataAccessConfigsView } from '@dv/shared/data-access/config';
import { DarlehenStore } from '@dv/shared/data-access/darlehen';
import { DokumentsStore } from '@dv/shared/data-access/dokuments';
import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import {
  selectRevision,
  selectRouteId,
  selectRouteTrancheId,
  selectSharedDataAccessGesuchCache,
} from '@dv/shared/data-access/gesuch';
import {
  AenderungCompletionState,
  GesuchAenderungStore,
} from '@dv/shared/data-access/gesuch-aenderung';
import { GesuchInfoStore } from '@dv/shared/data-access/gesuch-info';
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
    TranslocoPipe,
    RouterLink,
    RouterLinkActive,
    MatChipsModule,
    MatMenuModule,
    MatTooltipModule,
    SharedPatternAppHeaderComponent,
    SharedPatternAppHeaderPartsDirective,
    SharedUiLoadingComponent,
    SharedUiDarlehenMenuComponent,
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
  private dokumentsStore = inject(DokumentsStore);
  private gesuchStore = inject(GesuchStore);
  private gesuchInfoStore = inject(GesuchInfoStore);
  private config = inject(SharedModelCompileTimeConfig);

  private deploymentConfigSig = this.store.selectSignal(
    selectSharedDataAccessConfigsView,
  );
  route = inject(ActivatedRoute);
  gesuchAenderungStore = inject(GesuchAenderungStore);
  darlehenStore = inject(DarlehenStore);

  @Output() openSidenav = new EventEmitter<void>();

  gesuchIdSig = this.store.selectSignal(selectRouteId);

  gesuchTrancheIdSig = this.store.selectSignal(selectRouteTrancheId);
  revisionSig = this.store.selectSignal(selectRevision);

  private otherGesuchInfoSourceSig = toSignal(
    this.store.select(selectSharedDataAccessGesuchCache).pipe(
      map(({ gesuch }) => gesuch),
      filter(isDefined),
    ),
  );

  tranchenSig = this.gesuchAenderungStore.getRelativeTranchenViewSig(
    this.gesuchIdSig,
  );
  isTrancheRouteSig = toSignal(
    urlAfterNavigationEnd(this.router).pipe(
      map((url) => url.includes(`/${getTrancheRoute('tranche')}/`)),
    ),
  );
  isAenderungRouteSig = toSignal(
    urlAfterNavigationEnd(this.router).pipe(
      map((url) => aenderungRoutes.some((route) => url.includes(`/${route}/`))),
    ),
  );
  canViewBerechnungSig = computed(() => {
    const canViewBerechnung =
      this.gesuchInfoStore.gesuchInfo().data?.canGetBerechnung;

    return canViewBerechnung;
  });
  isBeschwerdeHaengigSig = computed(() => {
    const beschwerdeHaengig =
      this.gesuchInfoStore.gesuchInfo().data?.beschwerdeHaengig;
    return beschwerdeHaengig;
  });
  isLoadingSig = computed(() => {
    return (
      isPending(this.gesuchInfoStore.gesuchInfo()) ||
      isPending(this.gesuchStore.lastStatusChange())
    );
  });
  listedAenderungen: AenderungCompletionState[] = [
    'open',
    'completed',
    'rejected',
    'initial',
  ];

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
      if (gesuchId) {
        this.darlehenStore.getAllDarlehenSb$({ gesuchId });
        this.gesuchInfoStore.loadGesuchInfo$({ gesuchId });
        this.gesuchAenderungStore.getAllTranchenForGesuch$({ gesuchId });
      }
    });

    effect(() => {
      const gesuchTrancheId = this.gesuchTrancheIdSig();
      if (gesuchTrancheId) {
        this.dokumentsStore.getGesuchDokumenteAndDocumentsToUpload$({
          gesuchTrancheId,
        });
      }
    });

    effect(() => {
      const gesuch = this.otherGesuchInfoSourceSig();

      if (gesuch?.id) {
        this.gesuchInfoStore.loadGesuchInfo$({ gesuchId: gesuch.id });
        this.einreichenStore.validateSteps$({
          gesuchTrancheId: gesuch.gesuchTrancheToWorkWith.id,
        });
      }
    });
  }

  availableTrancheInteractionSig = computed(() => {
    const rolesMap = this.permissionStore.rolesMapSig();
    const gesuchStatus = this.gesuchInfoStore.gesuchInfo().data?.gesuchStatus;

    if (gesuchStatus === 'IN_BEARBEITUNG_SB' && rolesMap.V0_Sachbearbeiter) {
      return 'CREATE_TRANCHE';
    } else {
      return null;
    }
  });

  statusUebergaengeOptionsSig = computed(() => {
    const rolesMap = this.permissionStore.rolesMapSig();
    const gesuchInfo = this.gesuchInfoStore.gesuchInfo().data;
    const gesuchStatus = gesuchInfo?.gesuchStatus;

    const sbCanBearbeitungAbschliessen =
      this.dokumentsStore.dokumenteCanFlagsSig().sbCanBearbeitungAbschliessen;
    const validations =
      this.einreichenStore.validationViewSig().invalidFormularProps.validations;

    const canTriggerManuellPruefen =
      this.gesuchInfoStore.gesuchInfo().data?.canTriggerManuellPruefen;

    if (!gesuchStatus) {
      return {};
    }

    const { permissions } = getGesuchPermissions(
      gesuchInfo,
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
          hasAcceptedAllDokuments: !!sbCanBearbeitungAbschliessen,
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
        this.gesuchStore.setStatus$[nextStatus]({ gesuchTrancheId });
        break;
      case 'VERSENDET':
        this.gesuchStore.setStatus$[nextStatus]({
          gesuchTrancheId,
          onSuccess: () => {
            this.gesuchAenderungStore.getAllTranchenForGesuch$({ gesuchId });
          },
        });
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
    const periode = this.gesuchInfoStore.gesuchInfo().data;
    if (!id || !periode) return;

    SharedDialogTrancheErstellenComponent.open(this.dialog, {
      type: 'createTranche',
      id,
      minDate: new Date(periode.startDate),
      maxDate: new Date(periode.endDate),
    })
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe();
  }
}
