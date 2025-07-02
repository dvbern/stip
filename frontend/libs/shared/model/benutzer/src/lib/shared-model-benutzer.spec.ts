import { type } from '@dv/shared/model/type-util';

import {
  BenutzerRole,
  roleToStateColor,
  toKnownUserErrorType,
} from './shared-model-benutzer';

describe('Shared model benutzer', () => {
  it('toKnownUserError - to correctly parse email exist error', () => {
    const error = {
      error: {
        errorMessage: 'User exists with same email',
      },
    };
    expect(toKnownUserErrorType(error, 'test')).toEqual('emailExists');
  });

  it('toKnownUserError - to correctly parse unknown error', () => {
    const error = {
      error: {
        errorMessage: 'Unknown error',
      },
    };
    expect(toKnownUserErrorType(error, 'test')).toEqual('test');
  });

  it.each([
    type<[BenutzerRole, string][]>([
      ['V0_Sachbearbeiter', 'info'],
      ['V0_Sachbearbeiter-Admin', 'success'],
      ['V0_Jurist', 'warning'],
    ]),
  ])(
    'roleToStateColor - to correctly map roles to state colors',
    ([role, color]) => {
      expect(roleToStateColor(role)).toEqual(color);
    },
  );
});
