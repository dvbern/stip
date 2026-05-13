/* eslint-disable @angular-eslint/no-input-rename */
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
  input,
} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatMenuModule } from '@angular/material/menu';
import { MatSelectModule } from '@angular/material/select';
import { MatSidenavModule } from '@angular/material/sidenav';
import { Router } from '@angular/router';
import { TranslocoDirective } from '@jsverse/transloco';
import { Store } from '@ngrx/store';

import { AusbildungStore } from '@dv/shared/data-access/ausbildung';
import { selectSharedDataAccessBenutzer } from '@dv/shared/data-access/benutzer';
import { DashboardStore } from '@dv/shared/data-access/dashboard';
import {
  SharedDataAccessGesuchEvents,
  selectLastUpdate,
} from '@dv/shared/data-access/gesuch';
import { GesuchAenderungStore } from '@dv/shared/data-access/gesuch-aenderung';
import { SharedDialogCreateAusbildungComponent } from '@dv/shared/dialog/create-ausbildung';
import { SharedDialogTrancheErstellenComponent } from '@dv/shared/dialog/tranche-erstellen';
import { SharedModelGsAusbildungView } from '@dv/shared/model/ausbildung';
import { AenderungMelden, Gesuchsperiode } from '@dv/shared/model/gesuch';
import { compareById } from '@dv/shared/model/type-util';
import { SharedUiConfirmDialogComponent } from '@dv/shared/ui/confirm-dialog';
import {
  SharedUiDashboardAusbildungComponent,
  SharedUiDashboardCompactAusbildungComponent,
} from '@dv/shared/ui/dashboard';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiNotificationsComponent } from '@dv/shared/ui/notifications';

@Component({
  selector: 'dv-sozialdienst-app-feature-gesuch-cockpit',
  imports: [
    MatSidenavModule,
    MatSelectModule,
    MatMenuModule,
    SharedUiIconChipComponent,
    SharedUiNotificationsComponent,
    SharedUiDashboardAusbildungComponent,
    SharedUiDashboardCompactAusbildungComponent,
    TranslocoDirective,
  ],
  templateUrl: './sozialdienst-app-feature-gesuch-cockpit.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SozialdienstAppFeatureGesuchCockpitComponent {
  fallIdSig = input<string | undefined>(undefined, { alias: 'fallId' });

  private store = inject(Store);
  private router = inject(Router);
  private ausbildungStore = inject(AusbildungStore);
  private dialog = inject(MatDialog);
  private benutzerSig = this.store.selectSignal(selectSharedDataAccessBenutzer);

  dashboardStore = inject(DashboardStore);
  gesuchAenderungStore = inject(GesuchAenderungStore);

  benutzerNameSig = computed(() => {
    const benutzer = this.benutzerSig();
    return `${benutzer?.vorname} ${benutzer?.nachname}`;
  });

  private gesuchUpdatedSig = this.store.selectSignal(selectLastUpdate);

  constructor() {
    this.store.dispatch(SharedDataAccessGesuchEvents.reset());

    effect(() => {
      const fallId = this.fallIdSig();

      if (fallId) {
        this.dashboardStore.loadSozialdienstDashboard$({ fallId });
      }
    });

    effect(() => {
      const fallId = this.fallIdSig();

      if (this.gesuchUpdatedSig() && fallId) {
        this.dashboardStore.loadSozialdienstDashboard$({ fallId });
      }
    });
  }

  compareById = compareById;

  createAusbildung(fallId: string, minAusbildungEnd: string | undefined) {
    SharedDialogCreateAusbildungComponent.open(
      this.dialog,
      fallId,
      minAusbildungEnd,
    )
      .afterClosed()
      .subscribe(() => {
        this.dashboardStore.loadSozialdienstDashboard$({ fallId });
      });
  }

  trackByPerioden(
    _index: number,
    periode: Gesuchsperiode & { gesuchLoading: boolean },
  ) {
    return periode.id + periode.gesuchLoading;
  }

  aenderungMelden(melden: AenderungMelden) {
    const {
      gesuch: { id, startDate, endDate },
    } = melden;
    SharedDialogTrancheErstellenComponent.open(this.dialog, {
      type: 'createAenderung',
      gesuchId: id,
      minDate: new Date(startDate),
      maxDate: new Date(endDate),
    })
      .afterClosed()
      .subscribe();
  }

  deleteAusbildung(ausbildung: SharedModelGsAusbildungView) {
    SharedUiConfirmDialogComponent.open(this.dialog, {
      title: 'shared.dashboard.ausbildung.delete.dialog.title',
      message: 'shared.dashboard.ausbildung.delete.dialog.message',
      cancelText: 'shared.cancel',
      confirmText: 'shared.form.delete',
    })
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          this.store.dispatch(
            SharedDataAccessGesuchEvents.deleteGesuch({
              gesuchId: ausbildung.gesuchs[0].id,
            }),
          );
          this.store.dispatch(SharedDataAccessGesuchEvents.reset());
        }
      });
  }

  ausbildungUnterbrechen(
    ausbildungId: string,
    openAusbildungUnterbruchAntragId?: string,
  ) {
    const fallId = this.fallIdSig();
    if (!fallId) {
      return;
    }
    if (openAusbildungUnterbruchAntragId) {
      this.router.navigate([
        '/',
        'ausbildung-unterbrechen',
        openAusbildungUnterbruchAntragId,
      ]);
    } else {
      this.ausbildungStore.createAusbildungUnterbruchAntrag$({
        ausbildungId,
        onSuccess: (unterbruchId) => {
          this.router.navigate([
            '/',
            'ausbildung-unterbrechen',
            unterbruchId,
            'fall',
            fallId,
          ]);
        },
      });
    }
  }

  deleteGesuch(gesuchId: string) {
    SharedUiConfirmDialogComponent.open(this.dialog, {
      title: 'shared.dashboard.gesuch.delete.dialog.title',
      message: 'shared.dashboard.gesuch.delete.dialog.message',
      cancelText: 'shared.cancel',
      confirmText: 'shared.form.delete',
    })
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          this.store.dispatch(
            SharedDataAccessGesuchEvents.deleteGesuch({ gesuchId }),
          );
        }
      });
  }

  deleteAenderung(aenderungId: string) {
    SharedUiConfirmDialogComponent.open(this.dialog, {
      title: 'shared.dashboard.aenderung.delete.dialog.title',
      message: 'shared.dashboard.aenderung.delete.dialog.message',
      cancelText: 'shared.cancel',
      confirmText: 'shared.form.delete',
    })
      .afterClosed()
      .subscribe((result) => {
        const fallId = this.fallIdSig();
        if (result && fallId) {
          this.gesuchAenderungStore.deleteGesuchAenderung$({
            aenderungId,
            onSuccess: () => {
              this.dashboardStore.loadSozialdienstDashboard$({ fallId });
            },
          });
        }
      });
  }
}
