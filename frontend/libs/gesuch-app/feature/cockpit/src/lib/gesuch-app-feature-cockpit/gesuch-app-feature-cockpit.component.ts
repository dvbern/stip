import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatMenuModule } from '@angular/material/menu';
import { MatSelectModule } from '@angular/material/select';
import { RouterLink } from '@angular/router';
import { Store } from '@ngrx/store';

import { GesuchAppFeatureDelegierenDialogComponent } from '@dv/gesuch-app/feature/delegieren-dialog';
import { GesuchAppUiAdvTranslocoDirective } from '@dv/gesuch-app/ui/adv-transloco-directive';
import { selectSharedDataAccessBenutzer } from '@dv/shared/data-access/benutzer';
import { DarlehenStore } from '@dv/shared/data-access/darlehen';
import { DashboardStore } from '@dv/shared/data-access/dashboard';
import { FallStore } from '@dv/shared/data-access/fall';
import {
  SharedDataAccessGesuchEvents,
  selectLastUpdate,
} from '@dv/shared/data-access/gesuch';
import { GesuchAenderungStore } from '@dv/shared/data-access/gesuch-aenderung';
import { SharedDataAccessLanguageEvents } from '@dv/shared/data-access/language';
import { SozialdienstStore } from '@dv/shared/data-access/sozialdienst';
import { SharedDialogCreateAusbildungComponent } from '@dv/shared/dialog/create-ausbildung';
import { SharedDialogTrancheErstellenComponent } from '@dv/shared/dialog/tranche-erstellen';
import { GlobalNotificationStore } from '@dv/shared/global/notification';
import { SharedModelGsAusbildungView } from '@dv/shared/model/ausbildung';
import {
  AenderungMelden,
  Gesuchsperiode,
  Sozialdienst,
} from '@dv/shared/model/gesuch';
import { Language } from '@dv/shared/model/language';
import { compareById } from '@dv/shared/model/type-util';
import { SharedPatternAppHeaderPartsDirective } from '@dv/shared/pattern/app-header';
import { SharedPatternMainLayoutComponent } from '@dv/shared/pattern/main-layout';
import { SharedUiClearButtonComponent } from '@dv/shared/ui/clear-button';
import { SharedUiConfirmDialogComponent } from '@dv/shared/ui/confirm-dialog';
import { SharedUiDarlehenMenuComponent } from '@dv/shared/ui/darlehen-menu';
import {
  SharedUiDashboardAusbildungComponent,
  SharedUiDashboardCompactAusbildungComponent,
} from '@dv/shared/ui/dashboard';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiNotificationsComponent } from '@dv/shared/ui/notifications';
import { SharedUiVersionTextComponent } from '@dv/shared/ui/version-text';
import { provideMaterialDefaultOptions } from '@dv/shared/util/form';

import { selectGesuchAppFeatureCockpitView } from './gesuch-app-feature-cockpit.selector';

@Component({
  selector: 'dv-gesuch-app-feature-cockpit',
  imports: [
    MatSelectModule,
    RouterLink,
    CommonModule,
    SharedPatternMainLayoutComponent,
    SharedPatternAppHeaderPartsDirective,
    SharedUiIconChipComponent,
    SharedUiVersionTextComponent,
    SharedUiClearButtonComponent,
    SharedUiNotificationsComponent,
    SharedUiDashboardAusbildungComponent,
    SharedUiDashboardCompactAusbildungComponent,
    SharedUiDarlehenMenuComponent,
    GesuchAppUiAdvTranslocoDirective,
    MatMenuModule,
  ],
  providers: [
    SozialdienstStore,
    provideMaterialDefaultOptions({ subscriptSizing: 'dynamic' }),
  ],
  templateUrl: './gesuch-app-feature-cockpit.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GesuchAppFeatureCockpitComponent {
  private store = inject(Store);
  private dialog = inject(MatDialog);
  private benutzerSig = this.store.selectSignal(selectSharedDataAccessBenutzer);

  fallStore = inject(FallStore);
  darlehenStore = inject(DarlehenStore);
  dashboardStore = inject(DashboardStore);
  gesuchAenderungStore = inject(GesuchAenderungStore);
  globalNotificationStore = inject(GlobalNotificationStore);
  sozialdienstStore = inject(SozialdienstStore);
  cockpitViewSig = this.store.selectSignal(selectGesuchAppFeatureCockpitView);
  benutzerNameSig = computed(() => {
    const benutzer = this.benutzerSig();
    return `${benutzer?.vorname} ${benutzer?.nachname}`;
  });
  availableSozialdiensteSig = computed(() => {
    const sozialdienste = this.sozialdienstStore.availableSozialdienste()?.data;
    const delegierterSozialdienst =
      this.dashboardStore.dashboardViewSig()?.delegierung?.sozialdienst;

    return sozialdienste?.filter(
      (sozialdienst) =>
        sozialdienst.aktiv || sozialdienst.id === delegierterSozialdienst?.id,
    );
  });

  private gotNewFallSig = computed(() => {
    return this.fallStore.currentFallViewSig()?.id;
  });
  private gesuchUpdatedSig = this.store.selectSignal(selectLastUpdate);

  constructor() {
    this.store.dispatch(SharedDataAccessGesuchEvents.reset());
    this.fallStore.loadCurrentFall$();
    this.sozialdienstStore.loadAvailableSozialdienste$();

    effect(() => {
      const fallId = this.gotNewFallSig();

      if (fallId) {
        this.darlehenStore.getAllDarlehenGs$({ fallId });
        this.dashboardStore.loadDashboard$();
      }
    });

    effect(() => {
      if (this.gesuchUpdatedSig()) {
        this.dashboardStore.loadDashboard$();
      }
    });
  }

  compareById = compareById;

  createAusbildung(fallId: string) {
    SharedDialogCreateAusbildungComponent.open(this.dialog, fallId)
      .afterClosed()
      .subscribe(() => {
        this.dashboardStore.loadDashboard$();
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
    SharedDialogTrancheErstellenComponent.open(this.dialog, {
      type: 'createAenderung',
      id,
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
              this.globalNotificationStore.createSuccessNotification({
                messageKey: 'shared.dashboard.gesuch.delegieren.success',
              });
              this.dashboardStore.loadDashboard$();
            },
          });
        }
      });
  }
}
