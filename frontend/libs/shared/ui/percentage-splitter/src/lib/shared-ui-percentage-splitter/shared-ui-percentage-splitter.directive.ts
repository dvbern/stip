import { Directive } from '@angular/core';

@Directive({
  selector: '[dvLabelA],[dvLabelB],[dvHintA],[dvHintB]',
  standalone: true,
})
export class SharedUiPercentageSplitterDirective {}
