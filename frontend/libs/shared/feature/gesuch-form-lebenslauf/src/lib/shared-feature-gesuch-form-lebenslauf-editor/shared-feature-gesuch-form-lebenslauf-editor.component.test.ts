import { provideHttpClient } from '@angular/common/http';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { provideMockStore } from '@ngrx/store/testing';
import { render, screen } from '@testing-library/angular';
import { default as userEvent } from '@testing-library/user-event';
import { TranslateTestingModule } from 'ngx-translate-testing';

import { AusbildungsstaetteStore } from '@dv/shared/data-access/ausbildungsstaette';
import { RolesMap } from '@dv/shared/model/benutzer';
import { SharedModelLebenslauf } from '@dv/shared/model/lebenslauf';
import {
  TEST_ABSCHLUESSE,
  mockConfigsState,
  mockedGesuchAppWritableGesuchState,
  provideCompileTimeConfig,
  provideSharedPatternJestTestAusbildungstaetten,
} from '@dv/shared/pattern/jest-test-setup';
import { clickAutocompleteOption } from '@dv/shared/util-fn/comp-test';

import { SharedFeatureGesuchFormLebenslaufEditorComponent } from './shared-feature-gesuch-form-lebenslauf-editor.component';

async function setup(type: SharedModelLebenslauf['type']) {
  return await render(SharedFeatureGesuchFormLebenslaufEditorComponent, {
    inputs: {
      itemSig: { type },
      ausbildungenSig: [],
    },
    imports: [
      TranslateTestingModule.withTranslations({}),
      NoopAnimationsModule,
    ],
    providers: [
      provideHttpClient(),
      provideCompileTimeConfig(),
      provideMockStore({
        initialState: {
          benutzers: {
            rolesMap: {
              V0_Gesuchsteller: true,
            } satisfies RolesMap,
          },
          language: { language: 'de' },
          gesuchs: mockedGesuchAppWritableGesuchState({}),
          ausbildungsstaettes: { ausbildungsstaettes: [] },
          configs: mockConfigsState(),
        },
      }),
      provideSharedPatternJestTestAusbildungstaetten(),
      AusbildungsstaetteStore,
    ],
  });
}

describe(SharedFeatureGesuchFormLebenslaufEditorComponent.name, () => {
  describe('new AUSBILDUNG', () => {
    it('should initially display abschluss, ausbildungAbgeschlossen, start and ende', async () => {
      await setup('AUSBILDUNG');

      expect(
        screen.queryByTestId('lebenslauf-editor-abschluss-select'),
      ).toBeInTheDocument();
      expect(
        screen.queryByTestId('lebenslauf-editor-fachrichtungBerufsbezeichnung'),
      ).toBeNull();
      expect(screen.queryByTestId('lebenslauf-editor-von')).toBeInTheDocument();
      expect(screen.queryByTestId('lebenslauf-editor-bis')).toBeInTheDocument();
      expect(
        screen.queryByTestId('lebenslauf-editor-ausbildung-abgeschlossen'),
      ).toBeInTheDocument();
      expect(
        screen.queryByTestId('lebenslauf-editor-taetigkeitsart'),
      ).toBeNull();
      expect(
        screen.queryByTestId('lebenslauf-editor-taetigkeits-beschreibung'),
      ).toBeNull();
    });

    it('should display berufsbezeichnung/fachrichtung if zusatfrage is set', async () => {
      const renderResult = await setup('AUSBILDUNG');
      const { queryByTestId, detectChanges } = renderResult;

      await clickAutocompleteOption(
        'lebenslauf-editor-abschluss-select',
        TEST_ABSCHLUESSE.abschlussBerufsbezeichnung1.bezeichnungDe,
        TEST_ABSCHLUESSE.abschlussBerufsbezeichnung1.matchName,
      );

      detectChanges();

      expect(
        queryByTestId('lebenslauf-editor-fachrichtungBerufsbezeichnung'),
      ).toBeInTheDocument();

      await clickAutocompleteOption(
        'lebenslauf-editor-abschluss-select',
        TEST_ABSCHLUESSE.abschlussFachrichtung1.bezeichnungDe,
        TEST_ABSCHLUESSE.abschlussFachrichtung1.matchName,
      );

      detectChanges();

      expect(
        queryByTestId('lebenslauf-editor-fachrichtungBerufsbezeichnung'),
      ).toBeInTheDocument();
    });

    it('should reset fachrichtung/berufsbezeichnung if field is hidden', async () => {
      const renderResult = await setup('AUSBILDUNG');
      const { detectChanges } = renderResult;

      const user = userEvent.setup();
      const value = 'Mein Beruf';

      expect(
        screen.queryByTestId('lebenslauf-editor-fachrichtungBerufsbezeichnung'),
      ).toBeNull();

      detectChanges();

      await clickAutocompleteOption(
        'lebenslauf-editor-abschluss-select',
        TEST_ABSCHLUESSE.abschlussBerufsbezeichnung1.bezeichnungDe,
        TEST_ABSCHLUESSE.abschlussBerufsbezeichnung1.matchName,
      );

      detectChanges();

      expect(
        screen.queryByTestId('lebenslauf-editor-fachrichtungBerufsbezeichnung'),
      ).toBeInTheDocument();

      detectChanges();

      await user.type(
        screen.getByTestId('lebenslauf-editor-fachrichtungBerufsbezeichnung'),
        value,
      );

      detectChanges();

      expect(
        screen.getByTestId('lebenslauf-editor-fachrichtungBerufsbezeichnung'),
      ).toHaveValue(value);

      await clickAutocompleteOption(
        'lebenslauf-editor-abschluss-select',
        TEST_ABSCHLUESSE.abschlussWithoutZusatzfrage1.bezeichnungDe,
        TEST_ABSCHLUESSE.abschlussWithoutZusatzfrage1.matchName,
      );

      detectChanges();

      expect(
        screen.queryByTestId('lebenslauf-editor-fachrichtungBerufsbezeichnung'),
      ).toBeNull();

      await clickAutocompleteOption(
        'lebenslauf-editor-abschluss-select',
        TEST_ABSCHLUESSE.abschlussBerufsbezeichnung1.bezeichnungDe,
        TEST_ABSCHLUESSE.abschlussBerufsbezeichnung1.matchName,
      );

      detectChanges();

      expect(
        screen.queryByTestId('lebenslauf-editor-fachrichtungBerufsbezeichnung'),
      ).toBeInTheDocument();

      expect(
        screen.getByTestId('lebenslauf-editor-fachrichtungBerufsbezeichnung'),
      ).toHaveValue('');
    });
  });

  describe('new TAETIGKEIT', () => {
    it('should not have ausbildungspezifisch fields', async () => {
      await setup('TAETIGKEIT');

      expect(
        screen.queryByTestId('lebenslauf-editor-abschluss-select'),
      ).toBeNull();
      expect(
        screen.queryByTestId('lebenslauf-editor-fachrichtungBerufsbezeichnung'),
      ).toBeNull();
      expect(
        screen.queryByTestId('lebenslauf-editor-titel-des-abschlusses'),
      ).toBeNull();
      expect(
        screen.queryByTestId('lebenslauf-editor-ausbildung-abgeschlossen'),
      ).toBeNull();
      expect(
        screen.queryByTestId('lebenslauf-editor-taetigkeitsart'),
      ).toBeInTheDocument();
      expect(
        screen.queryByTestId('lebenslauf-editor-taetigkeitsBeschreibung'),
      ).toBeInTheDocument();
    });
  });
});
