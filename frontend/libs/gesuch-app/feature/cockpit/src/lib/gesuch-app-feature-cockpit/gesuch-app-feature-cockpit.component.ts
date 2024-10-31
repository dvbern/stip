import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  Input,
  OnInit,
  computed,
  effect,
  inject,
} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { RouterLink } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';
import { addDays, format } from 'date-fns';

import { GesuchAppDialogCreateAusbildungComponent } from '@dv/gesuch-app/dialog/create-ausbildung';
import { GesuchAppEventCockpit } from '@dv/gesuch-app/event/cockpit';
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
import { sharedDataAccessGesuchsperiodeEvents } from '@dv/shared/data-access/gesuchsperiode';
import { SharedDataAccessLanguageEvents } from '@dv/shared/data-access/language';
import { NotificationStore } from '@dv/shared/data-access/notification';
import { SharedModelGsDashboardView } from '@dv/shared/model/ausbildung';
import { GesuchTrancheSlim, Gesuchsperiode } from '@dv/shared/model/gesuch';
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
export class GesuchAppFeatureCockpitComponent implements OnInit {
  private store = inject(Store);
  private dialog = inject(MatDialog);
  private benutzerSig = this.store.selectSignal(selectSharedDataAccessBenutzer);

  dashboardItems: SharedModelGsDashboardView[] = [
    {
      fall: {
        id: '1',
        fallNummer: '1',
        mandant: 'bern',
      },
      hasActiveAusbildungen: false,
      activeAusbildungen: [],
      inactiveAusbildungen: [
        {
          fallId: 'asdf-1',
          ausbildungBegin: format(new Date(), 'yyyy-MM-dd'),
          ausbildungEnd: format(addDays(new Date(), 32), 'yyyy-MM-dd'),
          fachrichtung: 'foobar',
          pensum: 'VOLLZEIT',
          status: 'inactive',
          gesuchs: [
            {
              id: '1',
              einreichefristAbgelaufen: false,
              einreichefristDays: 10,
              reduzierterBeitrag: false,
              yearRange: '24/25',

              gesuchsperiode: {
                id: '548faee2-fd08-4497-80e7-e761c94ded09',
                bezeichnungDe: 'Herbst 2024',
                bezeichnungFr: 'Automne 2024',
                gueltigkeitStatus: 'PUBLIZIERT',
                gesuchsperiodeStart: '2024-07-01',
                gesuchsperiodeStopp: '2025-06-30',
                aufschaltterminStart: '2024-07-01',
                aufschaltterminStopp: '2025-06-30',
                einreichefristNormal: '2024-11-30',
                einreichefristReduziert: '2025-02-28',
                gesuchsjahr: {
                  id: '95ff5449-2c83-4be5-9364-788f6d08b115',
                  bezeichnungDe: 'Gesuchsjahr 24',
                  bezeichnungFr: 'Année de la demande 24',
                  technischesJahr: 2024,
                  gueltigkeitStatus: 'PUBLIZIERT',
                },
                ausbKosten_SekII: 2000,
                ausbKosten_Tertiaer: 3000,
              },
              gesuchStatus: 'IN_BEARBEITUNG_GS',
            },
          ],
          ausbildungsgang: {
            id: '1',
            bezeichnungDe: 'Ausbildungsgang',
            bezeichnungFr: 'Formation',
            bildungskategorie: {
              id: '2',
              bezeichnungDe: 'Bildungskategorie',
              bezeichnungFr: 'Catégorie de formation',
              bfs: 0,
              bildungsstufe: 'TERTIAER',
            },
          },
        },
        {
          fallId: 'asdf-2',
          ausbildungBegin: format(new Date(), 'yyyy-MM-dd'),
          ausbildungEnd: format(addDays(new Date(), 32), 'yyyy-MM-dd'),
          fachrichtung: 'foobar',
          pensum: 'VOLLZEIT',
          status: 'inactive',
          gesuchs: [
            {
              id: '1',
              einreichefristAbgelaufen: false,
              einreichefristDays: 10,
              reduzierterBeitrag: false,
              yearRange: '24/25',

              gesuchsperiode: {
                id: '548faee2-fd08-4497-80e7-e761c94ded09',
                bezeichnungDe: 'Herbst 2024',
                bezeichnungFr: 'Automne 2024',
                gueltigkeitStatus: 'PUBLIZIERT',
                gesuchsperiodeStart: '2024-07-01',
                gesuchsperiodeStopp: '2025-06-30',
                aufschaltterminStart: '2024-07-01',
                aufschaltterminStopp: '2025-06-30',
                einreichefristNormal: '2024-11-30',
                einreichefristReduziert: '2025-02-28',
                gesuchsjahr: {
                  id: '95ff5449-2c83-4be5-9364-788f6d08b115',
                  bezeichnungDe: 'Gesuchsjahr 24',
                  bezeichnungFr: 'Année de la demande 24',
                  technischesJahr: 2024,
                  gueltigkeitStatus: 'PUBLIZIERT',
                },
                ausbKosten_SekII: 2000,
                ausbKosten_Tertiaer: 3000,
              },
              gesuchStatus: 'IN_BEARBEITUNG_GS',
            },
          ],
          ausbildungsgang: {
            id: '1',
            bezeichnungDe: 'Ausbildungsgang',
            bezeichnungFr: 'Formation',
            bildungskategorie: {
              id: '2',
              bezeichnungDe: 'Bildungskategorie',
              bezeichnungFr: 'Catégorie de formation',
              bfs: 0,
              bildungsstufe: 'TERTIAER',
            },
          },
        },
      ],
      notifications: [
        {
          gesuchId: '1',
          notificationType: 'GESUCH_EINGEREICHT',
          userErstellt: 'user',
          notificationText: 'Das Gesuch wurde eingereicht',
          timestampErstellt: '2021-08-01T12:00:00',
        },
      ],
    },
  ];

  fallStore = inject(FallStore);
  gesuchAenderungStore = inject(GesuchAenderungStore);
  notificationStore = inject(NotificationStore);
  cockpitViewSig = this.store.selectSignal(selectGesuchAppFeatureCockpitView);
  // Do not initialize signals in computed directly, just usage
  benutzerNameSig = computed(() => {
    const benutzer = this.benutzerSig();
    return `${benutzer?.vorname} ${benutzer?.nachname}`;
  });
  @Input({ required: true }) tranche?: GesuchTrancheSlim;

  constructor() {
    effect(
      () => {
        const gesuchId = this.cockpitViewSig().gesuchsperiodes?.[0]?.gesuch?.id;
        if (gesuchId) {
          this.gesuchAenderungStore.getAllTranchenForGesuch$({ gesuchId });
        }
      },
      { allowSignalWrites: true },
    );
  }

  ngOnInit() {
    this.fallStore.loadCurrentFall$();
    this.notificationStore.loadNotifications$();
    this.store.dispatch(GesuchAppEventCockpit.init());
    this.store.dispatch(SharedDataAccessGesuchEvents.loadGsDashboard());
    this.store.dispatch(sharedDataAccessGesuchsperiodeEvents.init());
  }

  createAusbildung(fallId: string) {
    GesuchAppDialogCreateAusbildungComponent.open(
      this.dialog,
      fallId,
    ).subscribe((data) => {
      console.log('data', data);
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

  aenderungMelden(gesuchId: string, minDate: string, maxDate: string) {
    SharedUiAenderungMeldenDialogComponent.open(this.dialog, {
      minDate: new Date(minDate),
      maxDate: new Date(maxDate),
    })
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          this.gesuchAenderungStore.createGesuchAenderung$({
            gesuchId,
            createAenderungsantragRequest: result,
          });
        }
      });
  }

  deleteGesuch(gesuchId: string) {
    SharedUiConfirmDialogComponent.open(this.dialog, {
      title: 'gesuch-app.gesuch.delete.dialog.title',
      message: 'gesuch-app.gesuch.delete.dialog.message',
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
      title: 'gesuch-app.aenderungs-entry.delete.dialog.title',
      message: 'gesuch-app.aenderungs-entry.delete.dialog.message',
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
