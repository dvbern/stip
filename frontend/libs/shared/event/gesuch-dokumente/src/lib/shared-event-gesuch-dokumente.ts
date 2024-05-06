import { createActionGroup, emptyProps, props } from '@ngrx/store';

import { SharedModelGesuchFormStep } from '@dv/shared/model/gesuch-form';

export const SharedEventGesuchDokumente = createActionGroup({
  source: 'GesuchDokumente Page',
  events: {
    init: emptyProps(),
    loadDocuments: props<{ gesuchId: string }>(),
    nextTriggered: props<{
      id: string;
      origin: SharedModelGesuchFormStep;
    }>(),
  },
});
