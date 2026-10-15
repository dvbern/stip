import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  inject,
  input,
} from '@angular/core';

import { DemoDataAppUiAdvTranslocoDirective } from '@dv/demo-data-app/ui/adv-transloco-directive';
import {
  DemoDataTestBerechnungDetails,
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
  valuesSig = input.required<{
    values: Partial<Record<'soll' | 'ist', DemoDataTestBerechnungValues>>;
    details: Partial<Record<'soll' | 'ist', DemoDataTestBerechnungDetails>>;
    valid?: DemoDataTestBerechnungValid;
  }>();
  sollIstKeys = ['soll', 'ist'] as const;

  getText() {
    return this.elementRef.nativeElement.innerText;
  }
}
