import { createActionGroup, emptyProps } from '@ngrx/store';

export const SharedEventGesuchDokumente = createActionGroup({
  source: 'GesuchDokumente Page',
  events: {
    init: emptyProps(),
  },
});
