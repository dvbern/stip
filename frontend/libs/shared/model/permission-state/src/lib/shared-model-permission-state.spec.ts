import { AppConfig } from '@dv/shared/model/config';
import { Gesuchstatus, SharedModelGesuch } from '@dv/shared/model/gesuch';

import {
  getGesuchPermissions,
  isNotReadonly,
} from './shared-model-permission-state';

const gesuch: SharedModelGesuch = {
  fallId: '',
  fallNummer: '',
  ausbildungId: '',
  hasPendingAusbildungUnterbruchAntrag: false,
  gesuchsperiode: {
    id: '',
    bezeichnungDe: '',
    bezeichnungFr: '',
    gueltigkeitStatus: 'ENTWURF',
    gesuchsperiodeStart: '',
    gesuchsperiodeStopp: '',
    aufschaltterminStart: '',
    einreichefristNormal: '',
    einreichefristReduziert: '',
    fristNachreichenDokumente: 0,
    gesuchsjahr: {
      id: '',
      bezeichnungDe: '',
      bezeichnungFr: '',
      technischesJahr: 0,
      gueltigkeitStatus: 'ENTWURF',
    },
    ausbKosten_SekII: 0,
    ausbKosten_Tertiaer: 0,
  },
  gesuchStatus: Gesuchstatus.ABKLAERUNG_DURCH_RECHSTABTEILUNG,
  gesuchNummer: '',
  id: '',
  aenderungsdatum: '',
  gesuchTrancheToWorkWith: {
    id: '',
    gueltigAb: '',
    gueltigBis: '',
    status: 'UEBERPRUEFEN',
    typ: 'TRANCHE',
  },
  verfuegt: false,
};

const gesuchAppConfig: AppConfig = {
  type: 'gesuch-app',
  view: 'gesuchsteller',
  keyPrefix: 'gesuch-app',
};
const sachbearbeiterAppConfig: AppConfig = {
  type: 'sachbearbeitung-app',
  view: 'sachbearbeiter',
  keyPrefix: 'sachbearbeitung-app',
};

describe('when App Gesuchsteller', () => {
  it('should be readonly if in bearbeitung Sachbearbeiter', () => {
    gesuch.gesuchStatus = Gesuchstatus.IN_BEARBEITUNG_SB;

    expect(
      getGesuchPermissions(gesuch, gesuchAppConfig, {
        V0_Gesuchsteller: true,
      }).permissions.canWrite,
    ).toBe(false);
  });
});

describe('when App Sachbearbeitung', () => {
  it('should be readonly if in bearbeitung Gesuchsteller', () => {
    gesuch.gesuchStatus = Gesuchstatus.IN_BEARBEITUNG_GS;

    expect(
      getGesuchPermissions(gesuch, sachbearbeiterAppConfig, {
        V0_Sachbearbeiter: true,
      }).permissions.canWrite,
    ).toBe(false);
  });
});

describe('isNotReadonly', () => {
  describe('when appConfig is sachbearbeitung-app', () => {
    it('should return true if user has V0_Sachbearbeiter role', () => {
      const rolesMap = { V0_Sachbearbeiter: true } as const;
      expect(isNotReadonly(sachbearbeiterAppConfig, rolesMap, undefined)).toBe(
        true,
      );
    });

    it('should return true if user has V0_Jurist role', () => {
      const rolesMap = { V0_Jurist: true } as const;
      expect(isNotReadonly(sachbearbeiterAppConfig, rolesMap, undefined)).toBe(
        true,
      );
    });

    it('should return false if user has neither V0_Sachbearbeiter nor V0_Jurist role', () => {
      const rolesMap = { V0_Gesuchsteller: true } as const;
      expect(isNotReadonly(sachbearbeiterAppConfig, rolesMap, undefined)).toBe(
        false,
      );
    });
  });

  describe('when appConfig is gesuch-app', () => {
    it('should return true if not delegated (delegierung is undefined)', () => {
      const rolesMap = {};
      expect(isNotReadonly(gesuchAppConfig, rolesMap, undefined)).toBe(true);
    });

    it('should return true if delegated (delegierung is boolean false)', () => {
      const rolesMap = {};
      const delegierung = false;
      expect(isNotReadonly(gesuchAppConfig, rolesMap, delegierung)).toBe(true);
    });

    it('should return false if delegated (delegierung is boolean true)', () => {
      const rolesMap = {};
      const delegierung = true;
      expect(isNotReadonly(gesuchAppConfig, rolesMap, delegierung)).toBe(false);
    });

    it('should return true if delegated but not angenommen', () => {
      const rolesMap = {};
      expect(
        isNotReadonly(gesuchAppConfig, rolesMap, { status: 'EINGEREICHT' }),
      ).toBe(true);
    });

    it('should return true if delegated and user has V0_Sozialdienst-Mitarbeiter role', () => {
      const rolesMap = { 'V0_Sozialdienst-Mitarbeiter': true } as const;
      expect(
        isNotReadonly(gesuchAppConfig, rolesMap, { status: 'AKZEPTIERT' }),
      ).toBe(true);
    });

    it('should return true if delegated but not angenommen and user has V0_Sozialdienst-Mitarbeiter role', () => {
      const rolesMap = { 'V0_Sozialdienst-Mitarbeiter': true } as const;
      expect(
        isNotReadonly(gesuchAppConfig, rolesMap, { status: 'ABGELEHNT' }),
      ).toBe(true);
    });

    it('should return false if delegated and user does not have V0_Sozialdienst-Mitarbeiter role', () => {
      const rolesMap = { V0_Gesuchsteller: true } as const;
      expect(
        isNotReadonly(gesuchAppConfig, rolesMap, { status: 'AKZEPTIERT' }),
      ).toBe(false);
    });
  });
});
