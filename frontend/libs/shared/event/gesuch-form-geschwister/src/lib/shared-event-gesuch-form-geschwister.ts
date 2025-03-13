import { createActionGroup, emptyProps, props } from '@ngrx/store';

import { SharedModelGesuchFormularUpdate } from '@dv/shared/model/gesuch';
import { GesuchFormStep } from '@dv/shared/model/gesuch-form';

export const SharedEventGesuchFormGeschwister = createActionGroup({
  source: 'GesuchFormGeschwister Geschwister Page',
  events: {
    init: emptyProps(),
    saveTriggered: props<{
      gesuchId: string;
      trancheId: string;
      gesuchFormular: Partial<SharedModelGesuchFormularUpdate>;
      origin: GesuchFormStep;
    }>(),
    saveSubformTriggered: props<{
      gesuchId: string;
      trancheId: string;
      gesuchFormular: Partial<SharedModelGesuchFormularUpdate>;
      origin: GesuchFormStep;
    }>(),
    nextTriggered: props<{
      id: string;
      origin: GesuchFormStep;
    }>(),
  },
});
