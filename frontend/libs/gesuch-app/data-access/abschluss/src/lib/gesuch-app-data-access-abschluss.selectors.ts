import { createSelector } from '@ngrx/store';

import { toAbschlussPhase } from '@dv/gesuch-app/model/gesuch-abschluss';
import { sharedDataAccessGesuchsFeature } from '@dv/shared/data-access/gesuch';
import { SharedModelError } from '@dv/shared/model/error';
import {
  SPECIAL_VALIDATION_ERRORS,
  isSpecialValidationError,
} from '@dv/shared/model/gesuch-form';

import { gesuchAppDataAccessAbschlussFeature } from './gesuch-app-data-access-abschluss.feature';

export const selectGesuchAppDataAccessAbschlussView = createSelector(
  gesuchAppDataAccessAbschlussFeature.selectAbschlussState,
  sharedDataAccessGesuchsFeature.selectGesuch,
  sharedDataAccessGesuchsFeature.selectLastUpdate,
  sharedDataAccessGesuchsFeature.selectValidations,
  (state, gesuch, lastUpdate, validations) => {
    const checkValidationErrors = getValidationErrors(state.checkResult?.error);
    const allErrors = validations?.errors ?? [];
    const allValidations = allErrors.concat(checkValidationErrors ?? []);
    return {
      ...state,
      gesuch,
      lastUpdate,
      validations: allValidations,
      specialValidationErrors: allValidations
        .filter(isSpecialValidationError)
        .map((error) => SPECIAL_VALIDATION_ERRORS[error.messageTemplate]),
      canCheck: allErrors.length === 0,
      abschlussPhase: toAbschlussPhase(gesuch, !!state.checkResult?.success),
    };
  },
);

const getValidationErrors = (error?: SharedModelError) => {
  if (error?.type === 'validationError') {
    return error.validationErrors;
  }
  return undefined;
};
