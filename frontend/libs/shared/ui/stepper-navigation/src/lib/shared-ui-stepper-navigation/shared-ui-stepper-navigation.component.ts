import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  Output,
} from '@angular/core';
import { TranslocoPipe } from '@jsverse/transloco';

@Component({
  selector: 'dv-shared-ui-stepper-navigation',
  imports: [TranslocoPipe],
  templateUrl: './shared-ui-stepper-navigation.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiStepperNavigationComponent {
  @Input() nextStepVisible = false;
  @Input() prevStepVisible = false;
  @Input() currentStep: number | null = null;
  @Input() totalSteps: number | null = null;

  @Output() nextStep = new EventEmitter<void>();
  @Output() prevStep = new EventEmitter<void>();
}
