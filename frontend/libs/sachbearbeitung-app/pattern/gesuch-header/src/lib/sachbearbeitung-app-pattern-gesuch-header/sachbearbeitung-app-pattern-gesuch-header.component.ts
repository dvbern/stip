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

import { SharedDataAccessGesuchEvents } from '@dv/shared/data-access/gesuch';
import { GesuchAenderungStore } from '@dv/shared/data-access/gesuch-aenderung';
import { SharedModelGesuch } from '@dv/shared/model/gesuch';
import {
  SharedPatternAppHeaderComponent,
  SharedPatternAppHeaderPartsDirective,
} from '@dv/shared/pattern/app-header';
import { SharedUiAenderungMeldenDialogComponent } from '@dv/shared/ui/aenderung-melden-dialog';
import { SharedUiKommentarDialogComponent } from '@dv/shared/ui/kommentar-dialog';

type StatusUebergang = 'BEARBEITUNG_ABSCHIESSEN' | 'ZURUECKWEISEN';

const statusUebergaenge = {
  BEARBEITUNG_ABSCHIESSEN: {
    icon: 'check',
    titleKey: 'BEARBEITUNG_ABSCHIESSEN',
    typ: 'BEARBEITUNG_ABSCHIESSEN' as const,
  },
  ZURUECKWEISEN: {
    icon: 'undo',
    titleKey: 'ZURUECKWEISEN',
    typ: 'ZURUECKWEISEN' as const,
  },
} satisfies Record<StatusUebergang, unknown>;

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
  ],
  templateUrl: './sachbearbeitung-app-pattern-gesuch-header.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppPatternGesuchHeaderComponent {
  currentGesuchSig = input.required<SharedModelGesuch | null>({
    alias: 'currentGesuch',
  });
  navClickedSig = input.required<{ value: unknown }>({ alias: 'navClicked' });
  store = inject(Store);
  router = inject(Router);
  destroyRef = inject(DestroyRef);
  private dialog = inject(MatDialog);
  gesuchAenderungStore = inject(GesuchAenderungStore);

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

  statusUebergaengeMenu = Object.values(statusUebergaenge);

  constructor() {
    effect(
      () => {
        const gesuchId = this.currentGesuchSig()?.id;

        if (gesuchId) {
          this.gesuchAenderungStore.getAllTranchenForGesuch$({ gesuchId });
        }
      },
      { allowSignalWrites: true },
    );
  }

  canSetToBearbeitungSig = computed(() => {
    const gesuch = this.currentGesuchSig();
    const status = gesuch?.gesuchStatus;
    if (!status) {
      return false;
    }
    return status === 'BEREIT_FUER_BEARBEITUNG';
  });

  setToBearbeitung() {
    this.store.dispatch(SharedDataAccessGesuchEvents.setGesuchToBearbeitung());
  }

  canEndBearbeitungOrRejectSig = computed(() => {
    const gesuch = this.currentGesuchSig();
    const status = gesuch?.gesuchStatus;
    if (!status) {
      return false;
    }
    return status === 'IN_BEARBEITUNG_SB';
  });

  setStatusUebergang(status: StatusUebergang) {
    const gesuchId = this.currentGesuchSig()?.id;

    if (status === 'BEARBEITUNG_ABSCHIESSEN') {
      this.store.dispatch(
        SharedDataAccessGesuchEvents.setGesuchBearbeitungAbschliessen(),
      );
    }

    if (status === 'ZURUECKWEISEN' && gesuchId) {
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
