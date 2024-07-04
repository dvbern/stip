import { createActionGroup, emptyProps } from '@ngrx/store';

export const SharedEventGesuchFormProtokoll = createActionGroup({
  source: 'GesuchFormProtokoll Page',
  events: {
    init: emptyProps(),
  },
});
