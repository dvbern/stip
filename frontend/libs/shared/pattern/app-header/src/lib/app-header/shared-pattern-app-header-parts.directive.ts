import { Directive } from '@angular/core';

@Directive({
  selector:
    '[dvMobileNavContent], [dvHeaderDesktopRight], [dvHeaderDesktopLeft], [dvHeaderMobileAddons]',
  standalone: true,
})
export class SharedPatternAppHeaderPartsDirective {}
