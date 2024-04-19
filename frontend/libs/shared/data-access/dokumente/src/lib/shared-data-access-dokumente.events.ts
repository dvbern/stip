import { createActionGroup, props } from '@ngrx/store';

import { SharedModelError } from '@dv/shared/model/error';
import { Dokument } from '@dv/shared/model/gesuch';

export const SharedDataAccessDokumenteApiEvents = createActionGroup({
  source: 'Dokumente API',
  events: {
    dokumentesLoadedSuccess: props<{ dokumentes: Dokument[] }>(),
    dokumentesLoadedFailure: props<{ error: SharedModelError }>(),
  },
});
