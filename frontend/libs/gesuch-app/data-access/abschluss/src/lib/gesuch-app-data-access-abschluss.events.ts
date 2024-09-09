import { createActionGroup, emptyProps, props } from '@ngrx/store';

import { SharedModelError } from '@dv/shared/model/error';

export const GesuchAppDataAccessAbschlussApiEvents = createActionGroup({
  source: 'Abschluss API',
  events: {
    gesuchAbschliessen: props<{
      gesuchId: string;
    }>(),
    trancheAbschliessen: props<{
      trancheId: string;
    }>(),
    check: props<{ gesuchId: string }>(),
    gesuchCheckSuccess: props<{ error: SharedModelError }>(),
    gesuchCheckFailure: props<{ error: SharedModelError }>(),
    abschlussSuccess: emptyProps(),
    abschlussFailure: props<{ error: SharedModelError }>(),
  },
});
