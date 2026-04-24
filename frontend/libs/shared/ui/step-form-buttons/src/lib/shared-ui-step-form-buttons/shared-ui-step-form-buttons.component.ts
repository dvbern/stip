import { ChangeDetectionStrategy, Component, HostBinding } from '@angular/core';

@Component({
  selector: 'dv-shared-ui-step-form-buttons',
  standalone: true,
  templateUrl: './shared-ui-step-form-buttons.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiStepFormButtonsComponent {
  @HostBinding('class') klass =
    'tw:col-span-12 tw:xl:col-span-8 tw:mt-12 tw:flex tw:flex-col tw:gap-3 tw:md:flex-row tw:items-start tw:justify-start';
}
