import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  computed,
  inject,
} from '@angular/core';
import { RouterLink } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';

import { GesuchAppEventCockpit } from '@dv/gesuch-app/event/cockpit';
import { GesuchAppPatternMainLayoutComponent } from '@dv/gesuch-app/pattern/main-layout';
import { selectSharedDataAccessBenutzer } from '@dv/shared/data-access/benutzer';
import { FallStore } from '@dv/shared/data-access/fall';
import { SharedDataAccessGesuchEvents } from '@dv/shared/data-access/gesuch';
import { sharedDataAccessGesuchsperiodeEvents } from '@dv/shared/data-access/gesuchsperiode';
import { SharedDataAccessLanguageEvents } from '@dv/shared/data-access/language';
import { Gesuchsperiode } from '@dv/shared/model/gesuch';
import { Language } from '@dv/shared/model/language';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiLanguageSelectorComponent } from '@dv/shared/ui/language-selector';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
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
  ],
  providers: [FallStore],
  templateUrl: './gesuch-app-feature-cockpit.component.html',
  styleUrls: ['./gesuch-app-feature-cockpit.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GesuchAppFeatureCockpitComponent implements OnInit {
  private store = inject(Store);
  private benutzerSig = this.store.selectSignal(selectSharedDataAccessBenutzer);

  fallStore = inject(FallStore);
  cockpitViewSig = this.store.selectSignal(selectGesuchAppFeatureCockpitView);
  // Do not initialize signals in computed directly, just usage
  benutzerNameSig = computed(() => {
    const benutzer = this.benutzerSig();
    return `${benutzer?.vorname} ${benutzer?.nachname}`;
  });

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
}
