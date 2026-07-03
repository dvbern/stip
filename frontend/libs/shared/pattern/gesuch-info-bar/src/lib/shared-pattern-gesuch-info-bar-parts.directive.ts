import { Directive } from '@angular/core';

@Directive({
  selector: '[dvGesuchNavItems], [dvGesuchStatusIndication]',
  standalone: true,
})
export class SharedPatternGesuchInfoBarPartsDirective {}
