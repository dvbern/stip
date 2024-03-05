import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { provideMockStore } from '@ngrx/store/testing';
import { render } from '@testing-library/angular';
import { default as userEvent } from '@testing-library/user-event';
import { TranslateTestingModule } from 'ngx-translate-testing';

import {
  Ausbildung,
  GesuchFormularUpdate,
  PersonInAusbildung,
  Wohnsitz,
} from '@dv/shared/model/gesuch';
import { provideMaterialDefaultOptions } from '@dv/shared/pattern/angular-material-config';
import { mockElementScrollIntoView } from '@dv/shared/util-fn/comp-test';

import { SharedFeatureGesuchFormEinnahmenkostenComponent } from './shared-feature-gesuch-form-einnahmenkosten.component';

async function setup(gesuchFormular: GesuchFormularUpdate) {
  mockElementScrollIntoView();
  return await render(SharedFeatureGesuchFormEinnahmenkostenComponent, {
    imports: [
      TranslateTestingModule.withTranslations({ de: {} }),
      NoopAnimationsModule,
    ],
    providers: [
      provideMockStore({
        initialState: {
          ausbildungsstaettes: { ausbildungsstaettes: [] },
          gesuchs: {
            gesuchFormular,
          },
          language: { language: 'de' },
        },
      }),
      provideMaterialDefaultOptions(),
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
      const { queryByTestId } = await setup({
        personInAusbildung: undefined,
        ausbildung: createEmptyAusbildung(),
        familiensituation: { elternVerheiratetZusammen: true },
      });

      expect(
        queryByTestId('gesuch-form-einnahmenkosten-data-incomplete-warning'),
      ).toBeInTheDocument();
    });

    it('should display warning if ausbildung is undefined', async () => {
      const { queryByTestId } = await setup({
        personInAusbildung: createEmptyPersonInAusbildung(),
        ausbildung: undefined,
        familiensituation: { elternVerheiratetZusammen: true },
      });

      expect(
        queryByTestId('gesuch-form-einnahmenkosten-data-incomplete-warning'),
      ).toBeInTheDocument();
    });

    it('should display warning if familiensituation is undefined', async () => {
      const { queryByTestId } = await setup({
        personInAusbildung: createEmptyPersonInAusbildung(),
        ausbildung: createEmptyAusbildung(),
        familiensituation: undefined,
      });

      expect(
        queryByTestId('gesuch-form-einnahmenkosten-data-incomplete-warning'),
      ).toBeInTheDocument();
    });
  });

  describe('visibility rules for field "auswaertigeMittagessenProWoche"', () => {
    it('should not display auswaertigeMittagessenProWoche if personInAusbildung has wohnsitz "eigener Haushalt"', async () => {
      const { queryByTestId, fixture } =
        await setupWithPreparedGesuchWithWohnsitz(Wohnsitz.EIGENER_HAUSHALT);

      await fixture.whenStable();

      expect(
        queryByTestId('form-einnahmen-kosten-auswaertigeMittagessenProWoche'),
      ).toBeInTheDocument();
    });

    it('should not display auswaertigeMittagessenProWoche if personInAusbildung has wohnsitz "Familie"', async () => {
      const { queryByTestId } = await setupWithPreparedGesuchWithWohnsitz(
        Wohnsitz.FAMILIE,
      );

      expect(
        queryByTestId('form-einnahmen-kosten-auswaertigeMittagessenProWoche'),
      ).toBeInTheDocument();
    });

    it('should not display auswaertigeMittagessenProWoche if personInAusbildung has wohnsitz "Mutter Vater"', async () => {
      const { queryByTestId } = await setupWithPreparedGesuchWithWohnsitz(
        Wohnsitz.MUTTER_VATER,
      );

      expect(
        queryByTestId('form-einnahmen-kosten-auswaertigeMittagessenProWoche'),
      ).toBeInTheDocument();
    });
  });

  describe('visibility rules for field "wohnkosten"', () => {
    it('should display wohnkosten if personInAusbildung has wohnsitz "eigener Haushalt"', async () => {
      const { queryByTestId } = await setupWithPreparedGesuchWithWohnsitz(
        Wohnsitz.EIGENER_HAUSHALT,
      );

      expect(
        queryByTestId('form-einnahmen-kosten-wohnkosten'),
      ).toBeInTheDocument();
    });

    it('should display wohnkosten if personInAusbildung has wohnsitz "Familie"', async () => {
      const { queryByTestId } = await setupWithPreparedGesuchWithWohnsitz(
        Wohnsitz.FAMILIE,
      );

      expect(
        queryByTestId('form-einnahmen-kosten-wohnkosten'),
      ).toBeInTheDocument();
    });

    it('should display wohnkosten if personInAusbildung has wohnsitz "Mutter Vater"', async () => {
      const { queryByTestId } = await setupWithPreparedGesuchWithWohnsitz(
        Wohnsitz.MUTTER_VATER,
      );

      expect(
        queryByTestId('form-einnahmen-kosten-wohnkosten'),
      ).toBeInTheDocument();
    });
  });

  describe('should have conditional required validation for some fields', () => {
    it('field zulagen should be optional if no kind has been specified', async () => {
      const { getByTestId } = await setupWithPreparedGesuchWithWohnsitz(
        Wohnsitz.FAMILIE,
        {
          kinds: undefined,
        },
      );
      await userEvent.click(getByTestId('button-save-continue'));

      expect(getByTestId('form-einnahmen-kosten-zulagen')).toHaveClass(
        'ng-valid',
      );
    });

    it('field zulagen should not be optional if a kind has been specified', async () => {
      const { getByTestId } = await setupWithPreparedGesuchWithWohnsitz(
        Wohnsitz.FAMILIE,
        { kinds: [{} as never] },
      );

      await userEvent.click(getByTestId('button-save-continue'));
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
      const { queryByTestId } = await setup({
        personInAusbildung: createEmptyPersonInAusbildung(),
        ausbildung: createEmptyAusbildung(),
        familiensituation: {
          elternVerheiratetZusammen: false,
          gerichtlicheAlimentenregelung: true,
        },
      });

      expect(
        queryByTestId('gesuch-form-einnahmenkosten-data-incomplete-warning'),
      ).toBeNull();
      expect(
        queryByTestId('form-einnahmen-kosten-alimente'),
      ).toBeInTheDocument();
    });
  });
});

function createEmptyAusbildung(): Ausbildung {
  return {
    fachrichtung: '',
    ausbildungBegin: '',
    ausbildungEnd: '',
    pensum: 'VOLLZEIT',
  };
}

function createEmptyPersonInAusbildung(): PersonInAusbildung {
  return {
    adresse: {
      land: 'CH',
      strasse: '',
      ort: '',
      plz: '',
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
    nationalitaet: 'CH',
    quellenbesteuert: false,
    heimatort: '',
    sozialhilfebeitraege: false,
    korrespondenzSprache: 'DEUTSCH',
  };
}
