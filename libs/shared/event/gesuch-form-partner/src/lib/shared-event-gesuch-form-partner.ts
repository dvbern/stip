import { createActionGroup, emptyProps, props } from '@ngrx/store';
import { SharedModelGesuchFormular } from '@dv/shared/model/gesuch';
import { SharedModelGesuchFormStep } from '@dv/shared/model/gesuch-form';

export const SharedEventGesuchFormPartner = createActionGroup({
  source: 'GesuchFormPartner Page',
  events: {
    init: emptyProps(),
    prevStepTriggered: props<{
      gesuchId: string;
      gesuchFormular: Partial<SharedModelGesuchFormular>;
      origin: SharedModelGesuchFormStep;
    }>(),
    nextStepTriggered: props<{
      gesuchId: string;
      gesuchFormular: Partial<SharedModelGesuchFormular>;
      origin: SharedModelGesuchFormStep;
    }>(),
  },
});
