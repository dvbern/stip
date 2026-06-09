import { ChangeDetectionStrategy, Component, HostBinding } from '@angular/core';

@Component({
  selector: 'dv-shared-ui-step-form-buttons',
  standalone: true,
  templateUrl: './shared-ui-step-form-buttons.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiStepFormButtonsComponent {
  @HostBinding('class') klass =
    'testing-library-fix tw:mt-6 tw:xl:col-span-2 tw:flex tw:gap-3';
}
