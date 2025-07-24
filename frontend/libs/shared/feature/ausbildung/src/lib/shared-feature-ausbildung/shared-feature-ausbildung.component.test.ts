import { provideHttpClient } from '@angular/common/http';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { provideMockStore } from '@ngrx/store/testing';
import { fireEvent, render } from '@testing-library/angular';
import { TranslateTestingModule } from 'ngx-translate-testing';
import { of } from 'rxjs';

import { AusbildungStore } from '@dv/shared/data-access/ausbildung';
import { AusbildungsstaetteStore } from '@dv/shared/data-access/ausbildungsstaette';
import { Ausbildung, AusbildungService } from '@dv/shared/model/gesuch';
import {
  provideSharedPatternJestTestAusbildungstaetten,
  provideSharedPatternJestTestSetup,
} from '@dv/shared/pattern/jest-test-setup';
import { provideMaterialDefaultOptions } from '@dv/shared/util/form';
import {
  checkMatCheckbox,
  clickFirstMatSelectOption,
  prepareEvent,
} from '@dv/shared/util-fn/comp-test';

import { SharedFeatureAusbildungComponent } from './shared-feature-ausbildung.component';

async function setup() {
  return await render(SharedFeatureAusbildungComponent, {
    inputs: {
      fallIdSig: null,
    },
    imports: [
      TranslateTestingModule.withTranslations({}),
      NoopAnimationsModule,
    ],
    providers: [
      provideHttpClient(),
      {
        provide: AusbildungService,
        useValue: {
          getAusbildung$: () => of({} as Ausbildung),
        },
      },
      provideMockStore({
        initialState: {
          gesuchs: {
            gesuch: null,
            gesuchFormular: {
              ausbildung: {
                editable: true,
              },
            },
            cache: {
              gesuch: null,
              gesuchId: null,
              gesuchFormular: {
                ausbildung: {
                  editable: true,
                },
              },
            },
          },
          language: { language: 'de' },
          configs: {},
        },
      }),
      provideMaterialDefaultOptions(),
      provideSharedPatternJestTestSetup({
        appType: 'sachbearbeitung-app',
        authClientId: 'stip-sachbearbeitung-app',
      }),
      provideSharedPatternJestTestAusbildungstaetten(),
      AusbildungsstaetteStore,
      AusbildungStore,
    ],
  });
}

describe(SharedFeatureAusbildungComponent.name, () => {
  describe('form validity', () => {
    beforeEach(() => {
      jest.useFakeTimers();
      jest.setSystemTime(new Date('2019-02-01'));
    });

    afterEach(() => {
      jest.runOnlyPendingTimers();
      jest.useRealTimers();
    });

    it('should be invalid if begin is not a date', async () => {
      const { getByTestId, detectChanges } = await setup();
      const input = getByTestId('form-education-beginn-der-ausbildung');
      await prepareEvent().type(input, 'gugus');
      fireEvent.blur(input);

      detectChanges();

      expect(input).toHaveClass('ng-invalid');
    });

    it('should be invalid if end is not a date', async () => {
      const { getByTestId, detectChanges } = await setup();
      const input = getByTestId('form-education-ende-der-ausbildung');
      await prepareEvent().type(input, 'gugus');
      fireEvent.blur(input);

      detectChanges();

      expect(input).toHaveClass('ng-invalid');
    });

    it('should be valid if a past date is provided for begin', async () => {
      const { getByTestId, detectChanges } = await setup();
      const input = getByTestId('form-education-beginn-der-ausbildung');
      await prepareEvent().type(input, '01.2018');
      fireEvent.blur(input);

      detectChanges();

      expect(input).not.toHaveClass('ng-invalid');
    });

    it('should be valid if the begin date is before the end date', async () => {
      const { getByTestId, detectChanges } = await setup();
      const beginInput = getByTestId('form-education-beginn-der-ausbildung');
      const endInput = getByTestId('form-education-ende-der-ausbildung');
      await prepareEvent().type(beginInput, '01.2019');
      fireEvent.blur(beginInput);
      await prepareEvent().type(endInput, '01.2020');
      fireEvent.blur(endInput);
      detectChanges();

      expect(beginInput).not.toHaveClass('ng-invalid');
    });

    it('should be invalid if the begin date is after the end date', async () => {
      const { getByTestId, detectChanges } = await setup();
      const beginInput = getByTestId('form-education-beginn-der-ausbildung');
      const endInput = getByTestId('form-education-ende-der-ausbildung');
      await prepareEvent().type(beginInput, '01.2020');
      fireEvent.blur(beginInput);
      await prepareEvent().type(endInput, '01.2019');
      fireEvent.blur(endInput);

      detectChanges();

      expect(beginInput).not.toHaveClass('ng-invalid');
      expect(endInput).toHaveClass('ng-invalid');
      expect(
        getByTestId('form-education-form').querySelector('mat-error'),
      ).toHaveTextContent('shared.form.ausbildung.form.error.monthYearAfter');
    });

    (
      [
        ['before', '01.2019', true],
        ['equal', '02.2019', true],
        ['after', '03.2019', false],
      ] as const
    ).forEach(([position, endDate, shouldBeInvalid]) => {
      it(`should be invalid=${shouldBeInvalid} if end date is ${position} the current month`, async () => {
        const { getByTestId, detectChanges } = await setup();
        const input = getByTestId('form-education-ende-der-ausbildung');
        await prepareEvent().type(input, endDate);
        fireEvent.blur(input);

        detectChanges();

        (shouldBeInvalid ? expect(input) : expect(input).not).toHaveClass(
          `ng-invalid`,
        );
      });
    });

    it('should have disabled inputs depending on each previous input state', async () => {
      const fields = {
        notFound: 'form-education-ausbildungNichtGefunden',
        staette: 'form-education-ausbildungsstaette',
        gang: 'form-education-ausbildungsgang',
        alternativ: {
          staette: 'form-education-alternativeAusbildungsstaette',
          gang: 'form-education-alternativeAusbildungsgang',
        },
        fachrichtung: 'form-education-fachrichtung',
      };
      const { getByTestId, detectChanges } = await setup();

      detectChanges();

      await clickFirstMatSelectOption(fields.staette);
      detectChanges();

      expect(getByTestId(fields.gang)).not.toHaveClass(
        'mat-mdc-select-disabled',
      );

      await clickFirstMatSelectOption(fields.gang);
      detectChanges();

      expect(getByTestId(fields.fachrichtung)).toBeEnabled();
      expect(getByTestId(fields.fachrichtung)).not.toHaveClass(
        'mat-mdc-select-disabled',
      );

      await prepareEvent().type(
        getByTestId(fields.fachrichtung),
        'fachrichtung1',
      );
      detectChanges();

      expect(getByTestId(fields.fachrichtung)).toHaveValue('fachrichtung1');

      await checkMatCheckbox(fields.notFound);
      detectChanges();

      for (const field of [fields.alternativ.staette, fields.alternativ.gang]) {
        const fieldEl = getByTestId(field);

        expect(fieldEl).toHaveValue('');
        expect(fieldEl).toBeEnabled();

        await prepareEvent().click(fieldEl);
        fireEvent.blur(fieldEl);

        detectChanges();

        expect(fieldEl).toHaveClass('ng-invalid');
      }
    });

    it('should disable ausbildungsort if isAusbildungAusland is checked', async () => {
      const { getByTestId, detectChanges } = await setup();

      detectChanges();

      expect(getByTestId('form-education-ausbildungsort')).not.toBeDisabled();

      await checkMatCheckbox('form-education-isAusbildungAusland');

      detectChanges();

      expect(getByTestId('form-education-ausbildungsort')).toBeDisabled();
    });
  });
});
