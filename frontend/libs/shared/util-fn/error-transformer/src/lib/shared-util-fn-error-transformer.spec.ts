import {
  byErrorType,
  sharedUtilFnErrorTransformer,
} from './shared-util-fn-error-transformer';

describe('sharedUtilFnErrorTransformer - fn', () => {
  it('should use default message as fallback message', () => {
    const error = sharedUtilFnErrorTransformer('unknown error');
    expect(error).toEqual({
      type: 'unknownError',
      error: 'unknown error',
      message: 'Unknown Error',
      messageKey: 'shared.genericError.general',
    });
  });
  it('should extract error message', () => {
    const error = new Error('boom');
    expect(sharedUtilFnErrorTransformer(error)).toEqual({
      type: 'unknownError',
      error,
      messageKey: 'shared.genericError.general',
    });
  });
  it('should extract validation errors', () => {
    const validationErrors = [
      {
        messageTemplate:
          'dvbern.stip.validation.gesuch.einreichen.svnummer.unique.message',
        message: 'Es darf nur ein Gesuch pro Gesuchsteller eingereicht werden',
        propertyPath: '',
      },
    ];
    const error = {
      error: {
        validationErrors,
        validationWarnings: [],
        hasDocuments: null,
      },
      message: 'Bad Request',
      status: 400,
    };
    expect(sharedUtilFnErrorTransformer(error)).toEqual({
      type: 'validationError',
      validationErrors,
      validationWarnings: [],
      hasDocuments: null,
      message: 'Es darf nur ein Gesuch pro Gesuchsteller eingereicht werden',
      messageKey:
        'dvbern.stip.validation.gesuch.einreichen.svnummer.unique.message',
      messageKeys: [
        'dvbern.stip.validation.gesuch.einreichen.svnummer.unique.message',
      ],
      status: error.status,
    });
  });
});

describe('sharedUtilFnErrorTransformer - filter', () => {
  const validationErrorsCollection = [
    [
      {
        messageTemplate: 'dv.error.validation.1',
        message: 'error validation 1',
        propertyPath: 'foo.bar.baz',
      },
      {
        messageTemplate: 'dv.error.validation.2',
        message: 'error validation 2',
        propertyPath: 'foo.bar.baz',
      },
    ],
    [
      {
        messageTemplate: 'dv.error.validation.3',
        message: 'error validation 3',
        propertyPath: 'foo.bar.baz',
      },
    ],
  ];
  const rawErrors = [
    'error 2',
    new Error('error 3'),
    {
      error: new Error('error 1'),
    },
    {
      error: {
        stack: '1/2/3',
      },
      message: 'Internal Server Error',
      status: 500,
    },
    {
      error: {
        validationErrors: validationErrorsCollection[0],
        validationWarnings: [],
        hasDocuments: null,
      },
      status: 400,
    },
    {
      error: {
        validationErrors: validationErrorsCollection[1],
        validationWarnings: [],
        hasDocuments: null,
      },
      status: 400,
    },
  ];

  it('should filter a stream of errors by validationError', () => {
    const errors = rawErrors
      .map(sharedUtilFnErrorTransformer)
      .filter(byErrorType('validationError'));
    expect(errors).toEqual([
      {
        type: 'validationError',
        validationErrors: validationErrorsCollection[0],
        validationWarnings: [],
        hasDocuments: null,
        message: 'error validation 1',
        messageKey: 'dv.error.validation.1',
        messageKeys: ['dv.error.validation.1', 'dv.error.validation.2'],
        status: 400,
      },
      {
        type: 'validationError',
        validationErrors: validationErrorsCollection[1],
        validationWarnings: [],
        hasDocuments: null,
        message: 'error validation 3',
        messageKey: 'dv.error.validation.3',
        messageKeys: ['dv.error.validation.3'],
        status: 400,
      },
    ]);
  });
});
