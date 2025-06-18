import { createActionGroup, emptyProps, props } from '@ngrx/store';

import { GesuchFormStep } from '@dv/shared/model/gesuch-form';

export const SharedEventGesuchFormAuszahlung = createActionGroup({
  source: 'GesuchFormAuszahlung Page',
  events: {
    init: emptyProps(),
    nextTriggered: props<{
      id: string;
      trancheId: string;
      origin: GesuchFormStep;
    }>(),
  },
});
