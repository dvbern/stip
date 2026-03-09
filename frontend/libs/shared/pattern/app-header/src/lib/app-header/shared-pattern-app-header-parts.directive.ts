import { Directive } from '@angular/core';

// todo-before-merge: cleanup unused refs
@Directive({
  selector:
    '[dvMobileNavContent], [dvHeaderDesktopRight], [dvHeaderDesktopLeft], [dvHeaderMobileAddons], [dvGesuchNavItems], [dvGesuchStatusIndication]',
  standalone: true,
})
export class SharedPatternAppHeaderPartsDirective {}
