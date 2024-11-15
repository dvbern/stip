import { prepareLanguage } from '@dv/shared/model/type-util';

import { TranslatedPropertyPipe } from './translated-property.pipe';

describe('SharedUiTranslatedPropertyPipe', () => {
  describe('transform', () => {
    const pipe = new TranslatedPropertyPipe();
    const value = {
      nameDe: 'NameDe',
      nameFr: 'NameFr',
      descriptionDe: 'DescriptionDe',
      descriptionFr: 'DescriptionFr',
      notTranslated: 'NotTranslated',
    };

    it.each([
      ['name', 'de', { value: 'NameDe', error: false }],
      ['description', 'fr', { value: 'DescriptionFr', error: false }],
      ['notTranslated', 'de', { value: null, error: true }],
      ['notExisting', 'de', { value: null, error: true }],
      ['name', 'en', { value: null, error: true }],
    ])(
      'should return the correct value for "%s" with language %s',
      (property: any, language, expected) => {
        if (expected.error) {
          expect(() => pipe.transform(value, property, language)).toThrow();
        } else {
          expect(pipe.transform(value, property, language)).toBe(
            expected.value,
          );
        }
      },
    );
  });

  describe('prepareLanguage', () => {
    it.each([
      ['de', { value: 'De', error: false }],
      ['fr', { value: 'Fr', error: false }],
      ['en', { value: null, error: true }],
      ['asdf', { value: null, error: true }],
      [null, { value: null, error: true }],
    ])(
      'should handle current language "%s" correctly',
      (language, expected) => {
        if (expected.error) {
          expect(() => prepareLanguage(language as any)).toThrow();
        } else {
          expect(prepareLanguage(language as any)).toBe(expected.value);
        }
      },
    );
  });
});
