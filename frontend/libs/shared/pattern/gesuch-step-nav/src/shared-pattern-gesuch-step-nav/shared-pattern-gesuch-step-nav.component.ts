import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  Output,
  inject,
} from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';

import { SharedModelGesuchFormStep } from '@dv/shared/model/gesuch-form';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';

import { sharedPatternGesuchStepNavView } from './shared-pattern-gesuch-step-nav.selectors';

@Component({
  selector: 'dv-shared-pattern-gesuch-step-nav',
  standalone: true,
  imports: [
    RouterLink,
    TranslateModule,
    SharedUiIconChipComponent,
    RouterLinkActive,
  ],
  templateUrl: './shared-pattern-gesuch-step-nav.component.html',
  styleUrls: ['./shared-pattern-gesuch-step-nav.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedPatternGesuchStepNavComponent {
  private store = inject(Store);

  @Input() steps!: (SharedModelGesuchFormStep & {
    disabled: boolean;
  })[];
  @Output() navClicked = new EventEmitter();

  route = inject(Router);

  viewSig = this.store.selectSignal(sharedPatternGesuchStepNavView);

  trackByIndex(index: number): number {
    return index;
  }
}
