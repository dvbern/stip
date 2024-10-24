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
  input,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatDialog } from '@angular/material/dialog';
import { MatMenuModule } from '@angular/material/menu';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';

import { DokumentsStore } from '@dv/shared/data-access/dokuments';
import { SharedDataAccessGesuchEvents } from '@dv/shared/data-access/gesuch';
import { GesuchAenderungStore } from '@dv/shared/data-access/gesuch-aenderung';
import { SharedModelGesuch } from '@dv/shared/model/gesuch';
import {
  SharedPatternAppHeaderComponent,
  SharedPatternAppHeaderPartsDirective,
} from '@dv/shared/pattern/app-header';
import { SharedUiAenderungMeldenDialogComponent } from '@dv/shared/ui/aenderung-melden-dialog';
import { SharedUiIconBadgeComponent } from '@dv/shared/ui/icon-badge';
import { SharedUiKommentarDialogComponent } from '@dv/shared/ui/kommentar-dialog';
import {
  StatusUebergaengeMap,
  StatusUebergaengeOptions,
  StatusUebergang,
} from '@dv/shared/util/gesuch';
import { assertUnreachable } from '@dv/shared/util-fn/type-guards';

@Component({
  selector: 'dv-sachbearbeitung-app-pattern-gesuch-header',
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    RouterLink,
    RouterLinkActive,
    MatMenuModule,
    SharedPatternAppHeaderComponent,
    SharedPatternAppHeaderPartsDirective,
    SharedUiIconBadgeComponent,
  ],
  templateUrl: './sachbearbeitung-app-pattern-gesuch-header.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppPatternGesuchHeaderComponent {
  currentGesuchSig = input.required<SharedModelGesuch | null>({
    alias: 'currentGesuch',
  });
  isLoadingSig = input.required<boolean>({ alias: 'isLoading' });
  store = inject(Store);
  router = inject(Router);
  destroyRef = inject(DestroyRef);
  private dialog = inject(MatDialog);
  gesuchAenderungStore = inject(GesuchAenderungStore);
  dokumentsStore = inject(DokumentsStore);

  @Output() openSidenav = new EventEmitter<void>();

  private hasAcceptedAllDocumentsSig = computed(() => {
    const gesuchStatus = this.currentGesuchSig()?.gesuchStatus;
    if (!gesuchStatus) {
      return false;
    }

    return this.dokumentsStore.hasAcceptedAllDokumentsSig();
  });

  isTrancheRouteSig = computed(() => {
    const gesuch = this.currentGesuchSig();
    if (!gesuch) {
      return false;
    }

    return (
      // If it is a tranche route
      this.router.url.includes('/tranche/') ||
      // or a normal current gesuch route
      (this.router.url.includes('/gesuch/') &&
        !this.router.url.includes('/aenderung/'))
    );
  });

  isAenderungRouteSig = computed(() => {
    const gesuch = this.currentGesuchSig();
    if (!gesuch) {
      return false;
    }

    return this.router.url.includes('/aenderung/');
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
        const gesuchId = this.currentGesuchSig()?.id;
        if (gesuchId) {
          this.gesuchAenderungStore.getAllTranchenForGesuch$({ gesuchId });
        }

        const gesuchTrancheId =
          this.currentGesuchSig()?.gesuchTrancheToWorkWith.id;
        if (gesuchTrancheId) {
          this.dokumentsStore.getGesuchDokumente$({ gesuchTrancheId });
        }
      },
      { allowSignalWrites: true },
    );
  }

  availableTrancheInteractionSig = computed(() => {
    const gesuchStatus = this.currentGesuchSig()?.gesuchStatus;

    switch (gesuchStatus) {
      case 'BEREIT_FUER_BEARBEITUNG':
        return 'SET_TO_BEARBEITUNG';
      case 'IN_BEARBEITUNG_SB':
        return 'CREATE_TRANCHE';
      default:
        return null;
    }
  });

  canSetToBearbeitungSig = computed(() => {
    const gesuchStatus = this.currentGesuchSig()?.gesuchStatus;
    if (!gesuchStatus) {
      return false;
    }

    return gesuchStatus === 'BEREIT_FUER_BEARBEITUNG';
  });

  setToBearbeitung() {
    this.store.dispatch(SharedDataAccessGesuchEvents.setGesuchToBearbeitung());
  }

  statusUebergaengeOptionsSig = computed(() => {
    const gesuch = this.currentGesuchSig();
    const hasAcceptedAllDokuments = this.hasAcceptedAllDocumentsSig();
    if (!gesuch) {
      return [];
    }

    return StatusUebergaengeMap[gesuch.gesuchStatus]?.map((status) =>
      StatusUebergaengeOptions[status]({ hasAcceptedAllDokuments }),
    );
  });

  setStatusUebergang(nextStatus: StatusUebergang) {
    switch (nextStatus) {
      case 'BEARBEITUNG_ABSCHLIESSEN':
        this.setStatusBearbeitungAbschliessen();
        break;
      case 'ZURUECKWEISEN':
        this.setStatusZurueckweisen();
        break;
      case 'VERFUEGT':
        this.setStatusVerfuegt();
        break;
      case 'BEREIT_FUER_BEARBEITUNG':
        this.setStatusBereitFuerBearbeitung();
        break;
      case 'VERSENDET':
        this.setGesuchVersendet();
        break;
      default:
        assertUnreachable(nextStatus);
    }
  }

  setGesuchVersendet() {
    this.store.dispatch(SharedDataAccessGesuchEvents.setGesuchVersendet());
  }

  private setStatusBearbeitungAbschliessen() {
    this.store.dispatch(
      SharedDataAccessGesuchEvents.setGesuchBearbeitungAbschliessen(),
    );
  }

  private setStatusVerfuegt() {
    this.store.dispatch(SharedDataAccessGesuchEvents.setGesuchVerfuegt());
  }

  private setStatusBereitFuerBearbeitung() {
    const gesuchId = this.currentGesuchSig()?.id;

    if (gesuchId) {
      SharedUiKommentarDialogComponent.open(this.dialog, {
        entityId: gesuchId,
        titleKey:
          'sachbearbeitung-app.header.status-uebergang.bereit-fuer-bearbeitung.title',
        messageKey:
          'sachbearbeitung-app.header.status-uebergang.bereit-fuer-bearbeitung.message',
        placeholderKey:
          'sachbearbeitung-app.header.status-uebergang.bereit-fuer-bearbeitung.placeholder',
        confirmKey:
          'sachbearbeitung-app.header.status-uebergang.bereit-fuer-bearbeitung.confirm',
      })
        .afterClosed()
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe((result) => {
          if (result) {
            this.store.dispatch(
              SharedDataAccessGesuchEvents.setGesuchBereitFuerBearbeitung({
                kommentar: result.kommentar,
              }),
            );
          }
        });
    }
  }

  private setStatusZurueckweisen() {
    const gesuchId = this.currentGesuchSig()?.id;

    if (gesuchId) {
      SharedUiKommentarDialogComponent.open(this.dialog, {
        entityId: gesuchId,
        titleKey:
          'sachbearbeitung-app.header.status-uebergang.zurueckweisen.title',
        messageKey:
          'sachbearbeitung-app.header.status-uebergang.zurueckweisen.message',
        placeholderKey:
          'sachbearbeitung-app.header.status-uebergang.zurueckweisen.placeholder',
        confirmKey:
          'sachbearbeitung-app.header.status-uebergang.zurueckweisen.confirm',
      })
        .afterClosed()
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe((result) => {
          if (result) {
            this.store.dispatch(
              SharedDataAccessGesuchEvents.setGesuchZurueckweisen({
                kommentar: result.kommentar,
              }),
            );
          }
        });
    }
  }

  createTranche() {
    const gesuch = this.currentGesuchSig();
    if (!gesuch) return;

    SharedUiAenderungMeldenDialogComponent.open(this.dialog, {
      minDate: new Date(gesuch.gesuchsperiode.gesuchsperiodeStart),
      maxDate: new Date(gesuch.gesuchsperiode.gesuchsperiodeStopp),
    })
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((result) => {
        if (result) {
          this.gesuchAenderungStore.createGesuchTrancheCopy$({
            gesuchId: gesuch.id,
            createGesuchTrancheRequest: result,
          });
        }
      });
  }
}
