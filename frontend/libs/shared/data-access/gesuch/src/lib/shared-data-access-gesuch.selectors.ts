import { getRouterSelectors } from '@ngrx/router-store';
import { createSelector } from '@ngrx/store';

import {
  Gesuchstatus,
  SharedModelGesuchFormular,
  SharedModelGesuchFormularProps,
  ValidationMessage,
} from '@dv/shared/model/gesuch';
import { isDefined } from '@dv/shared/util-fn/type-guards';

import { sharedDataAccessGesuchsFeature } from './shared-data-access-gesuch.feature';

const { selectRouteParam } = getRouterSelectors();

export const selectRouteId = selectRouteParam('id');

export const selectSharedDataAccessGesuchsView = createSelector(
  sharedDataAccessGesuchsFeature.selectGesuchsState,
  (state) => {
    const currentForm = state.gesuchFormular ?? state.cache.gesuchFormular;
    return {
      ...state,
      readonly: state.gesuch?.gesuchStatus === Gesuchstatus.FEHLERHAFT,
      trancheId: state.gesuch?.gesuchTrancheToWorkWith.id,
      cachedGesuchFormular: currentForm,
      invalidFormularProps: {
        lastUpdate: state.lastUpdate,
        validations: {
          errors: transformValidationMessage(
            state.validations?.errors,
            currentForm,
          ),
          warnings: transformValidationMessage(
            state.validations?.warnings,
            currentForm,
          ),
        },
      },
    };
  },
);

/**
 * Returns true if the gesuchFormular has the given property
 */
export const isFormularProp =
  (gesuchFormular: SharedModelGesuchFormular | null) =>
  (prop: string): prop is SharedModelGesuchFormularProps => {
    if (!gesuchFormular) return false;
    return Object.keys(gesuchFormular).includes(prop);
  };

const transformValidationMessage = (
  messages?: ValidationMessage[],
  currentForm?: SharedModelGesuchFormular | null,
) => {
  return messages
    ?.map((m) => m.propertyPath)
    .filter(isDefined)
    .filter(isFormularProp(currentForm ?? null));
};
