import { provideHttpClient } from '@angular/common/http';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { render, screen } from '@testing-library/angular';
import { userEvent } from '@testing-library/user-event';
import { TranslateTestingModule } from 'ngx-translate-testing';

import { Language } from '@dv/shared/model/language';
// eslint-disable-next-line @nx/enforce-module-boundaries
import { configureTestbedTranslateLanguage } from '@dv/shared/pattern/vitest-test-setup';

import { SharedUiSelectSearchComponent } from './shared-ui-select-search.component';

type TestType = {
  id: string;
  testId: string;
  displayValueDe: string;
  displayValueFr: string;
};

const values: TestType[] = [
  {
    id: 'uuid1',
    testId: 'test1',
    displayValueDe: 'Wert 1',
    displayValueFr: 'Valeur 1',
  },
  {
    id: 'uuid2',
    testId: 'test2',
    displayValueDe: 'Ein anderer Wert 2',
    displayValueFr: 'Une autre valeur 2',
  },
  {
    id: 'uuid3',
    testId: 'test3',
    displayValueDe: 'Noch ein Wert 3',
    displayValueFr: 'Encore une valeur 3',
  },
];

async function setup(language: Language = 'de') {
  const result = await render(SharedUiSelectSearchComponent, {
    imports: [
      NoopAnimationsModule,
      TranslateTestingModule.withTranslations({ de: {}, fr: {} }),
    ],
    providers: [provideHttpClient()],
    inputs: {
      labelKeySig: 'label.test.key',
      valuesSig: values,
      isEntryValidSig: () => true,
      invalidValueLabelKeySig: 'error.test.key',
      languageSig: language,
      testIdSig: 'autocomplete-test-id',
    },
    configureTestBed: configureTestbedTranslateLanguage(language),
  });

  return result;
}

describe('SharedUiSelectSearchComponent Component Test', () => {
  it('should set the correct value when a value is searched and selected', async () => {
    const { getByTestId } = await setup();
    const user = userEvent.setup();

    const selectSearch = getByTestId('autocomplete-test-id');
    expect(selectSearch).toBeInTheDocument();
    selectSearch.click();

    const input = (
      await screen.findByRole('listbox')
    ).querySelector<HTMLInputElement>(
      '.mat-select-search-input:not(.mat-select-search-hidden )',
    );
    expect(input).toBeInTheDocument();

    await user.type(input!, 'Ein anderer Wert');

    const options = screen.getAllByRole('option');
    expect(options.length).toBe(1); // Should show values[1] only

    expect(options[0]).toHaveTextContent(values[1].displayValueDe);

    await user.click(options[0]);
    screen.debug();
    expect(
      selectSearch.querySelector('.mat-mdc-select-min-line'),
    ).toHaveTextContent(values[1].displayValueDe);
  });

  it('should display the correct land value depending on language', async () => {
    const { getByTestId } = await setup('fr');
    const user = userEvent.setup();

    const selectSearch = getByTestId('autocomplete-test-id');
    expect(selectSearch).toBeInTheDocument();
    selectSearch.click();

    const input = (
      await screen.findByRole('listbox')
    ).querySelector<HTMLInputElement>(
      '.mat-select-search-input:not(.mat-select-search-hidden )',
    );
    expect(input).toBeInTheDocument();

    await user.type(input!, 'Encor');

    const options = screen.getAllByRole('option');
    expect(options[0]).toHaveTextContent(values[2].displayValueFr);

    await user.click(options[0]);
    expect(
      selectSearch.querySelector('.mat-mdc-select-min-line'),
    ).toHaveTextContent(values[2].displayValueFr);
  });
});
