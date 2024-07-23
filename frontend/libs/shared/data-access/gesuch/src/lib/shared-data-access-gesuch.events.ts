import { createActionGroup, emptyProps, props } from '@ngrx/store';

import { SharedModelError } from '@dv/shared/model/error';
import {
  GesuchCreate,
  GetGesucheSBQueryTyp,
  SharedModelGesuch,
} from '@dv/shared/model/gesuch';
import { SharedModelGesuchFormStep } from '@dv/shared/model/gesuch-form';

export const SharedDataAccessGesuchEvents = createActionGroup({
  source: 'Gesuch API',
  events: {
    init: emptyProps(),
    loadGesuch: emptyProps(),
    loadAll: props<{
      query: GetGesucheSBQueryTyp;
    }>(),
    loadAllDebounced: props<{
      query: GetGesucheSBQueryTyp;
    }>(),
    newTriggered: props<{
      create: GesuchCreate;
    }>(),
    removeTriggered: props<{ id: string }>(),
    gesuchLoadedSuccess: props<{ gesuch: SharedModelGesuch }>(),
    gesuchLoadedFailure: props<{ error: SharedModelError }>(),
    gesuchCreatedSuccess: props<{ id: string }>(),
    gesuchCreatedFailure: props<{ error: SharedModelError }>(),
    gesuchUpdatedSuccess: props<{
      id: string;
      origin: SharedModelGesuchFormStep;
    }>(),

    gesuchUpdatedFailure: props<{ error: SharedModelError }>(),
    gesuchUpdatedSubformSuccess: props<{
      id: string;
      origin: SharedModelGesuchFormStep;
    }>(),
    gesuchUpdatedSubformFailure: props<{ error: SharedModelError }>(),
    gesuchRemovedSuccess: emptyProps(),
    gesuchRemovedFailure: props<{ error: SharedModelError }>(),
    gesuchValidateSteps: props<{ id: string }>(),
    gesuchValidationSuccess: props<{ error: SharedModelError }>(),
    gesuchValidationFailure: props<{ error: SharedModelError }>(),
    gesuchsLoadedSuccess: props<{ gesuchs: SharedModelGesuch[] }>(),
    gesuchsLoadedFailure: props<{ error: SharedModelError }>(),
    setGesuchToBearbeitung: emptyProps(),
  },
});
