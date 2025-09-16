import { importProvidersFrom, inject } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import {
  TranslocoService,
  TranslocoTestingModule,
  TranslocoTestingOptions,
} from '@jsverse/transloco';
import { OAuthService, provideOAuthClient } from 'angular-oauth2-oidc';
import { of } from 'rxjs';

import {
  CompileTimeConfig,
  SharedModelCompileTimeConfig,
} from '@dv/shared/model/config';
import {
  AbschlussSlim,
  AusbildungsstaetteService,
  AusbildungsstaetteSlim,
  GesuchFormularType,
  GesuchTranche,
  SharedModelGesuch,
} from '@dv/shared/model/gesuch';
// eslint-disable-next-line @nx/enforce-module-boundaries
import { StoreUtilService } from '@dv/shared/util-data-access/store-util';

export type DeepPartial<T> = T extends object
  ? {
      [P in keyof T]?: DeepPartial<T[P]>;
    }
  : T;

export const mockedGesuchAppWritableGesuchState = (overrides?: {
  gesuch?: DeepPartial<SharedModelGesuch>;
  tranche?: DeepPartial<GesuchTranche>;
  formular?: DeepPartial<GesuchFormularType>;
}) => {
  const gesuch = {
    id: '123',
    ...(overrides?.gesuch ?? {}),
    gesuchTrancheToWorkWith: {
      ...(overrides?.tranche ?? {}),
      gesuchFormular: {
        ...(overrides?.formular ?? {}),
      },
      status: 'IN_BEARBEITUNG_GS',
    },
    gesuchStatus: 'IN_BEARBEITUNG_GS',
  } as SharedModelGesuch;
  return {
    gesuch,
    gesuchFormular: gesuch.gesuchTrancheToWorkWith.gesuchFormular,
    cache: {
      gesuch,
      gesuchId: '123',
      gesuchFormular: gesuch.gesuchTrancheToWorkWith.gesuchFormular,
    },
  };
};

const defaultCompileTimeConfig: CompileTimeConfig = {
  appType: 'gesuch-app',
  authClientId: 'stip-gesuch-app',
};

export const provideCompileTimeConfig = (
  compileTimeConfig: CompileTimeConfig = defaultCompileTimeConfig,
) => ({
  provide: SharedModelCompileTimeConfig,
  useFactory: () => new SharedModelCompileTimeConfig(compileTimeConfig),
});

export const mockConfigsState = (
  compileTimeConfig: CompileTimeConfig = defaultCompileTimeConfig,
) => ({ loading: false, error: undefined, compileTimeConfig });

export function configureTestbedTranslateLanguage(language: string) {
  return (testBed: TestBed) => {
    testBed.runInInjectionContext(() => {
      inject(TranslocoService).setActiveLang(language);
    });
  };
}

export function provideSharedPatternVitestTestSetup(
  compileTimeConfig: CompileTimeConfig = defaultCompileTimeConfig,
) {
  return [
    provideOAuthClient(),
    provideCompileTimeConfig(compileTimeConfig),
    importProvidersFrom([RouterTestingModule, NoopAnimationsModule]),
    {
      provide: StoreUtilService,
      useValue: <{ [K in keyof StoreUtilService]: StoreUtilService[K] }>{
        waitForBenutzerData$: vitest.fn(() => (s: unknown) => s) as unknown,
      },
    },
  ];
}

export function provideSharedOAuthServiceWithGesuchstellerJWT() {
  return {
    provide: OAuthService,
    useValue: {
      getAccessToken: () =>
        /**
         * {
         *   ...
         *   "name": "John Doe",
         *   "realm_access": {
         *     "roles": [
         *       "Gesuchsteller"
         *     ]
         *   }
         * }
         */
        'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiR2VzdWNoc3RlbGxlciJdfX0.pW71nQV6d_VLc0a8R-4WxVkXOmega_z2RFZo7nTyYJI',
    },
  };
}

const prepareAbschluss = (abschluss: AbschlussSlim) => {
  return {
    ...abschluss,
    matchName: `${abschluss.bezeichnungDe} - shared.ausbildungskategorie.${abschluss.ausbildungskategorie}`,
  };
};

export const TEST_AUSBILDUNGSSTAETTEN = {
  staette1: {
    nameDe: 'Ausbildungsstaette DE 1',
    nameFr: 'Ausbildungsstaette FR 1',
    id: 'staette1',
    aktiv: true,
    ausbildungsgaenge: [
      {
        id: 'gang1',
        bezeichnungDe: 'Ausbildungsgang 1 DE',
        bezeichnungFr: 'Ausbildungsgang 1 FR',
        bildungskategorie: 'SEKUNDARSTUFE_I',
        bildungsrichtung: 'BERUFLICHE_GRUNDBILDUNG',
        zusatzfrage: 'FACHRICHTUNG',
        aktiv: true,
      },
    ],
  },
} satisfies Record<string, AusbildungsstaetteSlim>;

export const TEST_ABSCHLUESSE = {
  abschlussFachrichtung1: prepareAbschluss({
    id: 'abschluss1',
    bezeichnungDe: 'Abschluss Fachrichtung 1 DE',
    bezeichnungFr: 'Abschluss Fachrichtung 1 FR',
    ausbildungskategorie: 'HOEHERE_FACHSCHULE',
    bildungsrichtung: 'HOEHERE_BERUFSBILDUNG',
    zusatzfrage: 'FACHRICHTUNG',
    aktiv: true,
  }),
  abschlussFachrichtung2: prepareAbschluss({
    id: 'abschluss2',
    bezeichnungDe: 'Abschluss Fachrichtung 2 DE',
    bezeichnungFr: 'Abschluss Fachrichtung 2 FR',
    ausbildungskategorie: 'HOEHERE_FACHSCHULE',
    bildungsrichtung: 'HOEHERE_BERUFSBILDUNG',
    zusatzfrage: 'FACHRICHTUNG',
    aktiv: true,
  }),
  abschlussBerufsbezeichnung1: prepareAbschluss({
    id: 'abschluss3',
    bezeichnungDe: 'Abschluss Berufsbezeichnung 1 DE',
    bezeichnungFr: 'Abschluss Berufsbezeichnung 1 FR',
    ausbildungskategorie: 'BERUFSFACHSCHULEN',
    bildungsrichtung: 'BERUFLICHE_GRUNDBILDUNG',
    zusatzfrage: 'BERUFSBEZEICHNUNG',
    aktiv: true,
  }),
  abschlussBerufsbezeichnung2: prepareAbschluss({
    id: 'abschluss4',
    bezeichnungDe: 'Abschluss Berufsbezeichnung 2 DE',
    bezeichnungFr: 'Abschluss Berufsbezeichnung 2 FR',
    ausbildungskategorie: 'BERUFSFACHSCHULEN',
    bildungsrichtung: 'BERUFLICHE_GRUNDBILDUNG',
    zusatzfrage: 'BERUFSBEZEICHNUNG',
    aktiv: true,
  }),
  abschlussWithoutZusatzfrage1: prepareAbschluss({
    id: 'abschluss5',
    bezeichnungDe: 'Abschluss Without Zusatzfrage 1 DE',
    bezeichnungFr: 'Abschluss Without Zusatzfrage 1 FR',
    ausbildungskategorie: 'BERUFS_UND_HOEHERE_FACHSCHULE',
    bildungsrichtung: 'HOEHERE_BERUFSBILDUNG',
    aktiv: true,
  }),
} satisfies Record<string, AbschlussSlim & { matchName: string }>;

export function provideSharedPatternVitestTestAusbildungstaetten() {
  return {
    provide: AusbildungsstaetteService,
    useValue: {
      getAllAusbildungsstaetteForAuswahl$: () => {
        return of(Object.values(TEST_AUSBILDUNGSSTAETTEN));
      },
      getAllAbschluessForAuswahl$: () => {
        return of(Object.values(TEST_ABSCHLUESSE));
      },
    },
  };
}

export function getTranslocoModule(
  options: TranslocoTestingOptions & {
    de?: Record<string, string>;
    fr?: Record<string, string>;
  } = {},
) {
  const { de = {}, fr = {}, ...restOptions } = options;
  return TranslocoTestingModule.forRoot({
    langs: { de, fr },
    translocoConfig: {
      availableLangs: ['de', 'fr'],
      defaultLang: 'de',
    },
    preloadLangs: true,
    ...restOptions,
  });
}
