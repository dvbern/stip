import { GENERIC_REQUIRED_ERROR } from '@dv/shared/model/error';

import {
  DEFAULT_ERROR_KEY,
  getTranslationFromValidation,
} from './gesuch-app-feature-gesuch-form-abschluss.component';

describe('getTranslationFromValidation', () => {
  it('should return null on empty message and messageTemplate', () => {
    const result = getTranslationFromValidation({})({
      message: '',
      messageTemplate: '',
    });
    expect(result).toBeNull();
  });

  it('should return message as key content on empty messageTemplate', () => {
    const result = getTranslationFromValidation({})({
      message: 'hello',
      messageTemplate: '',
    });
    expect(result).toStrictEqual({ key: 'hello' });
  });

  it('should return null if messageKey is not found and message is empty', () => {
    const result = getTranslationFromValidation({ someKey: 'not being found' })(
      {
        message: '',
        messageTemplate: 'otherKey',
      },
    );
    expect(result).toBeNull();
  });

  it('should return messageTemplate as key content if found in translations', () => {
    const messageTemplate = 'someKey';
    const key = 'shared.gesuch.validation.' + messageTemplate;
    const result = getTranslationFromValidation({
      [key]: 'found',
    })({
      message: '',
      messageTemplate,
    });
    expect(result).toStrictEqual({ key });
  });

  it('should return the correct translation params from given propertyPath if it is a GENERIC_REQUIRED_ERROR', () => {
    const translations = {
      'shared.auszahlung.title': 'Auszahlung',
      [`shared.gesuch.validation.${GENERIC_REQUIRED_ERROR}`]: 'found',
    };
    const result = getTranslationFromValidation(translations)({
      message: '',
      messageTemplate: GENERIC_REQUIRED_ERROR,
      propertyPath: 'gesuchTranchen[0].gesuchFormular.auszahlung',
    });
    expect(result).toStrictEqual({
      key: `shared.gesuch.validation.${GENERIC_REQUIRED_ERROR}`,
      params: { section: 'Auszahlung' },
    });
  });

  it('should return the default error key if the propertyPath is not valid', () => {
    const translations = {
      [`shared.gesuch.validation.${GENERIC_REQUIRED_ERROR}`]: 'found',
    };
    const result = getTranslationFromValidation(translations)({
      message: '',
      messageTemplate: GENERIC_REQUIRED_ERROR,
      propertyPath: 'invalid.path',
    });
    expect(result).toStrictEqual({ key: DEFAULT_ERROR_KEY });
  });
});
