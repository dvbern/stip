import { createActionGroup, emptyProps, props } from '@ngrx/store';

import { SharedModelGesuchFormularUpdate } from '@dv/shared/model/gesuch';
import { GesuchFormStep } from '@dv/shared/model/gesuch-form';

export const SharedEventGesuchFormAbschluss = createActionGroup({
  source: 'GesuchFormAbschluss Page',
  events: {
    init: emptyProps(),
    saveTriggered: props<{
      gesuchId: string;
      gesuchFormular: Partial<SharedModelGesuchFormularUpdate>;
      origin: GesuchFormStep;
    }>(),
  },
});
