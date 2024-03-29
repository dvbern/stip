import { createSelector } from '@ngrx/store';

import { toAbschlussPhase } from '@dv/gesuch-app/model/gesuch-abschluss';
import { sharedDataAccessGesuchsFeature } from '@dv/shared/data-access/gesuch';
import { SharedModelError } from '@dv/shared/model/error';

import { gesuchAppDataAccessAbschlussFeature } from './gesuch-app-data-access-abschluss.feature';

export const selectGesuchAppDataAccessAbschlusssView = createSelector(
  gesuchAppDataAccessAbschlussFeature.selectAbschlussState,
  sharedDataAccessGesuchsFeature.selectGesuch,
  sharedDataAccessGesuchsFeature.selectLastUpdate,
  sharedDataAccessGesuchsFeature.selectValidations,
  (state, gesuch, lastUpdate, validations) => {
    const checkValidationErrors = getValidationErrors(state.checkResult?.error);
    const allValidations = (validations ?? []).concat(
      checkValidationErrors ?? [],
    );
    return {
      ...state,
      gesuch,
      lastUpdate,
      validations: allValidations,
      canCheck: validations?.length === 0,
      canAbschliessen: state.checkResult?.success,
      abschlussPhase: toAbschlussPhase(gesuch, true),
    };
  },
);

const getValidationErrors = (error?: SharedModelError) => {
  if (error?.type === 'validationError') {
    return error.validationErrors;
  }
  return undefined;
};
