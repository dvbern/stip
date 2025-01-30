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
import { MatDialog } from '@angular/material/dialog';
import { MatMenuModule } from '@angular/material/menu';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslatePipe } from '@ngx-translate/core';
import { filter, map } from 'rxjs';

import { GesuchStore } from '@dv/sachbearbeitung-app/data-access/gesuch';
import { SachbearbeitungAppUiGrundAuswahlDialogComponent } from '@dv/sachbearbeitung-app/ui/grund-auswahl-dialog';
import { DokumentsStore } from '@dv/shared/data-access/dokuments';
import {
  selectRouteId,
  selectRouteTrancheId,
  selectSharedDataAccessGesuchCache,
} from '@dv/shared/data-access/gesuch';
import { GesuchAenderungStore } from '@dv/shared/data-access/gesuch-aenderung';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import { GesuchInfo } from '@dv/shared/model/gesuch';
import { getGesuchPermissions } from '@dv/shared/model/permission-state';
import { urlAfterNavigationEnd } from '@dv/shared/model/router';
import { assertUnreachable, isDefined } from '@dv/shared/model/type-util';
import {
  SharedPatternAppHeaderComponent,
  SharedPatternAppHeaderPartsDirective,
} from '@dv/shared/pattern/app-header';
import { SharedUiAenderungMeldenDialogComponent } from '@dv/shared/ui/aenderung-melden-dialog';
import { SharedUiKommentarDialogComponent } from '@dv/shared/ui/kommentar-dialog';
import {
  StatusUebergaengeMap,
  StatusUebergaengeOptions,
  StatusUebergang,
} from '@dv/shared/util/gesuch';
import { isPending } from '@dv/shared/util/remote-data';

@Component({
  selector: 'dv-sachbearbeitung-app-pattern-gesuch-header',
  standalone: true,
  imports: [
    CommonModule,
    TranslatePipe,
    RouterLink,
    RouterLinkActive,
    MatMenuModule,
    SharedPatternAppHeaderComponent,
    SharedPatternAppHeaderPartsDirective,
  ],
  providers: [GesuchStore],
  templateUrl: './sachbearbeitung-app-pattern-gesuch-header.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppPatternGesuchHeaderComponent {
  private store = inject(Store);
  private router = inject(Router);
  private destroyRef = inject(DestroyRef);
  private dialog = inject(MatDialog);
  private dokumentsStore = inject(DokumentsStore);
  private gesuchStore = inject(GesuchStore);
  private config = inject(SharedModelCompileTimeConfig);
  gesuchAenderungStore = inject(GesuchAenderungStore);

  @Output() openSidenav = new EventEmitter<void>();

  gesuchIdSig = this.store.selectSignal(selectRouteId);
  gesuchTrancheIdSig = this.store.selectSignal(selectRouteTrancheId);
  private hasAcceptedAllDocumentsSig =
    this.dokumentsStore.hasAcceptedAllDokumentsSig;
  private otherGesuchInfoSourceSig = toSignal(
    this.store.select(selectSharedDataAccessGesuchCache).pipe(
      map(({ gesuch }) => gesuch),
      filter(isDefined),
      map(
        (gesuch) =>
          ({
            id: gesuch.id,
            startDate: gesuch.gesuchTrancheToWorkWith.gueltigAb,
            endDate: gesuch.gesuchTrancheToWorkWith.gueltigBis,
            gesuchStatus: gesuch.gesuchStatus,
            gesuchNummer: gesuch.gesuchNummer,
          }) satisfies GesuchInfo,
      ),
    ),
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
  gesuchPermissionsSig = computed(() => {
    const gesuchStatus = this.gesuchStore.gesuchInfo().data?.gesuchStatus;
    if (!gesuchStatus) {
      return {};
    }
    return getGesuchPermissions({ gesuchStatus }, this.config.appType);
  });
  isLoadingSig = computed(() => {
    return isPending(this.gesuchStore.gesuchInfo());
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
    effect(
      () => {
        const gesuchId = this.gesuchIdSig();
        if (gesuchId) {
          this.gesuchStore.loadGesuchInfo$({ gesuchId });
          this.gesuchAenderungStore.getAllTranchenForGesuch$({ gesuchId });
        }
      },
      { allowSignalWrites: true },
    );

    effect(
      () => {
        const gesuchTrancheId = this.gesuchTrancheIdSig();
        if (gesuchTrancheId) {
          this.dokumentsStore.getGesuchDokumente$({ gesuchTrancheId });
        }
      },
      { allowSignalWrites: true },
    );

    effect(
      () => {
        const gesuchInfo = this.otherGesuchInfoSourceSig();

        if (gesuchInfo) {
          this.gesuchStore.setGesuchInfo(gesuchInfo);
        }
      },
      { allowSignalWrites: true },
    );
  }

  availableTrancheInteractionSig = computed(() => {
    const gesuchStatus = this.gesuchStore.gesuchInfo().data?.gesuchStatus;

    if (gesuchStatus === 'IN_BEARBEITUNG_SB') {
      return 'CREATE_TRANCHE';
    } else {
      return null;
    }
  });

  statusUebergaengeOptionsSig = computed(() => {
    const gesuchStatus = this.gesuchStore.gesuchInfo().data?.gesuchStatus;
    const hasAcceptedAllDokuments = this.hasAcceptedAllDocumentsSig();
    if (!gesuchStatus) {
      return {};
    }

    const list = StatusUebergaengeMap[gesuchStatus]?.map((status) =>
      StatusUebergaengeOptions[status]({ hasAcceptedAllDokuments }),
    );

    return {
      list,
      isNotEmpty: !!list?.length,
    };
  });

  setStatusUebergang(nextStatus: StatusUebergang, gesuchTrancheId?: string) {
    if (!gesuchTrancheId) {
      return;
    }

    switch (nextStatus) {
      case 'SET_TO_BEARBEITUNG':
      case 'EINGEREICHT':
      case 'BEARBEITUNG_ABSCHLIESSEN':
      case 'VERFUEGT':
      case 'VERSENDET':
        this.gesuchStore.setStatus$[nextStatus]({ gesuchTrancheId });
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

    SharedUiAenderungMeldenDialogComponent.open(this.dialog, {
      minDate: new Date(periode.startDate),
      maxDate: new Date(periode.endDate),
    })
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((result) => {
        if (result) {
          this.gesuchAenderungStore.createGesuchTrancheCopy$({
            gesuchId,
            createGesuchTrancheRequest: result,
          });
        }
      });
  }
}
