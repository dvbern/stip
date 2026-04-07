import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  inject,
  input,
} from '@angular/core';

import { DemoDataAppUiAdvTranslocoDirective } from '@dv/demo-data-app/ui/adv-transloco-directive';
import {
  DemoDataTestBerechnungValid,
  DemoDataTestBerechnungValues,
} from '@dv/shared/model/gesuch';
import { SharedUiFormatChfNullablePipe } from '@dv/shared/ui/format-chf-pipe';

@Component({
  selector: 'dv-soll-ist',
  templateUrl: './soll-ist.component.html',
  imports: [SharedUiFormatChfNullablePipe, DemoDataAppUiAdvTranslocoDirective],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SollIstComponent {
  private elementRef = inject<ElementRef<HTMLElement>>(ElementRef<HTMLElement>);
  valuesSig = input.required<
    Partial<Record<'soll' | 'ist', DemoDataTestBerechnungValues>> & {
      valid?: DemoDataTestBerechnungValid;
    }
  >();
  sollIst = ['soll', 'ist'] as const;

  getText() {
    return this.elementRef.nativeElement.innerText;
  }
}
