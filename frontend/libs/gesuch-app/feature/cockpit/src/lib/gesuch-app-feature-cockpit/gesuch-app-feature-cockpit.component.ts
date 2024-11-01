import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  Input,
  computed,
  effect,
  inject,
} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { RouterLink } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';

import { DashboardStore } from '@dv/gesuch-app/data-access/dashboard';
import { GesuchAppDialogCreateAusbildungComponent } from '@dv/gesuch-app/dialog/create-ausbildung';
import { GesuchAppPatternMainLayoutComponent } from '@dv/gesuch-app/pattern/main-layout';
import { GesuchAppUiAenderungsEntryComponent } from '@dv/gesuch-app/ui/aenderungs-entry';
import {
  GesuchAppUiDashboardAusbildungComponent,
  GesuchAppUiDashboardCompactAusbildungComponent,
} from '@dv/gesuch-app/ui/dashboard';
import { selectSharedDataAccessBenutzer } from '@dv/shared/data-access/benutzer';
import { FallStore } from '@dv/shared/data-access/fall';
import { SharedDataAccessGesuchEvents } from '@dv/shared/data-access/gesuch';
import { GesuchAenderungStore } from '@dv/shared/data-access/gesuch-aenderung';
import { SharedDataAccessLanguageEvents } from '@dv/shared/data-access/language';
import {
  AenderungMelden,
  GesuchTrancheSlim,
  Gesuchsperiode,
} from '@dv/shared/model/gesuch';
import { Language } from '@dv/shared/model/language';
import { SharedUiAenderungMeldenDialogComponent } from '@dv/shared/ui/aenderung-melden-dialog';
import { SharedUiConfirmDialogComponent } from '@dv/shared/ui/confirm-dialog';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiLanguageSelectorComponent } from '@dv/shared/ui/language-selector';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiNotificationsComponent } from '@dv/shared/ui/notifications';
import { SharedUiRdIsPendingPipe } from '@dv/shared/ui/remote-data-pipe';
import { SharedUiVersionTextComponent } from '@dv/shared/ui/version-text';

import { selectGesuchAppFeatureCockpitView } from './gesuch-app-feature-cockpit.selector';

@Component({
  selector: 'dv-gesuch-app-feature-cockpit',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    TranslateModule,
    GesuchAppPatternMainLayoutComponent,
    SharedUiLanguageSelectorComponent,
    SharedUiIconChipComponent,
    SharedUiLoadingComponent,
    SharedUiVersionTextComponent,
    SharedUiNotificationsComponent,
    SharedUiRdIsPendingPipe,
    GesuchAppUiDashboardAusbildungComponent,
    GesuchAppUiDashboardCompactAusbildungComponent,
    GesuchAppUiAenderungsEntryComponent,
  ],
  providers: [FallStore],
  templateUrl: './gesuch-app-feature-cockpit.component.html',
  styleUrls: ['./gesuch-app-feature-cockpit.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GesuchAppFeatureCockpitComponent {
  private store = inject(Store);
  private dialog = inject(MatDialog);
  private benutzerSig = this.store.selectSignal(selectSharedDataAccessBenutzer);

  dashboardStore = inject(DashboardStore);
  fallStore = inject(FallStore);
  gesuchAenderungStore = inject(GesuchAenderungStore);
  cockpitViewSig = this.store.selectSignal(selectGesuchAppFeatureCockpitView);
  benutzerNameSig = computed(() => {
    const benutzer = this.benutzerSig();
    return `${benutzer?.vorname} ${benutzer?.nachname}`;
  });
  @Input({ required: true }) tranche?: GesuchTrancheSlim;

  private gotNewFallSig = computed(() => {
    return this.fallStore.currentFallViewSig()?.id;
  });

  constructor() {
    this.fallStore.loadCurrentFall$();

    effect(
      () => {
        const fallId = this.gotNewFallSig();

        if (fallId) {
          this.dashboardStore.loadDashboard$();
        }
      },
      { allowSignalWrites: true },
    );
  }

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
      gesuch: { id, gesuchsperiode },
    } = melden;
    SharedUiAenderungMeldenDialogComponent.open(this.dialog, {
      minDate: new Date(gesuchsperiode.gesuchsperiodeStart),
      maxDate: new Date(gesuchsperiode.gesuchsperiodeStopp),
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
          });
        }
      });
  }
}
