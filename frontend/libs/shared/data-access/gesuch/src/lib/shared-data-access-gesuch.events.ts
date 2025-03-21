import { createActionGroup, emptyProps, props } from '@ngrx/store';

import { SharedModelError } from '@dv/shared/model/error';
import {
  FallDashboardItem,
  GesuchCreate,
  GesuchUrlType,
  GetGesucheSBQueryType,
  SharedModelGesuch,
} from '@dv/shared/model/gesuch';
import { GesuchFormStep } from '@dv/shared/model/gesuch-form';

export const SharedDataAccessGesuchEvents = createActionGroup({
  source: 'Gesuch API',
  events: {
    init: emptyProps(),
    reset: emptyProps(),
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
      typ: GesuchUrlType;
    }>(),
    gesuchSetReturned: props<{
      gesuch: SharedModelGesuch;
    }>(),
    gesuchLoadedFailure: props<{ error: SharedModelError }>(),
    gesuchCreatedSuccess: props<{ id: string }>(),
    gesuchCreatedFailure: props<{ error: SharedModelError }>(),
    gesuchUpdatedSuccess: props<{
      id: string;
      origin: GesuchFormStep;
    }>(),

    gesuchUpdatedFailure: props<{ error: SharedModelError }>(),
    gesuchUpdatedSubformSuccess: props<{
      id: string;
      origin: GesuchFormStep;
    }>(),
    gesuchUpdatedSubformFailure: props<{ error: SharedModelError }>(),
    deleteGesuch: props<{ gesuchId: string }>(),
    deleteGesuchSuccess: emptyProps(),
    deleteGesuchFailure: props<{ error: SharedModelError }>(),
    gesuchsLoadedSuccess: props<{ gesuchs: SharedModelGesuch[] }>(),
    gesuchsLoadedFailure: props<{ error: SharedModelError }>(),
    gsDashboardLoadedSuccess: props<{ gsDashboard: FallDashboardItem[] }>(),
    gsDashboardLoadedFailure: props<{ error: SharedModelError }>(),
  },
});
