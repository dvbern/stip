import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  OnInit,
  computed,
} from '@angular/core';
import { RouterLink } from '@angular/router';
import { TranslocoPipe } from '@jsverse/transloco';

import { SharedEventGesuchFormAuszahlung } from '@dv/shared/event/gesuch-form-auszahlung';
import { SharedFeatureAuszahlungComponent } from '@dv/shared/feature/auszahlung';
import { SharedUiAuszahlungComponent } from '@dv/shared/ui/auszahlung';
import { SharedUiIfGesuchstellerDirective } from '@dv/shared/ui/if-app-type';

@Component({
  selector: 'dv-shared-feature-gesuch-form-auszahlung',
  imports: [
    RouterLink,
    TranslocoPipe,
    SharedUiAuszahlungComponent,
    SharedUiIfGesuchstellerDirective,
  ],
  templateUrl: './shared-feature-gesuch-form-auszahlung.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchFormAuszahlungComponent
  extends SharedFeatureAuszahlungComponent
  implements OnInit
{
  @HostBinding('class')
  hostClass = 'tw:pt-6';

  extendedAuszahlungViewSig = computed(() => {
    const baseView = this.auszahlungViewSig();

    return {
      ...baseView,
      readonly: true,
    };
  });

  ngOnInit(): void {
    this.store.dispatch(SharedEventGesuchFormAuszahlung.init());
  }
}
