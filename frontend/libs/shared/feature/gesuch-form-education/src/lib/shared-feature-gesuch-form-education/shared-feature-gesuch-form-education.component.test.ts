import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { provideMockStore } from '@ngrx/store/testing';
import { fireEvent, render } from '@testing-library/angular';
import { TranslateTestingModule } from 'ngx-translate-testing';

import { Ausbildungsstaette } from '@dv/shared/model/gesuch';
import { provideMaterialDefaultOptions } from '@dv/shared/pattern/angular-material-config';
import {
  checkMatCheckbox,
  clickFirstMatSelectOption,
  prepareEvent,
} from '@dv/shared/util-fn/comp-test';

import { SharedFeatureGesuchFormEducationComponent } from './shared-feature-gesuch-form-education.component';

async function setup() {
  return await render(SharedFeatureGesuchFormEducationComponent, {
    imports: [
      TranslateTestingModule.withTranslations({}),
      NoopAnimationsModule,
    ],
    providers: [
      provideMockStore({
        initialState: {
          ausbildungsstaettes: {
            ausbildungsstaettes: <Ausbildungsstaette[]>[
              {
                nameDe: 'staette1',
                nameFr: 'staette1',
                id: '1',
                ausbildungsgaenge: [
                  {
                    ausbildungsort: 'BERN',
                    ausbildungsrichtung: 'FACHHOCHSCHULEN',
                    bezeichnungDe: 'gang1',
                    bezeichnungFr: 'gang1',
                    ausbildungsstaetteId: '1',
                    id: '1',
                  },
                ],
              },
            ],
          },
          gesuchs: {
            gesuchFormular: {},
          },
          language: { language: 'de' },
        },
      }),
      provideMaterialDefaultOptions(),
    ],
  });
}

describe(SharedFeatureGesuchFormEducationComponent.name, () => {
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
      const { getByTestId } = await setup();
      const input = getByTestId('form-education-beginn-der-ausbildung');
      await prepareEvent().type(input, 'gugus');
      fireEvent.blur(input);

      expect(input).toHaveClass('ng-invalid');
    });

    it('should be invalid if end is not a date', async () => {
      const { getByTestId } = await setup();
      const input = getByTestId('form-education-ende-der-ausbildung');
      await prepareEvent().type(input, 'gugus');
      fireEvent.blur(input);

      expect(input).toHaveClass('ng-invalid');
    });

    it('should be valid if a past date is provided for begin', async () => {
      const { getByTestId } = await setup();
      const input = getByTestId('form-education-beginn-der-ausbildung');
      await prepareEvent().type(input, '01.2018');
      fireEvent.blur(input);

      expect(input).not.toHaveClass('ng-invalid');
    });

    it('should be valid if the begin date is before the end date', async () => {
      const { getByTestId } = await setup();
      const beginInput = getByTestId('form-education-beginn-der-ausbildung');
      const endInput = getByTestId('form-education-ende-der-ausbildung');
      await prepareEvent().type(beginInput, '01.2019');
      fireEvent.blur(beginInput);
      await prepareEvent().type(endInput, '01.2020');
      fireEvent.blur(endInput);

      expect(beginInput).not.toHaveClass('ng-invalid');
    });

    it('should be invalid if the begin date is after the end date', async () => {
      const { getByTestId } = await setup();
      const beginInput = getByTestId('form-education-beginn-der-ausbildung');
      const endInput = getByTestId('form-education-ende-der-ausbildung');
      await prepareEvent().type(beginInput, '01.2020');
      fireEvent.blur(beginInput);
      await prepareEvent().type(endInput, '01.2019');
      fireEvent.blur(endInput);

      expect(beginInput).not.toHaveClass('ng-invalid');
      expect(endInput).toHaveClass('ng-invalid');
      expect(getByTestId('form-education-form')).toHaveClass('ng-invalid');
      expect(
        getByTestId('form-education-form').querySelector('mat-error'),
      ).toHaveTextContent('shared.form.education.form.error.monthYearAfter');
    });

    (
      [
        ['before', '01.2019', 'invalid'],
        ['equal', '02.2019', 'invalid'],
        ['after', '03.2019', 'valid'],
      ] as const
    ).forEach(([position, endDate, expected]) => {
      it(`should be ${expected} if end date is ${position} the current month`, async () => {
        const { getByTestId } = await setup();
        const input = getByTestId('form-education-ende-der-ausbildung');
        await prepareEvent().type(input, endDate);
        fireEvent.blur(input);

        expect(input).toHaveClass(`ng-${expected}`);
      });
    });

    it('should have disabled inputs depending on each previous input state', async () => {
      const fields = {
        notFound: 'form-education-ausbildungNichtGefunden',
        land: 'form-education-ausbildungsland',
        staette: 'form-education-ausbildungsstaette',
        gang: 'form-education-ausbildungsgang',
        alternativ: {
          land: 'form-education-alternativeAusbildungsland',
          staette: 'form-education-alternativeAusbildungsstaette',
          gang: 'form-education-alternativeAusbildungsgang',
        },
        fachrichtung: 'form-education-fachrichtung',
      };
      const { getByTestId } = await setup();

      expect(getByTestId(fields.staette)).toBeDisabled();

      await clickFirstMatSelectOption(fields.land);
      expect(getByTestId(fields.gang)).toHaveClass('mat-mdc-select-disabled');

      await clickFirstMatSelectOption(fields.staette);
      expect(getByTestId(fields.gang)).not.toHaveClass(
        'mat-mdc-select-disabled',
      );

      await clickFirstMatSelectOption(fields.gang);
      expect(getByTestId(fields.fachrichtung)).toBeEnabled();
      expect(getByTestId(fields.fachrichtung)).not.toHaveClass(
        'mat-mdc-select-disabled',
      );

      await prepareEvent().type(
        getByTestId(fields.fachrichtung),
        'fachrichtung1',
      );
      expect(getByTestId(fields.fachrichtung)).toHaveValue('fachrichtung1');

      await checkMatCheckbox(fields.notFound);

      for (const field of [
        fields.alternativ.land,
        fields.alternativ.staette,
        fields.alternativ.gang,
        fields.fachrichtung,
      ]) {
        const fieldEl = getByTestId(field);

        expect(fieldEl).toHaveValue('');
        expect(fieldEl).toBeEnabled();

        await prepareEvent().click(fieldEl);
        fireEvent.blur(fieldEl);

        expect(fieldEl).toHaveClass('ng-invalid');
      }
    });
  });
});
