import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  computed,
  inject,
} from '@angular/core';
import { RouterLink } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';

import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import { SharedModelGesuchFormStep } from '@dv/shared/model/gesuch-form';
import { SharedPatternAppHeaderComponent } from '@dv/shared/pattern/app-header';
import { SharedPatternGesuchStepNavComponent } from '@dv/shared/pattern/gesuch-step-nav';
import { GlobalNotificationsComponent } from '@dv/shared/pattern/global-notification';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiProgressBarComponent } from '@dv/shared/ui/progress-bar';
import { SharedUiSearchComponent } from '@dv/shared/ui/search';
import { SharedUtilGesuchFormStepManagerService } from '@dv/shared/util/gesuch-form-step-manager';

@Component({
  selector: 'dv-sachbearbeitung-app-pattern-gesuch-step-layout',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    SharedPatternGesuchStepNavComponent,
    SharedPatternAppHeaderComponent,
    SharedUiIconChipComponent,
    SharedUiProgressBarComponent,
    SharedUiSearchComponent,
    TranslateModule,
    GlobalNotificationsComponent,
  ],
  templateUrl:
    './sachbearbeitung-app-pattern-gesuch-step-layout.component.html',
  styleUrls: [
    './sachbearbeitung-app-pattern-gesuch-step-layout.component.scss',
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppPatternAdministrationLayoutComponent {
  @Input()
  step?: SharedModelGesuchFormStep;
  navClicked = new EventEmitter();

  stepManager = inject(SharedUtilGesuchFormStepManagerService);
  private store = inject(Store);
  stepsSig = computed(() =>
    this.stepManager.getAllStepsWithStatus(this.viewSig().gesuchFormular),
  );
  viewSig = this.store.selectSignal(selectSharedDataAccessGesuchsView);
}
