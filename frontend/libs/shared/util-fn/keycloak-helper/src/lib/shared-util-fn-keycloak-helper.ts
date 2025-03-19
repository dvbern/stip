import { HttpResponse } from '@angular/common/http';

import {
  BenutzerRole,
  SharedModelBenutzer,
  SharedModelBenutzerApi,
  SharedModelBenutzerRole,
  SharedModelUserAdminError,
} from '@dv/shared/model/benutzer';
import { SharedModelState } from '@dv/shared/model/state-colors';

export const toKnownUserErrorType = (error: unknown, fallbackType: string) => {
  const parsed = SharedModelUserAdminError.parse(error);
  return parsed.type ?? fallbackType;
};

export const getCurrentUrl = (document: Document) => {
  return document.location.origin;
};

type HttpResponseWithLocation = HttpResponse<unknown> & {
  headers: { get: (header: 'Location') => string };
};

export const hasLocationHeader = (
  response: HttpResponse<unknown>,
): response is HttpResponseWithLocation => {
  return response.headers.has('Location');
};

export const roleToStateColor = (role: BenutzerRole): SharedModelState => {
  switch (role) {
    case 'V0_Sachbearbeiter':
      return 'info';
    case 'V0_Admin':
      return 'success';
    case 'V0_Jurist':
      return 'warning';
    default:
      return 'danger';
  }
};

/**
 * Joins the user lists from different roles into one list and adds the roles as a property to the Benutzer object.
 */
export const createBenutzerListFromRoleLookup = (
  benutzersByRole: {
    benutzers: SharedModelBenutzerApi[];
    role: 'V0_Sachbearbeiter' | 'V0_Admin' | 'V0_Jurist';
  }[],
): SharedModelBenutzer[] => {
  return Object.values(
    benutzersByRole.reduce<
      Record<
        string,
        SharedModelBenutzerApi & {
          name: string;
          roles: SharedModelBenutzerRole[];
        }
      >
    >(
      (allById, { role, benutzers }) =>
        benutzers.reduce(
          (all, benutzer) => ({
            ...all,
            [benutzer.id]: {
              ...benutzer,
              name: `${benutzer.firstName} ${benutzer.lastName}`,
              roles: [
                ...(all[benutzer.id]?.roles ?? []),
                { name: role, color: roleToStateColor(role) },
              ],
            },
          }),
          allById,
        ),
      {},
    ),
  ).map((benutzer) => ({
    ...benutzer,
    roles: {
      extraAmount:
        benutzer.roles.length > 2 ? benutzer.roles.length - 2 : undefined,
      compact: benutzer.roles.slice(0, 2),
      full: benutzer.roles,
    },
  }));
};
