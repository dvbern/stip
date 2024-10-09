import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
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
  navClickedSig = input.required<{ value: unknown }>({ alias: 'navClicked' });
  store = inject(Store);
  router = inject(Router);
  destroyRef = inject(DestroyRef);
  private dialog = inject(MatDialog);
  gesuchAenderungStore = inject(GesuchAenderungStore);
  dokumentsStore = inject(DokumentsStore);

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

  constructor() {
    effect(
      () => {
        const gesuchId = this.currentGesuchSig()?.id;
        if (gesuchId) {
          this.gesuchAenderungStore.getAllTranchenForGesuch$({ gesuchId });
        }

        const trancheId = this.currentGesuchSig()?.gesuchTrancheToWorkWith.id;
        if (trancheId) {
          this.dokumentsStore.getDokumenteAndRequired$(trancheId);
        }
      },
      { allowSignalWrites: true },
    );
  }

  canSetToBearbeitungSig = computed(() => {
    const gesuchStatus = this.currentGesuchSig()?.gesuchStatus;
    if (!gesuchStatus) {
      return false;
    }

    return gesuchStatus === 'BEREIT_FUER_BEARBEITUNG';
  });

  canSetCurrentStatusUebergangSig = computed(() => {
    const gesuchStatus = this.currentGesuchSig()?.gesuchStatus;
    if (!gesuchStatus) {
      return false;
    }

    const hasAcceptedAllDokuments =
      this.dokumentsStore.hasAcceptedAllDokumentsSig();

    switch (gesuchStatus) {
      case 'IN_BEARBEITUNG_SB':
        // 'DOKUMENTE_OFFEN' could be used to notify the SB user that there are still documents to be accepted
        return hasAcceptedAllDokuments ? gesuchStatus : 'DOKUMENTE_OFFEN';
      case 'IN_FREIGABE':
      case 'VERSANDBEREIT':
        return gesuchStatus;
      default:
        return 'NO_UEBERGANG';
    }
  });

  setToBearbeitung() {
    this.store.dispatch(SharedDataAccessGesuchEvents.setGesuchToBearbeitung());
  }

  statusUebergaengeOptionsSig = computed(() => {
    const gesuch = this.currentGesuchSig();
    if (!gesuch) {
      return [];
    }

    return StatusUebergaengeMap[gesuch.gesuchStatus]?.map(
      (status) => StatusUebergaengeOptions[status],
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
