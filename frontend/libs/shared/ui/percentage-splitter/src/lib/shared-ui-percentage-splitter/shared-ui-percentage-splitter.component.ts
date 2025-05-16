import {
  ChangeDetectionStrategy,
  Component,
  Injector,
  Input,
  OnInit,
  effect,
  inject,
  runInInjectionContext,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MaskitoDirective } from '@maskito/angular';
import { TranslatePipe } from '@ngx-translate/core';

import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { percentStringToNumber } from '@dv/shared/util/form';
import { maskitoPercent } from '@dv/shared/util/maskito-util';

@Component({
  selector: 'dv-shared-ui-percentage-splitter',
  imports: [
    MaskitoDirective,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    TranslatePipe,
  ],
  templateUrl: './shared-ui-percentage-splitter.component.html',
  styleUrls: ['./shared-ui-percentage-splitter.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiPercentageSplitterComponent implements OnInit {
  @Input({ required: true }) updateValidity: unknown;
  @Input({ required: true })
  controlA!: FormControl<string | undefined>;

  @Input({ required: true })
  controlB!: FormControl<string | undefined>;

  private injector = inject(Injector);

  public ngOnInit(): void {
    runInInjectionContext(this.injector, () => {
      const controlAChangedSig = toSignal(this.controlA.valueChanges, {
        initialValue: undefined,
      });
      const controlBChangedSig = toSignal(this.controlB.valueChanges, {
        initialValue: undefined,
      });

      this.controlA.addValidators(Validators.minLength(2));
      this.controlB.addValidators(Validators.minLength(2));

      effect(
        () => {
          const anteilA = percentStringToNumber(controlAChangedSig());
          if (anteilA !== undefined && anteilA !== null) {
            this.controlB.setValue((100 - anteilA)?.toString());
            this.controlB.setErrors(null);
          }
        },
        { allowSignalWrites: true },
      );

      effect(
        () => {
          const anteilB = percentStringToNumber(controlBChangedSig());
          if (anteilB !== undefined && anteilB !== null) {
            this.controlA.setValue((100 - anteilB)?.toString());
            this.controlA.setErrors(null);
          }
        },
        { allowSignalWrites: true },
      );
    });
  }

  maskitoOptionsPercent = maskitoPercent;
}
