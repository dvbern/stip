import { input } from '@angular/core';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { provideMockStore } from '@ngrx/store/testing';
import { render, screen } from '@testing-library/angular';
import { default as userEvent } from '@testing-library/user-event';
import { TranslateTestingModule } from 'ngx-translate-testing';

import {
  LebenslaufAusbildungsArt,
  LebenslaufItemUpdate,
} from '@dv/shared/model/gesuch';
import { SharedModelLebenslauf } from '@dv/shared/model/lebenslauf';
import { clickMatSelectOption } from '@dv/shared/util-fn/comp-test';

import { SharedFeatureGesuchFormLebenslaufEditorComponent } from './shared-feature-gesuch-form-lebenslauf-editor.component';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
async function setup(type: SharedModelLebenslauf['type']) {
  return await render(SharedFeatureGesuchFormLebenslaufEditorComponent, {
    imports: [
      TranslateTestingModule.withTranslations({}),
      NoopAnimationsModule,
    ],
    providers: [
      provideMockStore({
        initialState: {
          language: { language: 'de' },
          gesuchs: { gesuchFormular: {} },
          ausbildungsstaettes: { ausbildungsstaettes: [] },
        },
      }),
    ],
    componentProperties: {
      itemSig: input({ type } as Partial<SharedModelLebenslauf>),
      ausbildungenSig: input([] as LebenslaufItemUpdate[]),
    },
  });
}

describe(SharedFeatureGesuchFormLebenslaufEditorComponent.name, () => {
  describe('new AUSBILDUNG', () => {
    it('should initially display bildungsart, ausbildungAbgeschlossen, start and ende', async () => {
      await setup('AUSBILDUNG');

      expect(
        screen.queryByTestId('lebenslauf-editor-ausbildungsart-select'),
      ).toBeInTheDocument();
      expect(
        screen.queryByTestId('lebenslauf-editor-berufsbezeichnung'),
      ).toBeNull();
      expect(screen.queryByTestId('lebenslauf-editor-fachrichtung')).toBeNull();
      expect(
        screen.queryByTestId('lebenslauf-editor-titel-des-abschlusses'),
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

    it('should display berufsbezeichnung if one of {EIDGENOESSISCHES_BERUFSATTEST, EIDGENOESSISCHES_FAEHIGKEITSZEUGNIS} is selected', async () => {
      const { queryByTestId } = await setup('AUSBILDUNG');

      const bildungsartenWhichNeedBerufsbezeichnung = [
        LebenslaufAusbildungsArt.EIDGENOESSISCHES_BERUFSATTEST,
        LebenslaufAusbildungsArt.EIDGENOESSISCHES_FAEHIGKEITSZEUGNIS,
      ];
      const bildsartenWhichDontNeedBerufsbezeichnung = Object.values(
        LebenslaufAusbildungsArt,
      ).filter(
        (ausbildungsart) =>
          !bildungsartenWhichNeedBerufsbezeichnung.includes(ausbildungsart),
      );

      for (const bildungsart of bildungsartenWhichNeedBerufsbezeichnung) {
        await clickMatSelectOption(
          'lebenslauf-editor-ausbildungsart-select',
          bildungsart,
        );

        expect(
          queryByTestId('lebenslauf-editor-berufsbezeichnung'),
        ).toBeInTheDocument();
      }

      for (const bildungsart of bildsartenWhichDontNeedBerufsbezeichnung) {
        await clickMatSelectOption(
          'lebenslauf-editor-ausbildungsart-select',
          bildungsart,
        );

        expect(queryByTestId('lebenslauf-editor-berufsbezeichnung')).toBeNull();
      }
    });

    it('should reset berufsbezeichnung if field is hidden', async () => {
      await setup('AUSBILDUNG');
      const user = userEvent.setup();
      const value = 'Mein Beruf';

      expect(
        screen.queryByTestId('lebenslauf-editor-berufsbezeichnung'),
      ).toBeNull();

      await clickMatSelectOption(
        'lebenslauf-editor-ausbildungsart-select',
        LebenslaufAusbildungsArt.EIDGENOESSISCHES_BERUFSATTEST,
      );

      expect(
        screen.queryByTestId('lebenslauf-editor-berufsbezeichnung'),
      ).toBeInTheDocument();

      await user.type(
        screen.getByTestId('lebenslauf-editor-berufsbezeichnung'),
        value,
      );

      expect(
        screen.getByTestId('lebenslauf-editor-berufsbezeichnung'),
      ).toHaveValue(value);

      await clickMatSelectOption(
        'lebenslauf-editor-ausbildungsart-select',
        LebenslaufAusbildungsArt.FACHMATURITAET,
      );

      expect(
        screen.queryByTestId('lebenslauf-editor-berufsbezeichnung'),
      ).toBeNull();

      await clickMatSelectOption(
        'lebenslauf-editor-ausbildungsart-select',
        LebenslaufAusbildungsArt.EIDGENOESSISCHES_BERUFSATTEST,
      );

      expect(
        screen.queryByTestId('lebenslauf-editor-berufsbezeichnung'),
      ).toBeInTheDocument();

      expect(
        screen.getByTestId('lebenslauf-editor-berufsbezeichnung'),
      ).toHaveValue('');
    });

    it('should reset fachrichtung if field is hidden', async () => {
      await setup('AUSBILDUNG');

      const value = 'Meine Fachrichtung';

      expect(screen.queryByTestId('lebenslauf-editor-fachrichtung')).toBeNull();

      await clickMatSelectOption(
        'lebenslauf-editor-ausbildungsart-select',
        LebenslaufAusbildungsArt.BACHELOR_FACHHOCHSCHULE,
      );

      expect(
        screen.queryByTestId('lebenslauf-editor-fachrichtung'),
      ).toBeInTheDocument();

      await userEvent.type(
        screen.getByTestId('lebenslauf-editor-fachrichtung'),
        value,
      );

      expect(screen.getByTestId('lebenslauf-editor-fachrichtung')).toHaveValue(
        value,
      );

      await clickMatSelectOption(
        'lebenslauf-editor-ausbildungsart-select',
        LebenslaufAusbildungsArt.FACHMATURITAET,
      );

      expect(screen.queryByTestId('lebenslauf-editor-fachrichtung')).toBeNull();

      await clickMatSelectOption(
        'lebenslauf-editor-ausbildungsart-select',
        LebenslaufAusbildungsArt.BACHELOR_FACHHOCHSCHULE,
      );

      expect(
        screen.queryByTestId('lebenslauf-editor-fachrichtung'),
      ).toBeInTheDocument();

      expect(screen.getByTestId('lebenslauf-editor-fachrichtung')).toHaveValue(
        '',
      );
    });

    it('should reset titelDesAbschlusses if field is hidden', async () => {
      await setup('AUSBILDUNG');
      const value = 'Mein Beruf';

      expect(
        screen.queryByTestId('lebenslauf-editor-titel-des-abschlusses'),
      ).toBeNull();

      await clickMatSelectOption(
        'lebenslauf-editor-ausbildungsart-select',
        LebenslaufAusbildungsArt.ANDERER_BILDUNGSABSCHLUSS,
      );

      expect(
        screen.queryByTestId('lebenslauf-editor-titel-des-abschlusses'),
      ).toBeInTheDocument();

      await userEvent.type(
        screen.getByTestId('lebenslauf-editor-titel-des-abschlusses'),
        value,
      );

      expect(
        screen.getByTestId('lebenslauf-editor-titel-des-abschlusses'),
      ).toHaveValue(value);

      await clickMatSelectOption(
        'lebenslauf-editor-ausbildungsart-select',
        LebenslaufAusbildungsArt.FACHMATURITAET,
      );

      expect(
        screen.queryByTestId('lebenslauf-editor-titel-des-abschlusses'),
      ).toBeNull();

      await clickMatSelectOption(
        'lebenslauf-editor-ausbildungsart-select',
        LebenslaufAusbildungsArt.ANDERER_BILDUNGSABSCHLUSS,
      );

      expect(
        screen.queryByTestId('lebenslauf-editor-titel-des-abschlusses'),
      ).toBeInTheDocument();

      expect(
        screen.getByTestId('lebenslauf-editor-titel-des-abschlusses'),
      ).toHaveValue('');
    });

    // TODO: Fix this test
    it.skip('should display fachrichtung if one of {BACHELOR_HOCHSCHULE_UNI, BACHELOR_FACHHOCHSCHULE, MASTER} is selected', async () => {
      await setup('AUSBILDUNG');
      const bildungsartenWhichNeedFachrichtung = [
        LebenslaufAusbildungsArt.BACHELOR_HOCHSCHULE_UNI,
        LebenslaufAusbildungsArt.BACHELOR_FACHHOCHSCHULE,
        LebenslaufAusbildungsArt.MASTER,
      ];
      const bildungsartenWhichDontNeedFachrichtung = Object.values(
        LebenslaufAusbildungsArt,
      ).filter(
        (ausbildungsart) =>
          !bildungsartenWhichNeedFachrichtung.includes(ausbildungsart),
      );

      for (const bildungsart of bildungsartenWhichDontNeedFachrichtung) {
        await clickMatSelectOption(
          'lebenslauf-editor-ausbildungsart-select',
          bildungsart,
        );

        expect(
          screen.queryByTestId('lebenslauf-editor-fachrichtung'),
        ).toBeNull();
      }

      for (const bildungsart of bildungsartenWhichNeedFachrichtung) {
        await clickMatSelectOption(
          'lebenslauf-editor-ausbildungsart-select',
          bildungsart,
        );

        expect(
          screen.queryByTestId('lebenslauf-editor-fachrichtung'),
        ).toBeInTheDocument();
      }
    });

    // TODO: Fix this test
    it.skip('should display titelDesAbschlusses if ANDERER_BILDUNGSABSCHLUSS is selected', async () => {
      await setup('AUSBILDUNG');
      const bildungsartenWhichNeedTitelDesAbschlusses = [
        LebenslaufAusbildungsArt.ANDERER_BILDUNGSABSCHLUSS,
      ];
      const bildungsartenWhichDontNeedTitelDesAbschlusses = Object.values(
        LebenslaufAusbildungsArt,
      ).filter(
        (ausbildungsart) =>
          !bildungsartenWhichNeedTitelDesAbschlusses.includes(ausbildungsart),
      );

      for (const bildungsart of bildungsartenWhichDontNeedTitelDesAbschlusses) {
        await clickMatSelectOption(
          'lebenslauf-editor-ausbildungsart-select',
          bildungsart,
        );

        expect(
          screen.queryByTestId('lebenslauf-editor-titel-des-abschlusses'),
        ).toBeNull();
      }

      for (const bildungsart of bildungsartenWhichNeedTitelDesAbschlusses) {
        await clickMatSelectOption(
          'lebenslauf-editor-ausbildungsart-select',
          bildungsart,
        );

        expect(
          screen.queryByTestId('lebenslauf-editor-titel-des-abschlusses'),
        ).toBeInTheDocument();
      }
    });
  });

  describe('new TAETIGKEIT', () => {
    it('should not have ausbildungspezifisch fields', async () => {
      await setup('TAETIGKEIT');

      expect(
        screen.queryByTestId('lebenslauf-editor-ausbildungsart-select'),
      ).toBeNull();
      expect(screen.queryByTestId('lebenslauf-editor-fachrichtung')).toBeNull();
      expect(
        screen.queryByTestId('lebenslauf-editor-berufsbezeichnung'),
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
