import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';

import { GesuchAenderungStore } from '@dv/shared/data-access/gesuch-aenderung';
import { Gesuchsperiode } from '@dv/shared/model/gesuch';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiRdIsPendingPipe } from '@dv/shared/ui/remote-data-pipe';

import { selectGesuchAppFeatureCockpitView } from '../../../../cockpit/src/lib/gesuch-app-feature-cockpit/gesuch-app-feature-cockpit.selector';

@Component({
  selector: 'lib-gesuch-app-feature-aenderungsentry',
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    SharedUiLoadingComponent,
    SharedUiRdIsPendingPipe,
    RouterLink,
  ],
  templateUrl: './gesuch-app-feature-aenderungsentry.component.html',
  styleUrl: './gesuch-app-feature-aenderungsentry.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GesuchAppFeatureAenderungsentryComponent {
  private store = inject(Store);
  gesuchAenderungStore = inject(GesuchAenderungStore);

  cockpitViewSig = this.store.selectSignal(selectGesuchAppFeatureCockpitView);

  trackByPerioden(
    _index: number,
    periode: Gesuchsperiode & { gesuchLoading: boolean },
  ) {
    return periode.id + periode.gesuchLoading;
  }
}
