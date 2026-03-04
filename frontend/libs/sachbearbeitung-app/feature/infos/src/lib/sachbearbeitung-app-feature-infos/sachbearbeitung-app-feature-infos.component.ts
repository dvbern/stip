import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  inject,
} from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { TranslocoDirective, TranslocoPipe } from '@jsverse/transloco';
import { Store } from '@ngrx/store';

import {
  INFOS_OPTIONS,
  INFOS_ROUTE,
  InfosOptions,
} from '@dv/sachbearbeitung-app/model/infos';
import { selectRouteId } from '@dv/shared/data-access/gesuch';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiRouterOutletWrapperComponent } from '@dv/shared/ui/router-outlet-wrapper';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-infos',
  imports: [
    TranslocoPipe,
    SharedUiRouterOutletWrapperComponent,
    TranslocoDirective,
    CommonModule,
    TranslocoPipe,
    RouterLink,
    RouterLinkActive,
    TranslocoDirective,
    SharedUiIconChipComponent,
  ],
  templateUrl: './sachbearbeitung-app-feature-infos.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureInfosComponent {
  option?: InfosOptions;

  private store = inject(Store);

  infosOptions = INFOS_OPTIONS;
  infosRoute = INFOS_ROUTE;
  navClicked$ = new EventEmitter();
  gesuchIdSig = this.store.selectSignal(selectRouteId);
}
