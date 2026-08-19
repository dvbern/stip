import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
  input,
  untracked,
} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatMenuModule } from '@angular/material/menu';
import { ActivatedRoute, Router } from '@angular/router';
import { Store } from '@ngrx/store';

import { AusbildungStore } from '@dv/shared/data-access/ausbildung';
import {
  SharedDataAccessBenutzerApiEvents,
  selectSharedDataAccessBenutzer,
} from '@dv/shared/data-access/benutzer';
import { DarlehenStore } from '@dv/shared/data-access/darlehen';
import { DashboardStore } from '@dv/shared/data-access/dashboard';
import { FallStore } from '@dv/shared/data-access/fall';
import {
  SharedDataAccessGesuchEvents,
  selectLastUpdate,
} from '@dv/shared/data-access/gesuch';
import { GesuchAenderungStore } from '@dv/shared/data-access/gesuch-aenderung';
import { GesuchHeaderStore } from '@dv/shared/data-access/gesuch-header';
import { NavigationStore } from '@dv/shared/data-access/navigation';
import { SharedDialogCreateAusbildungComponent } from '@dv/shared/dialog/create-ausbildung';
import { SharedDialogNutzungsbedingungenComponent } from '@dv/shared/dialog/nutzungsbedingungen';
import { SharedDialogTrancheErstellenComponent } from '@dv/shared/dialog/tranche-erstellen';
import { GlobalNotificationStore } from '@dv/shared/global/notification';
import { GsDashboardActions } from '@dv/shared/model/ausbildung';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import { AenderungMelden, Gesuchsperiode } from '@dv/shared/model/gesuch';
import { assertUnreachable } from '@dv/shared/model/type-util';
import { SharedUiAdvTranslocoDirective } from '@dv/shared/ui/adv-transloco-directive';
import { SharedUiConfirmDialogComponent } from '@dv/shared/ui/confirm-dialog';
import { SharedUiDashboardAusbildungComponent } from '@dv/shared/ui/dashboard';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiVersionTextComponent } from '@dv/shared/ui/version-text';
import { isPending } from '@dv/shared/util/remote-data';

import { selectSharedFeatureGesuchstellerDashboardView } from './shared-feature-gesuchsteller-dashboard.selector';

@Component({
  selector: 'dv-shared-feature-gesuchsteller-dashboard',
  imports: [
    CommonModule,
    SharedUiIconChipComponent,
    SharedUiVersionTextComponent,
    SharedUiDashboardAusbildungComponent,
    SharedUiAdvTranslocoDirective,
    MatMenuModule,
  ],
  templateUrl: './shared-feature-gesuchsteller-dashboard.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchstellerDashboardComponent {
  // eslint-disable-next-line @angular-eslint/no-input-rename
  fallIdSig = input<string | undefined>(undefined, { alias: 'fallId' });
  private store = inject(Store);
  private dialog = inject(MatDialog);
  private router = inject(Router);
  private ausbildungStore = inject(AusbildungStore);
  private benutzerSig = this.store.selectSignal(selectSharedDataAccessBenutzer);

  config = inject(SharedModelCompileTimeConfig);
  route = inject(ActivatedRoute);

  navigationStore = inject(NavigationStore);

  fallStore = inject(FallStore);
  dashboardStore = inject(DashboardStore);
  gesuchAenderungStore = inject(GesuchAenderungStore);
  gesuchHeaderStore = inject(GesuchHeaderStore);
  globalNotificationStore = inject(GlobalNotificationStore);
  cockpitViewSig = this.store.selectSignal(
    selectSharedFeatureGesuchstellerDashboardView,
  );
  benutzerNameSig = computed(() => {
    const benutzer = this.benutzerSig();
    return `${benutzer?.vorname} ${benutzer?.nachname}`;
  });
  darlehenStore = inject(DarlehenStore);

  isUnterbruchOrAenderungPendingSig = computed(() => {
    const ausbildungUnterbruchPending = isPending(
      this.ausbildungStore.ausbildungUnterbrechenResponse(),
    );
    const aenderungPending = isPending(this.gesuchHeaderStore.header());
    return ausbildungUnterbruchPending || aenderungPending;
  });

  piaNameSig = computed(() => {
    const pia =
      this.dashboardStore.dashboardViewSig()?.currentDelegierung
        ?.persoenlicheAngaben;
    if (!pia) {
      return null;
    }
    return `${pia.vorname} ${pia.nachname}`;
  });

  private gesuchUpdatedSig = this.store.selectSignal(selectLastUpdate);

  private fallIdByAppTypeSig = computed(() => {
    switch (this.config.app.type) {
      case 'gesuch-app':
        return this.fallStore.currentFallViewSig()?.id;
      case 'sozialdienst-app':
        return this.fallIdSig();
    }
    return undefined;
  });

  constructor() {
    this.store.dispatch(SharedDataAccessGesuchEvents.reset());

    effect(() => {
      const fallId = this.fallIdByAppTypeSig();

      if (fallId) {
        this.dashboardStore.loadDashboard$({ fallId });
      }
    });

    effect(() => {
      if (this.gesuchUpdatedSig()) {
        const fallId = untracked(() => this.fallIdByAppTypeSig());
        if (fallId) {
          this.dashboardStore.loadDashboard$({ fallId });
        }
      }
    });
  }

  createAusbildung(fallId: string, minAusbildungEnd: string | undefined) {
    const nutzungsbedingungenAkzeptiert =
      this.benutzerSig()?.nutzungsbedingungenAkzeptiert;
    const benutzerId = this.benutzerSig()?.id;

    if (!nutzungsbedingungenAkzeptiert) {
      SharedDialogNutzungsbedingungenComponent.open(
        this.dialog,
        nutzungsbedingungenAkzeptiert ?? false,
      )
        .afterClosed()
        .subscribe((result) => {
          if (result && benutzerId) {
            this.store.dispatch(
              SharedDataAccessBenutzerApiEvents.nutzungsbedingungenAkzeptieren({
                benutzerId,
              }),
            );
          }
        });
    } else {
      SharedDialogCreateAusbildungComponent.open(
        this.dialog,
        fallId,
        minAusbildungEnd,
      )
        .afterClosed()
        .subscribe((result) => {
          const { gesuchId, gesuchTrancheId } = result ?? {};
          if (gesuchId && gesuchTrancheId) {
            this.router.navigate([
              '/gesuch',
              'ausbildung',
              gesuchId,
              'tranche',
              gesuchTrancheId,
            ]);
          }
        });
    }
  }

  trackByPerioden(
    _index: number,
    periode: Gesuchsperiode & { gesuchLoading: boolean },
  ) {
    return periode.id + periode.gesuchLoading;
  }

  handleOutput(output: GsDashboardActions) {
    switch (output.typ) {
      case 'delete ausbildung':
        return this.deleteAusbildung(output.id);
      case 'delete gesuch':
        return this.deleteGesuch(output.id);
      case 'delete aenderung':
        return this.deleteAenderung(output.id);
      case 'delete darlehen':
        return this.deleteDarlehen(output.id);
      case 'ausbildung unterbrechen':
        return this.ausbildungUnterbrechen(output.id, output.unterbruchId);
      case 'create aenderung':
        return this.aenderungMelden(output.melden);
      case 'create darlehen':
        return this.createDarlehen(output.gesuchId, output.fallId);
      default:
        assertUnreachable(output);
    }
  }

  private aenderungMelden(melden: AenderungMelden) {
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

  private deleteAusbildung(gesuchId: string) {
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
              gesuchId,
            }),
          );
          this.store.dispatch(SharedDataAccessGesuchEvents.reset());
        }
      });
  }

  private ausbildungUnterbrechen(ausbildungId: string, unterbruchId?: string) {
    if (unterbruchId) {
      this.router.navigate(['/', 'ausbildung-unterbrechen', unterbruchId]);
    } else {
      this.ausbildungStore.createAusbildungUnterbruchAntrag$({
        ausbildungId,
        onSuccess: (unterbruchId) => {
          this.router.navigate(['/', 'ausbildung-unterbrechen', unterbruchId]);
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

  private deleteAenderung(aenderungId: string) {
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
              const fallId = this.fallIdByAppTypeSig();
              if (fallId) {
                this.dashboardStore.loadDashboard$({ fallId });
              }
            },
          });
        }
      });
  }

  private createDarlehen(gesuchId: string, fallId: string) {
    this.darlehenStore.createDarlehen$({
      gesuchId,
      fallId,
    });
  }

  private deleteDarlehen(darlehenId: string) {
    SharedUiConfirmDialogComponent.open(this.dialog, {
      title: 'shared.form.darlehen.delete.dialog.title',
      message: 'shared.form.darlehen.delete.dialog.message',
      cancelText: 'shared.cancel',
      confirmText: 'shared.form.darlehen.delete',
    })
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          this.darlehenStore.darlehenDeleteGs$({
            data: { darlehenId },
            onSuccess: () => {
              this.globalNotificationStore.createSuccessNotification({
                messageKey: 'shared.dashboard.darlehen.deleteSuccess',
              });
              const fallId = this.fallIdByAppTypeSig();
              if (fallId) {
                this.dashboardStore.loadDashboard$({ fallId });
              }
            },
          });
        }
      });
  }
}
