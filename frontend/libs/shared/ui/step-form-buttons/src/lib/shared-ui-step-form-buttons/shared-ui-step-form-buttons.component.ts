import { ChangeDetectionStrategy, Component, HostBinding } from '@angular/core';

@Component({
  selector: 'dv-shared-ui-step-form-buttons',
  standalone: true,
  templateUrl: './shared-ui-step-form-buttons.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiStepFormButtonsComponent {
  @HostBinding('class') klass =
    'testing-library-fix tw:flex tw:flex-wrap tw:gap-3';
}
