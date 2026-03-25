import { CdkPortal, PortalModule } from '@angular/cdk/portal';
import { CommonModule } from '@angular/common';
import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  HostBinding,
  OnDestroy,
  ViewChild,
  inject,
} from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { TranslocoDirective } from '@jsverse/transloco';
import { Store } from '@ngrx/store';

import {
  INFOS_OPTIONS,
  INFOS_ROUTE,
  InfosOptions,
} from '@dv/sachbearbeitung-app/model/infos';
import { selectRouteGesuchId } from '@dv/shared/data-access/gesuch';
import { NavigationStore } from '@dv/shared/data-access/navigation';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiRouterOutletWrapperComponent } from '@dv/shared/ui/router-outlet-wrapper';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-infos',
  imports: [
    SharedUiRouterOutletWrapperComponent,
    TranslocoDirective,
    CommonModule,
    RouterLink,
    RouterLinkActive,
    TranslocoDirective,
    SharedUiIconChipComponent,
    PortalModule,
  ],
  templateUrl: './sachbearbeitung-app-feature-infos.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureInfosComponent
  implements AfterViewInit, OnDestroy
{
  @HostBinding('class') klass = 'tw:dv-pass-height';
  @ViewChild(CdkPortal)
  portalContent: CdkPortal | null = null;

  private navigationStore = inject(NavigationStore);
  private store = inject(Store);

  option?: InfosOptions;
  infosOptions = INFOS_OPTIONS;
  infosRoute = INFOS_ROUTE;
  navClicked$ = new EventEmitter();
  gesuchIdSig = this.store.selectSignal(selectRouteGesuchId);

  ngAfterViewInit(): void {
    this.navigationStore.setPortal(this.portalContent);
  }
  ngOnDestroy() {
    if (this.portalContent?.isAttached) {
      this.portalContent.detach();
    }
  }
}
