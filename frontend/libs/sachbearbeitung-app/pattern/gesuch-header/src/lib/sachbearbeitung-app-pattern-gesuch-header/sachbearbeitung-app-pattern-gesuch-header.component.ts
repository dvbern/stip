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
import { TranslatePipe } from '@ngx-translate/core';
import { filter, map } from 'rxjs';

import { GesuchStore } from '@dv/sachbearbeitung-app/data-access/gesuch';
import { SachbearbeitungAppUiGrundAuswahlDialogComponent } from '@dv/sachbearbeitung-app/ui/grund-auswahl-dialog';
import { DokumentsStore } from '@dv/shared/data-access/dokuments';
import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import {
  SharedDataAccessGesuchEvents,
  selectRouteId,
  selectRouteTrancheId,
  selectSharedDataAccessGesuchCache,
} from '@dv/shared/data-access/gesuch';
import { GesuchAenderungStore } from '@dv/shared/data-access/gesuch-aenderung';
import { SharedDialogTrancheErstellenComponent } from '@dv/shared/dialog/tranche-erstellen';
import { PermissionStore } from '@dv/shared/global/permission';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import { getGesuchPermissions } from '@dv/shared/model/permission-state';
import { urlAfterNavigationEnd } from '@dv/shared/model/router';
import { assertUnreachable, isDefined } from '@dv/shared/model/type-util';
import {
  SharedPatternAppHeaderComponent,
  SharedPatternAppHeaderPartsDirective,
} from '@dv/shared/pattern/app-header';
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
    TranslatePipe,
    RouterLink,
    RouterLinkActive,
    MatChipsModule,
    MatMenuModule,
    MatTooltipModule,
    SharedPatternAppHeaderComponent,
    SharedPatternAppHeaderPartsDirective,
    SharedUiLoadingComponent,
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
  private einreichnenStore = inject(EinreichenStore);
  private config = inject(SharedModelCompileTimeConfig);
  route = inject(ActivatedRoute);
  gesuchAenderungStore = inject(GesuchAenderungStore);

  @Output() openSidenav = new EventEmitter<void>();

  gesuchIdSig = this.store.selectSignal(selectRouteId);
  gesuchTrancheIdSig = this.store.selectSignal(selectRouteTrancheId);
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
      map((url) => url.includes('/tranche/')),
    ),
  );
  isAenderungRouteSig = toSignal(
    urlAfterNavigationEnd(this.router).pipe(
      map((url) => url.includes('/aenderung/') || url.includes('/initial/')),
    ),
  );
  canViewBerechnungSig = computed(() => {
    const canViewBerechnung =
      this.gesuchStore.gesuchInfo().data?.canGetBerechnung;

    return canViewBerechnung;
  });
  isBeschwerdeHaengigSig = computed(() => {
    const beschwerdeHaengig =
      this.gesuchStore.gesuchInfo().data?.beschwerdeHaengig;
    return beschwerdeHaengig;
  });
  isLoadingSig = computed(() => {
    return (
      isPending(this.gesuchStore.gesuchInfo()) ||
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
      if (gesuchId) {
        this.gesuchStore.loadGesuchInfo$({ gesuchId });
        this.gesuchAenderungStore.getAllTranchenForGesuch$({ gesuchId });
      }
    });

    effect(() => {
      const gesuchTrancheId = this.gesuchTrancheIdSig();
      if (gesuchTrancheId) {
        this.dokumentsStore.getDokumenteAndRequired$({ gesuchTrancheId });
      }
    });

    effect(() => {
      const gesuch = this.otherGesuchInfoSourceSig();

      if (gesuch?.id) {
        this.gesuchStore.loadGesuchInfo$({ gesuchId: gesuch.id });
        this.einreichnenStore.validateSteps$({
          gesuchTrancheId: gesuch.gesuchTrancheToWorkWith.id,
        });
      }
    });
  }

  availableTrancheInteractionSig = computed(() => {
    const rolesMap = this.permissionStore.rolesMapSig();
    const gesuchStatus = this.gesuchStore.gesuchInfo().data?.gesuchStatus;

    if (gesuchStatus === 'IN_BEARBEITUNG_SB' && rolesMap.V0_Sachbearbeiter) {
      return 'CREATE_TRANCHE';
    } else {
      return null;
    }
  });

  statusUebergaengeOptionsSig = computed(() => {
    const rolesMap = this.permissionStore.rolesMapSig();
    const gesuchInfo = this.gesuchStore.gesuchInfo().data;
    const gesuchStatus = gesuchInfo?.gesuchStatus;

    const sbCanBearbeitungAbschliessen =
      this.dokumentsStore.dokumenteCanFlagsSig().sbCanBearbeitungAbschliessen;
    const validations =
      this.einreichnenStore.validationViewSig().invalidFormularProps
        .validations;

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
      case 'EINGEREICHT':
      case 'BEARBEITUNG_ABSCHLIESSEN':
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
      case 'BEREIT_FUER_BEARBEITUNG':
        SharedUiKommentarDialogComponent.openOptional(this.dialog, {
          entityId: gesuchTrancheId,
          titleKey: `sachbearbeitung-app.header.status-uebergang.BEREIT_FUER_BEARBEITUNG.title`,
          messageKey: `sachbearbeitung-app.header.status-uebergang.BEREIT_FUER_BEARBEITUNG.message`,
          placeholderKey: `sachbearbeitung-app.header.status-uebergang.BEREIT_FUER_BEARBEITUNG.placeholder`,
          confirmKey: `sachbearbeitung-app.header.status-uebergang.BEREIT_FUER_BEARBEITUNG.confirm`,
        })
          .afterClosed()
          .pipe(takeUntilDestroyed(this.destroyRef))
          .subscribe((result) => {
            if (result) {
              this.gesuchStore.setStatus$['BEREIT_FUER_BEARBEITUNG']({
                gesuchTrancheId,
                text: result.kommentar,
              });
            }
          });
        break;
      case 'ZURUECKWEISEN':
        SharedUiKommentarDialogComponent.open(this.dialog, {
          entityId: gesuchTrancheId,
          titleKey: `sachbearbeitung-app.header.status-uebergang.ZURUECKWEISEN.title`,
          messageKey: `sachbearbeitung-app.header.status-uebergang.ZURUECKWEISEN.message`,
          placeholderKey: `sachbearbeitung-app.header.status-uebergang.ZURUECKWEISEN.placeholder`,
          confirmKey: `sachbearbeitung-app.header.status-uebergang.ZURUECKWEISEN.confirm`,
        })
          .afterClosed()
          .pipe(takeUntilDestroyed(this.destroyRef))
          .subscribe((result) => {
            if (result) {
              this.gesuchStore.setStatus$['ZURUECKWEISEN']({
                gesuchTrancheId,
                text: result.kommentar,
                onSuccess: (newGesuchTrancheId) => {
                  if (gesuchTrancheId !== newGesuchTrancheId) {
                    this.router.navigate([
                      'gesuch',
                      'info',
                      gesuchId,
                      'tranche',
                      newGesuchTrancheId,
                    ]);
                  } else {
                    this.store.dispatch(
                      SharedDataAccessGesuchEvents.loadGesuch(),
                    );
                  }
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
        })
          .afterClosed()
          .pipe(takeUntilDestroyed(this.destroyRef))
          .subscribe((result) => {
            if (result) {
              this.gesuchStore.setStatus$[nextStatus]({
                gesuchTrancheId,
                grundId: result.entityId,
                kanton: result.kanton,
              });
            }
          });
        break;
      default:
        assertUnreachable(nextStatus);
    }
  }

  createTranche() {
    const gesuchId = this.gesuchIdSig();
    const periode = this.gesuchStore.gesuchInfo().data;
    if (!gesuchId || !periode) return;

    SharedDialogTrancheErstellenComponent.open(this.dialog, {
      forAenderung: false,
      gesuchId,
      minDate: new Date(periode.startDate),
      maxDate: new Date(periode.endDate),
    })
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe();
  }
}
