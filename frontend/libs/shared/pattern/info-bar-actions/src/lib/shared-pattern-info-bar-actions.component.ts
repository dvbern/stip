import {
  Component,
  DestroyRef,
  Injector,
  computed,
  effect,
  inject,
  runInInjectionContext,
  signal,
} from '@angular/core';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { MatDialog } from '@angular/material/dialog';
import { MatMenuModule } from '@angular/material/menu';
import { MatTooltipModule } from '@angular/material/tooltip';
import { ActivatedRoute, Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { format } from 'date-fns';
import { firstValueFrom, map, startWith } from 'rxjs';

import { translatableShared } from '@dv/shared/assets/i18n';
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
  InBearbeitungSbReason,
  getTrancheRoute,
} from '@dv/shared/model/gesuch';
import { getGesuchPermissions } from '@dv/shared/model/permission-state';
import {
  createUrlChecksSig,
  urlAfterNavigationEnd,
} from '@dv/shared/model/router';
import { isDefined } from '@dv/shared/model/type-util';
import {
  hideAktionenRoutes,
  notGesuchRoute,
} from '@dv/shared/model/ui-constants';
import { SharedUiAdvTranslocoDirective } from '@dv/shared/ui/adv-transloco-directive';
import { SharedUiKommentarDialogComponent } from '@dv/shared/ui/kommentar-dialog';
import {
  StatusUebergaengeMap,
  StatusUebergaengeOptions,
  StatusUebergang,
} from '@dv/shared/util/gesuch';
import { getQueryParamValueSig } from '@dv/shared/util/navigation';
import { isPending } from '@dv/shared/util/remote-data';
import { ExportView } from '@dv/shared/util-data-access/export-tranche';

@Component({
  selector: 'dv-shared-pattern-info-bar-actions',
  imports: [MatTooltipModule, MatMenuModule, SharedUiAdvTranslocoDirective],
  templateUrl: './shared-pattern-info-bar-actions.component.html',
})
export class SharedPatternInfoBarActionsComponent {
  private config = inject(SharedModelCompileTimeConfig);
  private store = inject(Store);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private destroyRef = inject(DestroyRef);
  private injector = inject(Injector);
  private dialog = inject(MatDialog);
  private globalNotificationStore = inject(GlobalNotificationStore);
  private permissionStore = inject(PermissionStore);
  private einreichenStore = inject(EinreichenStore);
  private gesuchAenderungStore = inject(GesuchAenderungStore);
  private gesuchHeaderStore = inject(GesuchHeaderStore);
  private routeUrlSig = toSignal(
    urlAfterNavigationEnd(this.router).pipe(
      map(() => this.router.routerState.snapshot.url),
      startWith(this.router.routerState.snapshot.url),
    ),
  );
  private gesuchCacheSig = this.store.selectSignal(
    selectSharedDataAccessGesuchCache,
  );

  private availableTrancheInteractionSig = computed(() => {
    const rolesMap = this.permissionStore.rolesMapSig();
    const gesuchStatus =
      this.gesuchHeaderStore.viewSig()?.gesuchInfo?.state.gesuchStatus;

    if (gesuchStatus === 'IN_BEARBEITUNG_SB' && rolesMap.V0_Sachbearbeiter) {
      return 'CREATE_TRANCHE';
    }

    return null;
  });
  private aenderungActionsSig = computed(() => {
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

  private statusUebergaengeOptionsSig = computed(() => {
    const rolesMap = this.permissionStore.rolesMapSig();
    const validations =
      this.einreichenStore.validationViewSig().invalidFormularProps.validations;

    const {
      gesuchStatus,
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

  private exportValuesSig = computed<ExportView | undefined>(() => {
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

  private additionalLoadingSig = signal(false);

  gesuchIdSig = this.store.selectSignal(selectRouteGesuchId);
  trancheIdSig = this.store.selectSignal(selectRouteTrancheId);
  isExportingSig = signal(false);
  isLoadingSig = computed(() => {
    return (
      isPending(this.gesuchHeaderStore.header()) || this.additionalLoadingSig()
    );
  });
  isAenderungUpdatingSig = computed(() => {
    return (
      this.isLoadingSig() ||
      isPending(this.gesuchAenderungStore.cachedGesuchAenderung())
    );
  });

  routeChecksSig = createUrlChecksSig(
    this.router,
    `infos`,
    'darlehen',
    `${getTrancheRoute('aenderung')}`,
    `${getTrancheRoute('initial')}`,
    `${getTrancheRoute('eingereicht')}`,
  );

  berechnungIdSig = getQueryParamValueSig(this.route, 'berechnungId');
  isActionRouteSig = computed(() => {
    const url = this.routeUrlSig();
    return !hideAktionenRoutes.some(
      (route) => url?.includes(`/${route}/`) || this.berechnungIdSig(),
    );
  });

  actionMenuOptionsSig = computed(() => {
    const statusUebergaenge = this.statusUebergaengeOptionsSig();
    const availableTrancheInteraction = this.availableTrancheInteractionSig();
    const canExport = !!this.exportValuesSig();
    const routes = this.routeChecksSig();
    const isGesuchRoute = !notGesuchRoute.some((route) =>
      routes.matched.includes(route),
    );
    const showGesuchActions =
      isGesuchRoute && !routes.isEingereicht && !routes.isInitial;
    const showAenderungActions = !!routes.isAenderung;
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

  async setStatusUebergang(
    nextStatus: StatusUebergang,
    gesuchId?: string,
    gesuchTrancheId?: string,
  ) {
    if (!gesuchId || !gesuchTrancheId || !this.config.isSachbearbeitungApp) {
      return;
    }

    const module =
      // A feature that is only called if SB App
      // eslint-disable-next-line @nx/enforce-module-boundaries
      await import('@dv/sachbearbeitung-app/util-data-access/gesuch-actions');
    const exportTrancheService = runInInjectionContext(this.injector, () =>
      inject(module.GesuchActionsService),
    );

    exportTrancheService.setStatusUebergang(
      nextStatus,
      gesuchId,
      gesuchTrancheId,
    );

    effect(() => exportTrancheService.isLoadingSig, {
      injector: this.injector,
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
