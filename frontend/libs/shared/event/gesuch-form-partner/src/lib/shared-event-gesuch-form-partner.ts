import { createActionGroup, emptyProps, props } from '@ngrx/store';

import { GesuchFormularType } from '@dv/shared/model/gesuch';
import { GesuchFormStep } from '@dv/shared/model/gesuch-form';

export const SharedEventGesuchFormPartner = createActionGroup({
  source: 'GesuchFormPartner Page',
  events: {
    init: emptyProps(),
    prevStepTriggered: props<{
      gesuchId: string;
      trancheId: string;
      gesuchFormular: Partial<GesuchFormularType>;
      origin: GesuchFormStep;
    }>(),
    nextStepTriggered: props<{
      gesuchId: string;
      trancheId: string;
      gesuchFormular: Partial<GesuchFormularType>;
      origin: GesuchFormStep;
    }>(),
    nextTriggered: props<{
      id: string;
      origin: GesuchFormStep;
    }>(),
  },
});
