import { Directive } from '@angular/core';

// todo: cleanup unused
@Directive({
  selector:
    '[dvMobileNavContent], [dvHeaderDesktopRight], [dvHeaderDesktopLeft], [dvHeaderMobileAddons], [dvGesuchNavItems], [dvGesuchStatusIndication]',
  standalone: true,
})
export class SharedPatternAppHeaderPartsDirective {}
