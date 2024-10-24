import { createActionGroup, emptyProps, props } from '@ngrx/store';

import { SharedModelError } from '@dv/shared/model/error';
import {
  GesuchCreate,
  GetGesucheSBQueryType,
  GsDashboard,
  SharedModelGesuch,
} from '@dv/shared/model/gesuch';
import { SharedModelGesuchFormStep } from '@dv/shared/model/gesuch-form';

export const SharedDataAccessGesuchEvents = createActionGroup({
  source: 'Gesuch API',
  events: {
    init: emptyProps(),
    loadGesuch: emptyProps(),
    loadAll: props<{
      query: GetGesucheSBQueryType;
    }>(),
    loadAllDebounced: props<{
      query: GetGesucheSBQueryType;
    }>(),
    createGesuch: props<{
      create: GesuchCreate;
    }>(),
    gesuchLoadedSuccess: props<{
      gesuch: SharedModelGesuch;
      trancheId?: string;
    }>(),
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
    deleteGesuch: props<{ gesuchId: string }>(),
    deleteGesuchSuccess: emptyProps(),
    deleteGesuchFailure: props<{ error: SharedModelError }>(),
    gesuchsLoadedSuccess: props<{ gesuchs: SharedModelGesuch[] }>(),
    gesuchsLoadedFailure: props<{ error: SharedModelError }>(),
    loadGsDashboard: emptyProps(),
    gsDashboardLoadedSuccess: props<{ gsDashboard: GsDashboard[] }>(),
    gsDashboardLoadedFailure: props<{ error: SharedModelError }>(),
    setGesuchToBearbeitung: emptyProps(),
    setGesuchBearbeitungAbschliessen: emptyProps(),
    setGesuchZurueckweisen: props<{ kommentar: string }>(),
    setGesuchVerfuegt: emptyProps(),
    setGesuchBereitFuerBearbeitung: props<{ kommentar: string }>(),
    setGesuchVersendet: emptyProps(),
  },
});
