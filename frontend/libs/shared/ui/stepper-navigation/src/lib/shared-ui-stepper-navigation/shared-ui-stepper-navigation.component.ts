import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  Output,
} from '@angular/core';
import { TranslatePipe } from '@ngx-translate/core';

@Component({
  selector: 'dv-shared-ui-stepper-navigation',
  standalone: true,
  imports: [TranslatePipe],
  templateUrl: './shared-ui-stepper-navigation.component.html',
  styleUrls: ['./shared-ui-stepper-navigation.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiStepperNavigationComponent {
  @Input() nextStepVisible = false;
  @Input() prevStepVisible = false;

  @Output() nextStep = new EventEmitter<void>();
  @Output() prevStep = new EventEmitter<void>();
}
