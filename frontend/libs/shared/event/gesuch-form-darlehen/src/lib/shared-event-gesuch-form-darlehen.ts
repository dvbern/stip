import { createActionGroup, emptyProps, props } from '@ngrx/store';

import { SharedModelGesuchFormularUpdate } from '@dv/shared/model/gesuch';
import { SharedModelGesuchFormStep } from '@dv/shared/model/gesuch-form';

export const SharedEventGesuchFormDarlehen = createActionGroup({
  source: 'GesuchFormDarlehen Page',
  events: {
    init: emptyProps(),
    saveTriggered: props<{
      gesuchId: string;
      trancheId: string;
      gesuchFormular: Partial<SharedModelGesuchFormularUpdate>;
      origin: SharedModelGesuchFormStep;
    }>(),
    nextTriggered: props<{
      id: string;
      trancheId: string;
      origin: SharedModelGesuchFormStep;
    }>(),
  },
});
