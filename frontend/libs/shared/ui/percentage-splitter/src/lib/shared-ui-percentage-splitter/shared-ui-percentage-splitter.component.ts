import {
  ChangeDetectionStrategy,
  Component,
  Injector,
  Input,
  OnInit,
  effect,
  inject,
  input,
  runInInjectionContext,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MaskitoDirective } from '@maskito/angular';

import { SharedUiAdvTranslocoDirective } from '@dv/shared/ui/adv-transloco-directive';
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
    SharedUiAdvTranslocoDirective,
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

  allowOnlyOneSig = input<boolean>(false);

  private injector = inject(Injector);

  public ngOnInit(): void {
    runInInjectionContext(this.injector, () => {
      [
        [this.controlA, this.controlB],
        [this.controlB, this.controlA],
      ].forEach(([control, secondaryControl]) => {
        const controlChangedSig = toSignal(control.valueChanges, {
          initialValue: undefined,
        });

        effect(() => {
          const anteil = percentStringToNumber(controlChangedSig());
          const allowOnlyOne = this.allowOnlyOneSig();
          if (anteil !== undefined && anteil !== null && !allowOnlyOne) {
            secondaryControl.setValue((100 - anteil)?.toString());
            secondaryControl.setErrors(null);
          }
        });

        effect(() => {
          const anteil = percentStringToNumber(controlChangedSig());
          const allowOnlyOne = this.allowOnlyOneSig();
          if (anteil !== undefined && allowOnlyOne) {
            secondaryControl.setValue(undefined);
            secondaryControl.setErrors(null);
          }
        });
      });
    });
  }

  maskitoOptionsPercent = maskitoPercent();
}
