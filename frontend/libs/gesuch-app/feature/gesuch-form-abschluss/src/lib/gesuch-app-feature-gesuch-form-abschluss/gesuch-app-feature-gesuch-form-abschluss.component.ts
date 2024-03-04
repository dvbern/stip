import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  inject,
} from '@angular/core';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';

import {
  GesuchAppDataAccessAbschlussApiEvents,
  selectGesuchAppDataAccessAbschlusssView,
} from '@dv/gesuch-app/data-access/abschluss';
import { SharedDataAccessStammdatenApiEvents } from '@dv/shared/data-access/stammdaten';
import { SharedEventGesuchFormAbschluss } from '@dv/shared/event/gesuch-form-abschluss';

@Component({
  selector: 'dv-gesuch-app-feature-gesuch-form-abschluss',
  standalone: true,
  imports: [TranslateModule],
  templateUrl: './gesuch-app-feature-gesuch-form-abschluss.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GesuchAppFeatureGesuchFormAbschlussComponent implements OnInit {
  private store = inject(Store);

  viewSig = this.store.selectSignal(selectGesuchAppDataAccessAbschlusssView);

  ngOnInit(): void {
    this.store.dispatch(SharedEventGesuchFormAbschluss.init());
    this.store.dispatch(SharedDataAccessStammdatenApiEvents.init());
  }

  abschliessen(gesuchId: string) {
    this.store.dispatch(
      GesuchAppDataAccessAbschlussApiEvents.gesuchAbschliessen({ gesuchId }),
    );
  }
}
