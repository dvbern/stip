import { ChangeDetectionStrategy, Component, HostBinding } from '@angular/core';

@Component({
  selector: 'dv-shared-ui-step-form-buttons',
  standalone: true,
  templateUrl: './shared-ui-step-form-buttons.component.html',
  styleUrls: ['./shared-ui-step-form-buttons.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiStepFormButtonsComponent {
  @HostBinding('class') classes =
    'col-12 col-xl-8 mt-5 d-flex flex-column gap-3 flex-md-row align-items-center justify-content-center';
}
