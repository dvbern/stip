import { createSelector } from '@ngrx/store';

import { selectSharedDataAccessConfigsView } from '@dv/shared/data-access/config';
import { sharedDataAccessGesuchsFeature } from '@dv/shared/data-access/gesuch';
import { SharedModelError } from '@dv/shared/model/error';
import { toAbschlussPhase } from '@dv/shared/model/gesuch-abschluss';
import {
  SPECIAL_VALIDATION_ERRORS,
  isSpecialValidationError,
} from '@dv/shared/model/gesuch-form';

import { sharedDataAccessAbschlussFeature } from './gesuch-app-data-access-abschluss.feature';

export const selectGesuchAppDataAccessAbschlussView = createSelector(
  sharedDataAccessAbschlussFeature.selectAbschlussState,
  sharedDataAccessGesuchsFeature.selectGesuch,
  sharedDataAccessGesuchsFeature.selectIsEditingTranche,
  sharedDataAccessGesuchsFeature.selectLastUpdate,
  sharedDataAccessGesuchsFeature.selectValidations,
  selectSharedDataAccessConfigsView,
  (
    state,
    gesuch,
    isEditingTranche,
    lastUpdate,
    validations,
    { compileTimeConfig },
  ) => {
    const checkValidationErrors = getValidationErrors(state.checkResult?.error);
    const allErrors = validations?.errors ?? [];
    const allValidations = allErrors.concat(checkValidationErrors ?? []);
    return {
      ...state,
      gesuch,
      isEditingTranche,
      trancheId: gesuch?.gesuchTrancheToWorkWith.id,
      lastUpdate,
      validations: allValidations,
      specialValidationErrors: allValidations
        .filter(isSpecialValidationError)
        .map((error) => SPECIAL_VALIDATION_ERRORS[error.messageTemplate]),
      canCheck: allErrors.length === 0,
      abschlussPhase: toAbschlussPhase(gesuch, {
        isComplete: !!state.checkResult?.success,
        checkTranche: !!isEditingTranche,
        appType: compileTimeConfig?.appType,
      }),
    };
  },
);

const getValidationErrors = (error?: SharedModelError) => {
  if (error?.type === 'validationError') {
    return error.validationErrors;
  }
  return undefined;
};
