import { provideHttpClient } from '@angular/common/http';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { provideMockStore } from '@ngrx/store/testing';
import { render } from '@testing-library/angular';
import userEvent from '@testing-library/user-event';
import { TranslateTestingModule } from 'ngx-translate-testing';

import { AusbildungsstaetteStore } from '@dv/shared/data-access/ausbildungsstaette';
import { RolesMap } from '@dv/shared/model/benutzer';
import {
  Ausbildung,
  GesuchFormular,
  GesuchFormularUpdate,
  PersonInAusbildung,
  Wohnsitz,
} from '@dv/shared/model/gesuch';
import {
  mockConfigsState,
  mockedGesuchAppWritableGesuchState,
  provideSharedPatternVitestTestAusbildungstaetten,
  provideSharedPatternVitestTestSetup,
} from '@dv/shared/pattern/vitest-test-setup';
import { provideMaterialDefaultOptions } from '@dv/shared/util/form';
import { mockElementScrollIntoView } from '@dv/shared/util-fn/comp-test';

import { SharedFeatureGesuchFormEinnahmenkostenComponent } from './shared-feature-gesuch-form-einnahmenkosten.component';

async function setup(formular: GesuchFormular) {
  mockElementScrollIntoView();
  return await render(SharedFeatureGesuchFormEinnahmenkostenComponent, {
    imports: [
      TranslateTestingModule.withTranslations({ de: {} }),
      NoopAnimationsModule,
    ],
    providers: [
      provideHttpClient(),
      provideMockStore({
        initialState: {
          benutzers: {
            rolesMap: {
              V0_Gesuchsteller: true,
            } satisfies RolesMap,
          },
          gesuchs: mockedGesuchAppWritableGesuchState({
            tranche: { id: '1', typ: 'TRANCHE' },
            gesuch: {
              gesuchsperiode: {
                gesuchsjahr: {
                  technischesJahr: new Date().getFullYear(),
                },
              },
            },
            formular,
          }),
          language: { language: 'de' },
          configs: mockConfigsState(),
        },
      }),
      provideMaterialDefaultOptions(),
      provideSharedPatternVitestTestSetup(),
      provideSharedPatternVitestTestAusbildungstaetten(),
      AusbildungsstaetteStore,
    ],
  });
}

async function setupWithPreparedGesuchWithWohnsitz(
  wohnsitz: Wohnsitz,
  overrideGesuchFormular: Partial<GesuchFormularUpdate> = {},
) {
  const gesuchFormular = {
    familiensituation: { elternVerheiratetZusammen: true },
    personInAusbildung: createEmptyPersonInAusbildung(),
    ausbildung: createEmptyAusbildung(),
    ...overrideGesuchFormular,
  };
  gesuchFormular.personInAusbildung.wohnsitz = wohnsitz;

  return setup(gesuchFormular);
}

describe(SharedFeatureGesuchFormEinnahmenkostenComponent.name, () => {
  describe('should display warning if not all of personInAusbildung, familiensituation, ausbildung are defined', () => {
    it('should display warning if personInAusbildung is undefined', async () => {
      const { queryByTestId, detectChanges } = await setup({
        personInAusbildung: undefined,
        ausbildung: createEmptyAusbildung(),
        familiensituation: { elternVerheiratetZusammen: true },
      });

      detectChanges();

      expect(
        queryByTestId('gesuch-form-einnahmenkosten-data-incomplete-warning'),
      ).toBeInTheDocument();
    });

    it('should display warning if familiensituation is undefined', async () => {
      const { queryByTestId, detectChanges } = await setup({
        personInAusbildung: createEmptyPersonInAusbildung(),
        ausbildung: createEmptyAusbildung(),
        familiensituation: undefined,
      });

      detectChanges();

      expect(
        queryByTestId('gesuch-form-einnahmenkosten-data-incomplete-warning'),
      ).toBeInTheDocument();
    });
  });

  describe('visibility rules for field "auswaertigeMittagessenProWoche"', () => {
    it('should not display auswaertigeMittagessenProWoche if personInAusbildung has wohnsitz "eigener Haushalt"', async () => {
      const { queryByTestId, detectChanges } =
        await setupWithPreparedGesuchWithWohnsitz(Wohnsitz.EIGENER_HAUSHALT);

      detectChanges();

      expect(
        queryByTestId('form-einnahmen-kosten-auswaertigeMittagessenProWoche'),
      ).not.toBeInTheDocument();
    });

    it('should display auswaertigeMittagessenProWoche if personInAusbildung has wohnsitz "Familie"', async () => {
      const { queryByTestId, detectChanges } =
        await setupWithPreparedGesuchWithWohnsitz(Wohnsitz.FAMILIE);

      detectChanges();

      expect(
        queryByTestId('form-einnahmen-kosten-auswaertigeMittagessenProWoche'),
      ).toBeInTheDocument();
    });

    it('should display auswaertigeMittagessenProWoche if personInAusbildung has wohnsitz "Mutter Vater"', async () => {
      const { queryByTestId, detectChanges } =
        await setupWithPreparedGesuchWithWohnsitz(Wohnsitz.MUTTER_VATER);

      detectChanges();

      expect(
        queryByTestId('form-einnahmen-kosten-auswaertigeMittagessenProWoche'),
      ).toBeInTheDocument();
    });
  });

  describe('visibility rules for field "wohnkosten"', () => {
    it('should display wohnkosten if personInAusbildung has wohnsitz "eigener Haushalt"', async () => {
      const { queryByTestId, detectChanges } =
        await setupWithPreparedGesuchWithWohnsitz(Wohnsitz.EIGENER_HAUSHALT);

      detectChanges();

      expect(
        queryByTestId('form-einnahmen-kosten-wohnkosten'),
      ).toBeInTheDocument();
    });

    it('should not display wohnkosten if personInAusbildung has wohnsitz "Familie"', async () => {
      const { queryByTestId, detectChanges } =
        await setupWithPreparedGesuchWithWohnsitz(Wohnsitz.FAMILIE);

      detectChanges();

      expect(
        queryByTestId('form-einnahmen-kosten-wohnkosten'),
      ).not.toBeInTheDocument();
    });

    it('should not display wohnkosten if personInAusbildung has wohnsitz "Mutter Vater"', async () => {
      const { queryByTestId, detectChanges } =
        await setupWithPreparedGesuchWithWohnsitz(Wohnsitz.MUTTER_VATER);

      detectChanges();

      expect(
        queryByTestId('form-einnahmen-kosten-wohnkosten'),
      ).not.toBeInTheDocument();
    });
  });

  describe('visibility rules for field "wgWohnend"', () => {
    it('should display wgWohnend if personInAusbildung has wohnsitz "eigener Haushalt"', async () => {
      const { queryByTestId, detectChanges } =
        await setupWithPreparedGesuchWithWohnsitz(Wohnsitz.EIGENER_HAUSHALT);

      detectChanges();

      expect(
        queryByTestId('form-einnahmen-kosten-wgWohnend'),
      ).toBeInTheDocument();
    });

    it('should not display wgWohnend if personInAusbildung has wohnsitz "Familie"', async () => {
      const { queryByTestId, detectChanges } =
        await setupWithPreparedGesuchWithWohnsitz(Wohnsitz.FAMILIE);

      detectChanges();

      expect(
        queryByTestId('form-einnahmen-kosten-wgWohnend'),
      ).not.toBeInTheDocument();
    });
  });

  describe('should have conditional required validation for some fields', () => {
    it('field zulagen should be optional if no kind has been specified', async () => {
      const { getByTestId, detectChanges } =
        await setupWithPreparedGesuchWithWohnsitz(Wohnsitz.FAMILIE, {
          kinds: undefined,
        });

      detectChanges();
      await userEvent.click(getByTestId('button-save-continue'));

      detectChanges();

      expect(getByTestId('form-einnahmen-kosten-zulagen')).toHaveClass(
        'ng-valid',
      );
    });

    it('field zulagen should not be optional if a kind has been specified', async () => {
      const { getByTestId, detectChanges } =
        await setupWithPreparedGesuchWithWohnsitz(Wohnsitz.FAMILIE, {
          kinds: [{} as never],
        });

      detectChanges();
      await userEvent.click(getByTestId('button-save-continue'));

      detectChanges();

      expect(getByTestId('form-einnahmen-kosten-zulagen')).toHaveClass(
        'ng-invalid',
      );
    });
  });

  describe('should display alimente field correctly based on current state', () => {
    it('should not display alimente field if gerichtlicheAlimentenregelung is undefined', async () => {
      const { queryByTestId, detectChanges } = await setup({
        personInAusbildung: createEmptyPersonInAusbildung(),
        ausbildung: createEmptyAusbildung(),
        familiensituation: {
          elternVerheiratetZusammen: true,
        },
      });

      detectChanges();

      expect(
        queryByTestId('gesuch-form-einnahmenkosten-data-incomplete-warning'),
      ).toBeNull();
      expect(queryByTestId('form-einnahmen-kosten-alimente')).toBeNull();
    });

    it('should not display alimente field if gerichtlicheAlimentenregelung is false', async () => {
      const { queryByTestId, detectChanges } = await setup({
        personInAusbildung: createEmptyPersonInAusbildung(),
        ausbildung: createEmptyAusbildung(),
        familiensituation: {
          elternVerheiratetZusammen: false,
          gerichtlicheAlimentenregelung: false,
        },
      });

      detectChanges();

      expect(
        queryByTestId('gesuch-form-einnahmenkosten-data-incomplete-warning'),
      ).toBeNull();
      expect(queryByTestId('form-einnahmen-kosten-alimente')).toBeNull();
    });

    it('should display alimente field if gerichtlicheAlimentenregelung is true', async () => {
      const { queryByTestId, detectChanges } = await setup({
        personInAusbildung: createEmptyPersonInAusbildung(),
        ausbildung: createEmptyAusbildung(),
        familiensituation: {
          elternVerheiratetZusammen: false,
          gerichtlicheAlimentenregelung: true,
        },
      });

      detectChanges();

      expect(
        queryByTestId('gesuch-form-einnahmenkosten-data-incomplete-warning'),
      ).toBeNull();
      expect(
        queryByTestId('form-einnahmen-kosten-alimente'),
      ).toBeInTheDocument();
    });
  });

  describe('should display betreuungskostenKinder field correctly based on current state', () => {
    it('should not display betreuungskostenKinder field if no kinds', async () => {
      const { queryByTestId, detectChanges } = await setup({
        personInAusbildung: createEmptyPersonInAusbildung(),
        ausbildung: createEmptyAusbildung(),
        familiensituation: {
          elternVerheiratetZusammen: true,
        },
        kinds: undefined,
      });

      detectChanges();

      expect(
        queryByTestId('gesuch-form-einnahmenkosten-data-incomplete-warning'),
      ).toBeNull();
      expect(
        queryByTestId('form-einnahmen-kosten-betreuungskostenKinder'),
      ).toBeNull();
    });

    it('should display betreuungskostenKinder field if there are kinds', async () => {
      const { queryByTestId, detectChanges } = await setup({
        personInAusbildung: createEmptyPersonInAusbildung(),
        ausbildung: createEmptyAusbildung(),
        familiensituation: {
          elternVerheiratetZusammen: true,
        },
        kinds: [{} as never],
      });

      detectChanges();

      expect(
        queryByTestId('gesuch-form-einnahmenkosten-data-incomplete-warning'),
      ).toBeNull();
      expect(
        queryByTestId('form-einnahmen-kosten-betreuungskostenKinder'),
      ).toBeInTheDocument();
    });
  });
});

function createEmptyAusbildung(): Ausbildung {
  return {
    fallId: 'asdf',
    ausbildungBegin: '',
    ausbildungEnd: '',
    pensum: 'VOLLZEIT',
    ausbildungsgang: {
      id: '',
      abschluss: {
        id: '',
        bezeichnungDe: '',
        bezeichnungFr: '',
        aktiv: false,
        askForBerufsmaturitaet: false,
        ausbildungskategorie: 'BRUECKENANGEBOT',
        berufsbefaehigenderAbschluss: false,
        bfsKategorie: 1,
        bildungskategorie: 'TERTIAERSTUFE_A',
        bildungsrichtung: 'ALLGEMEINBILDENDE_SCHULE',
        ferien: 'LEHRE',
      },
      ausbildungsstaette: {
        id: '',
        nameDe: '',
        nameFr: '',
        aktiv: false,
      },
      aktiv: false,
    },
    editable: true,
    status: 'AKTIV',
  };
}

function createEmptyPersonInAusbildung(): PersonInAusbildung {
  return {
    adresse: {
      strasse: '',
      ort: '',
      plz: '',
      landId: '',
    },
    sozialversicherungsnummer: '',
    anrede: 'FRAU',
    vorname: '',
    nachname: '',
    identischerZivilrechtlicherWohnsitz: true,
    geburtsdatum: '1990-01-01',
    email: '',
    telefonnummer: '',
    wohnsitz: 'FAMILIE',
    nationalitaetId: '',
    heimatort: '',
    sozialhilfebeitraege: false,
    korrespondenzSprache: 'DEUTSCH',
  };
}
