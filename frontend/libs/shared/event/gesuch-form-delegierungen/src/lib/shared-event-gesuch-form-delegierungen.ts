import { createActionGroup, emptyProps, props } from '@ngrx/store';

import { GesuchFormStep } from '@dv/shared/model/gesuch-form';

export const SharedEventGesuchFormDelegierungen = createActionGroup({
  source: 'GesuchFormDelegierungen Page',
  events: {
    init: emptyProps(),
    nextTriggered: props<{
      id: string;
      trancheId: string;
      origin: GesuchFormStep;
    }>(),
  },
});
