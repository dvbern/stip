import { gesuchFormSteps } from './gesuch-form-steps';
import { SharedModelGesuchFormStep } from './shared-model-gesuch-form';

export type SpecialValidationError = {
  step: SharedModelGesuchFormStep;
  field: string;
  fieldErrorKey: string;
  validationErrorKey: string;
  linkKey: string;
};

export const SPECIAL_VALIDATION_ERRORS: Record<string, SpecialValidationError> =
  {
    'dvbern.stip.validation.gesuch.einreichen.svnummer.unique.message': {
      step: gesuchFormSteps.PERSON,
      field: 'sozialversicherungsnummer',
      fieldErrorKey: 'shared.form.person.validation.uniqueSvnummer',
      validationErrorKey: 'shared.gesuch.validation.uniqueSvnummer.message',
      linkKey: 'shared.gesuch.validation.uniqueSvnummer.link',
    },
  };

export const isSpecialValidationError = (error: {
  messageTemplate: string;
}) => {
  return error.messageTemplate in SPECIAL_VALIDATION_ERRORS;
};
