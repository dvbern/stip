import { createActionGroup, emptyProps, props } from '@ngrx/store';

import { SharedModelError } from '@dv/shared/model/error';
import { Benutzer } from '@dv/shared/model/gesuch';

export const SharedDataAccessBenutzerApiEvents = createActionGroup({
  source: 'Benutzer API',
  events: {
    loadCurrentBenutzer: emptyProps(),
    currentBenutzerLoadedSuccess: props<{ benutzer: Benutzer }>(),
    currentBenutzerLoadedFailure: props<{ error: SharedModelError }>(),
  },
});
