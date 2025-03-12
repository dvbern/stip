import { ValidationError } from '@dv/shared/model/error';
import { FormPropsExcluded } from '@dv/shared/model/gesuch';

import {
  FAMILIENSITUATION,
  GESCHWISTER,
  GSFormSteps,
  PERSON,
} from './gesuch-form-steps';
import { GesuchFormStep } from './shared-model-gesuch-form';

export type SpecialValidationError = {
  step: GesuchFormStep;
  field: string;
  fieldErrorKey: string;
  validationErrorKey: string;
  linkKey: string;
};

const wohnsitzFamiliensituationMap: Record<string, GesuchFormStep> = {
  personInAusbildung: PERSON,
  geschwisters: GESCHWISTER,
} satisfies Partial<Record<FormPropsExcluded, GesuchFormStep>>;
export const SPECIAL_VALIDATION_ERRORS: Record<
  string,
  (error: ValidationError) => SpecialValidationError
> = {
  'dvbern.stip.validation.gesuch.einreichen.svnummer.unique.message': () => ({
    step: GSFormSteps.PERSON,
    field: 'sozialversicherungsnummer',
    fieldErrorKey: 'shared.form.person.validation.uniqueSvnummer',
    validationErrorKey: 'shared.gesuch.validation.uniqueSvnummer.message',
    linkKey: 'shared.gesuch.validation.link.personInAusbildung',
  }),
  '{jakarta.validation.constraints.familiensituation.wohnsituation.message}': (
    validationError,
  ) => ({
    step:
      validationError.propertyPath &&
      validationError.propertyPath in wohnsitzFamiliensituationMap
        ? wohnsitzFamiliensituationMap[validationError.propertyPath]
        : FAMILIENSITUATION,
    field: 'wohnsitz',
    fieldErrorKey: `shared.form.${validationError.propertyPath}.validation.wohnsitz`,
    validationErrorKey: 'shared.gesuch.validation.wohnsitz.message',
    linkKey: 'shared.gesuch.validation.link.' + validationError.propertyPath,
  }),
};

export const isSpecialValidationError = (error: {
  messageTemplate: string;
}) => {
  return error.messageTemplate in SPECIAL_VALIDATION_ERRORS;
};
