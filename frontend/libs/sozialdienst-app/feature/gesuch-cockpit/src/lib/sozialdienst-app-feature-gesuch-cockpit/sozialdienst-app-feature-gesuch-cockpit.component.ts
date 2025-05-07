import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
  input,
  viewChild,
} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSelectModule } from '@angular/material/select';
import { MatSidenav, MatSidenavModule } from '@angular/material/sidenav';
import { Store } from '@ngrx/store';
import { TranslatePipe } from '@ngx-translate/core';

import { selectSharedDataAccessBenutzer } from '@dv/shared/data-access/benutzer';
import {
  SharedDataAccessGesuchEvents,
  selectLastUpdate,
} from '@dv/shared/data-access/gesuch';
import { GesuchAenderungStore } from '@dv/shared/data-access/gesuch-aenderung';
import { SharedDataAccessLanguageEvents } from '@dv/shared/data-access/language';
import { SharedDialogCreateAusbildungComponent } from '@dv/shared/dialog/create-ausbildung';
import { SharedModelGsAusbildungView } from '@dv/shared/model/ausbildung';
import { AenderungMelden, Gesuchsperiode } from '@dv/shared/model/gesuch';
import { Language } from '@dv/shared/model/language';
import { compareById } from '@dv/shared/model/type-util';
import { SharedPatternAppHeaderComponent } from '@dv/shared/pattern/app-header';
import { SharedPatternMobileSidenavComponent } from '@dv/shared/pattern/mobile-sidenav';
import { SharedUiAenderungMeldenDialogComponent } from '@dv/shared/ui/aenderung-melden-dialog';
import { SharedUiConfirmDialogComponent } from '@dv/shared/ui/confirm-dialog';
import {
  SharedUiDashboardAusbildungComponent,
  SharedUiDashboardCompactAusbildungComponent,
} from '@dv/shared/ui/dashboard';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiNotificationsComponent } from '@dv/shared/ui/notifications';
import { SozDashboardStore } from '@dv/sozialdienst-app/data-access/soz-dashboard';

@Component({
  selector: 'dv-sozialdienst-app-feature-gesuch-cockpit',
  standalone: true,
  imports: [
    CommonModule,
    MatSidenavModule,
    SharedPatternMobileSidenavComponent,
    SharedPatternAppHeaderComponent,
    TranslatePipe,
    MatSelectModule,
    SharedUiIconChipComponent,
    SharedUiNotificationsComponent,
    SharedUiDashboardAusbildungComponent,
    SharedUiDashboardCompactAusbildungComponent,
  ],
  templateUrl: './sozialdienst-app-feature-gesuch-cockpit.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [SozDashboardStore],
})
export class SozialdienstAppFeatureGesuchCockpitComponent {
  private sidenavSig = viewChild.required(MatSidenav);
  closeMenuSig = input<{ value: boolean } | null>(null, { alias: 'closeMenu' });

  private store = inject(Store);
  private dialog = inject(MatDialog);
  private benutzerSig = this.store.selectSignal(selectSharedDataAccessBenutzer);

  fallIdSig = input<string | undefined>(undefined, { alias: 'id' });

  dashboardStore = inject(SozDashboardStore);
  gesuchAenderungStore = inject(GesuchAenderungStore);
  benutzerNameSig = computed(() => {
    const benutzer = this.benutzerSig();
    return `${benutzer?.vorname} ${benutzer?.nachname}`;
  });

  private gesuchUpdatedSig = this.store.selectSignal(selectLastUpdate);

  constructor() {
    effect(() => {
      if (this.closeMenuSig()?.value) {
        this.sidenavSig().close();
      }
    });

    this.store.dispatch(SharedDataAccessGesuchEvents.reset());

    effect(
      () => {
        const fallId = this.fallIdSig();

        if (fallId) {
          this.dashboardStore.loadCachedSozDashboard$({ fallId });
        }
      },
      { allowSignalWrites: true },
    );

    effect(
      () => {
        const fallId = this.fallIdSig();

        if (this.gesuchUpdatedSig() && fallId) {
          this.dashboardStore.loadCachedSozDashboard$({ fallId });
        }
      },
      { allowSignalWrites: true },
    );
  }

  compareById = compareById;

  createAusbildung(fallId: string) {
    SharedDialogCreateAusbildungComponent.open(this.dialog, fallId)
      .afterClosed()
      .subscribe(() => {
        this.dashboardStore.loadCachedSozDashboard$({ fallId });
      });
  }

  trackByPerioden(
    _index: number,
    periode: Gesuchsperiode & { gesuchLoading: boolean },
  ) {
    return periode.id + periode.gesuchLoading;
  }

  handleLanguageChangeHeader(language: Language) {
    this.store.dispatch(
      SharedDataAccessLanguageEvents.headerMenuSelectorChange({ language }),
    );
  }

  aenderungMelden(melden: AenderungMelden) {
    const {
      gesuch: { id, startDate, endDate },
    } = melden;
    SharedUiAenderungMeldenDialogComponent.open(this.dialog, {
      minDate: new Date(startDate),
      maxDate: new Date(endDate),
    })
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          this.gesuchAenderungStore.createGesuchAenderung$({
            gesuchId: id,
            createAenderungsantragRequest: result,
          });
        }
      });
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
              this.dashboardStore.loadCachedSozDashboard$({ fallId });
            },
          });
        }
      });
  }
}
