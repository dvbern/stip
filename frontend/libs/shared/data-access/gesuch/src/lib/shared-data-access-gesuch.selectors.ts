import { getRouterSelectors } from '@ngrx/router-store';
import { createSelector } from '@ngrx/store';

import { selectSharedDataAccessConfigsView } from '@dv/shared/data-access/config';
import {
  Gesuchstatus,
  SharedModelGesuchFormular,
  SharedModelGesuchFormularProps,
  ValidationMessage,
} from '@dv/shared/model/gesuch';
import {
  SPECIAL_VALIDATION_ERRORS,
  isSpecialValidationError,
} from '@dv/shared/model/gesuch-form';
import { isDefined } from '@dv/shared/util-fn/type-guards';

import { sharedDataAccessGesuchsFeature } from './shared-data-access-gesuch.feature';

const { selectRouteParam } = getRouterSelectors();

export const selectRouteId = selectRouteParam('id');

export const selectSharedDataAccessGesuchsView = createSelector(
  selectSharedDataAccessConfigsView,
  sharedDataAccessGesuchsFeature.selectLastUpdate,
  sharedDataAccessGesuchsFeature.selectLoading,
  sharedDataAccessGesuchsFeature.selectGesuch,
  sharedDataAccessGesuchsFeature.selectGesuchFormular,
  (config, lastUpdate, loading, gesuch, gesuchFormular) => {
    return {
      lastUpdate,
      loading,
      gesuch,
      gesuchFormular,
      readonly:
        !!gesuch && gesuch.gesuchStatus !== Gesuchstatus.IN_BEARBEITUNG_GS,
      trancheId: gesuch?.gesuchTrancheToWorkWith.id,
      gesuchId: gesuch?.id,
      allowTypes: config.deploymentConfig?.allowedMimeTypes?.join(','),
    };
  },
);

export const selectSharedDataAccessGesuchValidationView = createSelector(
  sharedDataAccessGesuchsFeature.selectGesuchsState,
  (state) => {
    const currentForm = state.gesuchFormular ?? state.cache.gesuchFormular;
    return {
      cachedGesuchId: state.cache.gesuchId,
      cachedGesuchFormular: currentForm,
      invalidFormularProps: {
        lastUpdate: state.lastUpdate,
        validations: {
          errors: transformValidationMessagesToFormKeys(
            state.validations?.errors,
            currentForm,
          ),
          warnings: transformValidationMessagesToFormKeys(
            state.validations?.warnings,
            currentForm,
          ),
          hasDocuments: state.validations?.hasDocuments ?? null,
        },
        specialValidationErrors: state.validations?.errors
          .filter(isSpecialValidationError)
          .map((error) => SPECIAL_VALIDATION_ERRORS[error.messageTemplate]),
      },
    };
  },
);

export const selectSharedDataAccessGesuchSteuerdatenView = createSelector(
  sharedDataAccessGesuchsFeature.selectGesuchsState,
  (state) => {
    return {
      steuerdaten: state.gesuchFormular?.steuerdaten,
    };
  },
);

const transformValidationMessagesToFormKeys = (
  messages?: ValidationMessage[],
  currentForm?: SharedModelGesuchFormular | null,
) => {
  const formKeys: SharedModelGesuchFormularProps[] = [
    ...(Object.keys(currentForm ?? {}) as SharedModelGesuchFormularProps[]),
    'dokuments',
  ];

  return messages
    ?.filter(isDefined)
    .filter(
      (m) =>
        isGesuchFormularProp(formKeys)(m.propertyPath) ||
        isSpecialValidationError(m),
    )
    .map((m) => {
      if (m.messageTemplate?.includes('documents.required')) {
        return [{ ...m, propertyPath: 'dokuments' }, { ...m }];
      }
      return m;
    })
    .flat()
    .map((m) => m.propertyPath)
    .filter(isDefined)
    .filter(isGesuchFormularProp(formKeys));
};

/**
 * Returns true if the gesuchFormular has the given property
 */
export const isGesuchFormularProp =
  (formKeys: string[]) =>
  (prop?: string): prop is SharedModelGesuchFormularProps => {
    if (!prop) return false;
    return formKeys.includes(prop);
  };
