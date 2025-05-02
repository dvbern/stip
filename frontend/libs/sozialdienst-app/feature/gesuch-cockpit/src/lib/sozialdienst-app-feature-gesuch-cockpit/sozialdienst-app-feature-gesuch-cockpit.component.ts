/* eslint-disable @nx/enforce-module-boundaries */
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

import { DashboardStore } from '@dv/gesuch-app/data-access/dashboard';
import { GesuchAppDialogCreateAusbildungComponent } from '@dv/gesuch-app/dialog/create-ausbildung';
import { GesuchAppFeatureDelegierenDialogComponent } from '@dv/gesuch-app/feature/delegieren-dialog';
import {
  GesuchAppUiDashboardAusbildungComponent,
  GesuchAppUiDashboardCompactAusbildungComponent,
} from '@dv/gesuch-app/ui/dashboard';
import { selectSharedDataAccessBenutzer } from '@dv/shared/data-access/benutzer';
import { FallStore } from '@dv/shared/data-access/fall';
import {
  SharedDataAccessGesuchEvents,
  selectLastUpdate,
} from '@dv/shared/data-access/gesuch';
import { GesuchAenderungStore } from '@dv/shared/data-access/gesuch-aenderung';
import { SharedDataAccessLanguageEvents } from '@dv/shared/data-access/language';
import { SozialdienstStore } from '@dv/shared/data-access/sozialdienst';
import { SharedModelGsAusbildungView } from '@dv/shared/model/ausbildung';
import {
  AenderungMelden,
  Gesuchsperiode,
  Sozialdienst,
} from '@dv/shared/model/gesuch';
import { Language } from '@dv/shared/model/language';
import { compareById } from '@dv/shared/model/type-util';
import { SharedPatternAppHeaderComponent } from '@dv/shared/pattern/app-header';
import { SharedPatternMobileSidenavComponent } from '@dv/shared/pattern/mobile-sidenav';
import { SharedUiAenderungMeldenDialogComponent } from '@dv/shared/ui/aenderung-melden-dialog';
import { SharedUiClearButtonComponent } from '@dv/shared/ui/clear-button';
import { SharedUiConfirmDialogComponent } from '@dv/shared/ui/confirm-dialog';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiNotificationsComponent } from '@dv/shared/ui/notifications';

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
    SharedUiClearButtonComponent,
    SharedUiNotificationsComponent,
    GesuchAppUiDashboardAusbildungComponent,
    GesuchAppUiDashboardCompactAusbildungComponent,
  ],
  templateUrl: './sozialdienst-app-feature-gesuch-cockpit.component.html',
  styleUrl: './sozialdienst-app-feature-gesuch-cockpit.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SozialdienstAppFeatureGesuchCockpitComponent {
  private sidenavSig = viewChild.required(MatSidenav);
  closeMenuSig = input<{ value: boolean } | null>(null, { alias: 'closeMenu' });

  private store = inject(Store);
  private dialog = inject(MatDialog);
  private benutzerSig = this.store.selectSignal(selectSharedDataAccessBenutzer);

  // @Input({ required: true }) tranche?: GesuchTrancheSlim;
  fallStore = inject(FallStore);
  dashboardStore = inject(DashboardStore);
  gesuchAenderungStore = inject(GesuchAenderungStore);
  sozialdienstStore = inject(SozialdienstStore);
  benutzerNameSig = computed(() => {
    const benutzer = this.benutzerSig();
    return `${benutzer?.vorname} ${benutzer?.nachname}`;
  });

  private gotNewFallSig = computed(() => {
    return this.fallStore.currentFallViewSig()?.id;
  });
  private gesuchUpdatedSig = this.store.selectSignal(selectLastUpdate);

  constructor() {
    effect(() => {
      if (this.closeMenuSig()?.value) {
        this.sidenavSig().close();
      }
    });

    this.store.dispatch(SharedDataAccessGesuchEvents.reset());
    this.fallStore.loadCurrentFall$();
    this.sozialdienstStore.loadAvailableSozialdienste$();

    effect(
      () => {
        const fallId = this.gotNewFallSig();

        if (fallId) {
          this.dashboardStore.loadDashboard$();
        }
      },
      { allowSignalWrites: true },
    );

    effect(
      () => {
        if (this.gesuchUpdatedSig()) {
          this.dashboardStore.loadDashboard$();
        }
      },
      { allowSignalWrites: true },
    );
  }

  compareById = compareById;

  createAusbildung(fallId: string) {
    GesuchAppDialogCreateAusbildungComponent.open(
      this.dialog,
      fallId,
    ).subscribe(() => {
      this.dashboardStore.loadDashboard$();
    });
  }

  trackByPerioden(
    _index: number,
    periode: Gesuchsperiode & { gesuchLoading: boolean },
  ) {
    return periode.id + periode.gesuchLoading;
  }

  trackByIndex(index: number) {
    return index;
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
      title: 'gesuch-app.dashboard.ausbildung.delete.dialog.title',
      message: 'gesuch-app.dashboard.ausbildung.delete.dialog.message',
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
      title: 'gesuch-app.dashboard.gesuch.delete.dialog.title',
      message: 'gesuch-app.dashboard.gesuch.delete.dialog.message',
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
      title: 'gesuch-app.dashboard.aenderung.delete.dialog.title',
      message: 'gesuch-app.dashboard.aenderung.delete.dialog.message',
      cancelText: 'shared.cancel',
      confirmText: 'shared.form.delete',
    })
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          this.gesuchAenderungStore.deleteGesuchAenderung$({
            aenderungId,
            onSuccess: () => {
              this.dashboardStore.loadDashboard$();
            },
          });
        }
      });
  }

  delegiereSozialdienst(fallId: string, sozialdienst: Sozialdienst) {
    GesuchAppFeatureDelegierenDialogComponent.open(this.dialog)
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          const req = {
            sozialdienstId: sozialdienst.id,
            fallId,
            delegierungCreate: result,
          };

          this.sozialdienstStore.fallDelegieren$({
            req,
            onSuccess: () => {
              this.dashboardStore.loadDashboard$();
            },
          });
        }
      });
  }
}
