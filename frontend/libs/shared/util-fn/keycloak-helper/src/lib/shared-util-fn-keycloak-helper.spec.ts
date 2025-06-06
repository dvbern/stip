import { HttpHeaders, HttpResponse } from '@angular/common/http';

import {
  BenutzerRole,
  SharedModelBenutzerApi,
} from '@dv/shared/model/benutzer';
import { type } from '@dv/shared/model/type-util';

import {
  createBenutzerListFromRoleLookup,
  hasLocationHeader,
  roleToStateColor,
  toKnownUserErrorType,
} from './shared-util-fn-keycloak-helper';

describe('Keycloak-Helper Functions', () => {
  it('hasLocationHeader - checks correctly for location header in a HttpResponse', () => {
    const httpResponse = new HttpResponse({
      headers: new HttpHeaders({
        Location: 'http://example.com',
      }),
    });
    expect(hasLocationHeader(httpResponse)).toBe(true);
    expect(
      [httpResponse]
        .filter(hasLocationHeader)
        .map((r) => r.headers.get('Location')),
    ).toEqual(['http://example.com']);
  });

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

  it('createBenutzerListFromRoleLookup - to correctly join benutzer lists from different role lookups', () => {
    const benutzer1 = {
      id: '1',
      firstName: 'Max',
      lastName: 'Mustermann',
    } as SharedModelBenutzerApi;
    const benutzer2 = {
      id: '2',
      firstName: 'Erika',
      lastName: 'Musterfrau',
    } as SharedModelBenutzerApi;
    const benutzer3 = {
      id: '3',
      firstName: 'Laura',
      lastName: 'Muster',
    } as SharedModelBenutzerApi;
    const benutzer4 = {
      id: '4',
      firstName: 'Frank',
      lastName: 'Richter',
    } as SharedModelBenutzerApi;

    const roles = {
      Sachbearbeiter: { name: 'V0_Sachbearbeiter', color: 'info' },
      Admin: { name: 'V0_Sachbearbeiter-Admin', color: 'success' },
      Jurist: { name: 'V0_Jurist', color: 'warning' },
    };

    const roleLookups = [
      {
        role: 'V0_Sachbearbeiter' as const,
        benutzers: [benutzer1, benutzer2, benutzer3, benutzer4],
      },
      {
        role: 'V0_Sachbearbeiter-Admin' as const,
        benutzers: [benutzer2, benutzer3],
      },
      {
        role: 'V0_Jurist' as const,
        benutzers: [benutzer1, benutzer3],
      },
    ];

    expect(createBenutzerListFromRoleLookup(roleLookups)).toEqual([
      {
        ...benutzer1,
        name: 'Max Mustermann',
        roles: {
          extraAmount: undefined,
          compact: [roles.Sachbearbeiter, roles.Jurist],
          full: [roles.Sachbearbeiter, roles.Jurist],
        },
      },
      {
        ...benutzer2,
        name: 'Erika Musterfrau',
        roles: {
          extraAmount: undefined,
          compact: [roles.Sachbearbeiter, roles.Admin],
          full: [roles.Sachbearbeiter, roles.Admin],
        },
      },
      {
        ...benutzer3,
        name: 'Laura Muster',
        roles: {
          extraAmount: 1,
          compact: [roles.Sachbearbeiter, roles.Admin],
          full: [roles.Sachbearbeiter, roles.Admin, roles.Jurist],
        },
      },
      {
        ...benutzer4,
        name: 'Frank Richter',
        roles: {
          extraAmount: undefined,
          compact: [roles.Sachbearbeiter],
          full: [roles.Sachbearbeiter],
        },
      },
    ]);
  });
});
