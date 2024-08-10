import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  computed,
  effect,
  inject,
} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router, RouterLink } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';

import { GesuchAppEventCockpit } from '@dv/gesuch-app/event/cockpit';
import { GesuchAppPatternMainLayoutComponent } from '@dv/gesuch-app/pattern/main-layout';
import { selectSharedDataAccessBenutzer } from '@dv/shared/data-access/benutzer';
import { FallStore } from '@dv/shared/data-access/fall';
import { SharedDataAccessGesuchEvents } from '@dv/shared/data-access/gesuch';
import { GesuchAenderungStore } from '@dv/shared/data-access/gesuch-aenderung';
import { sharedDataAccessGesuchsperiodeEvents } from '@dv/shared/data-access/gesuchsperiode';
import { SharedDataAccessLanguageEvents } from '@dv/shared/data-access/language';
import { Gesuchsperiode } from '@dv/shared/model/gesuch';
import { Language } from '@dv/shared/model/language';
import { SharedUiAenderungMeldenDialogComponent } from '@dv/shared/ui/aenderung-melden-dialog';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiLanguageSelectorComponent } from '@dv/shared/ui/language-selector';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiRdIsPendingPipe } from '@dv/shared/ui/remote-data-pipe';
import { SharedUiVersionTextComponent } from '@dv/shared/ui/version-text';
import { isSuccess } from '@dv/shared/util/remote-data';

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
    SharedUiRdIsPendingPipe,
  ],
  providers: [FallStore],
  templateUrl: './gesuch-app-feature-cockpit.component.html',
  styleUrls: ['./gesuch-app-feature-cockpit.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GesuchAppFeatureCockpitComponent implements OnInit {
  private store = inject(Store);
  private dialog = inject(MatDialog);
  private router = inject(Router);
  private benutzerSig = this.store.selectSignal(selectSharedDataAccessBenutzer);

  fallStore = inject(FallStore);
  gesuchAenderungStore = inject(GesuchAenderungStore);
  cockpitViewSig = this.store.selectSignal(selectGesuchAppFeatureCockpitView);
  // Do not initialize signals in computed directly, just usage
  benutzerNameSig = computed(() => {
    const benutzer = this.benutzerSig();
    return `${benutzer?.vorname} ${benutzer?.nachname}`;
  });

  periodenSig = computed(() => {
    const perioden = this.cockpitViewSig().gesuchsperiodes;
    const aenderungsAntraege =
      this.gesuchAenderungStore.cachedAenderungsGesuche().data ?? [];

    // could there be multiple aenderungsAntraege for one gesuch? => ja => change to filter? or don't flatten the result of getAll
    return perioden.map((periode) => {
      const aenderungsAntrag = aenderungsAntraege.find(
        (a) => a.id === periode.gesuch?.id,
      );
      return {
        ...periode,
        aenderungsAntrag, // how can we make shure we then work on the ritght tranche, stats is on gesuch! and date (1104), sice initial gesuch shows as an Antrag if not submitted.
      };
    });
  });

  constructor() {
    effect(
      () => {
        const aenderung = this.gesuchAenderungStore.cachedAenderungsGesuch();
        if (isSuccess(aenderung)) {
          this.router.navigate(['/', 'gesuch', aenderung.data.id]); // navigate to aenderung (1104)
          this.gesuchAenderungStore.resetCachedGesuchAenderung();
        }
      },
      { allowSignalWrites: true },
    );

    effect(() => {
      const gesuchIds = this.cockpitViewSig()
        .gesuchsperiodes.map((p) => p.gesuch?.id)
        .filter((g) => g !== undefined) as string[];

      if (gesuchIds.length > 0) {
        this.gesuchAenderungStore.getAllAenderungsGesuche$(gesuchIds);
      }
    });
  }

  ngOnInit() {
    this.fallStore.loadCurrentFall$();
    this.store.dispatch(GesuchAppEventCockpit.init());
    this.store.dispatch(SharedDataAccessGesuchEvents.init());
    this.store.dispatch(sharedDataAccessGesuchsperiodeEvents.init());
  }

  handleCreate(periode: Gesuchsperiode, fallId: string) {
    this.store.dispatch(
      SharedDataAccessGesuchEvents.newTriggered({
        create: {
          fallId,
          gesuchsperiodeId: periode.id,
        },
      }),
    );
  }

  handleRemove(id: string) {
    this.store.dispatch(SharedDataAccessGesuchEvents.removeTriggered({ id }));
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
}
