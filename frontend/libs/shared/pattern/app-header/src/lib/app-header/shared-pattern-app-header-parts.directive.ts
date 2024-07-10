import { Directive } from '@angular/core';

@Directive({
  selector:
    '[dvMobileNavContent], [dvMobileNavHeader], [dvHeaderDesktopRight], [dvHeaderDesktopLeft], [dvHeaderMobileAddons]',
  standalone: true,
})
export class SharedPatternAppHeaderPartsDirective {}
