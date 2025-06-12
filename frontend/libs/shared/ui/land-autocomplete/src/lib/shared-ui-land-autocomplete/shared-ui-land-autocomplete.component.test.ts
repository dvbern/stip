import { provideHttpClient } from '@angular/common/http';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { provideTranslateService } from '@ngx-translate/core';
import { render, screen } from '@testing-library/angular';
import { userEvent } from '@testing-library/user-event';

import { Language } from '@dv/shared/model/language';
import { provideLandLookupMock } from '@dv/shared/util-fn/comp-test';

import { SharedUiLandAutocompleteComponent } from './shared-ui-land-autocomplete.component';

async function setup(language: Language = 'de') {
  return await render(SharedUiLandAutocompleteComponent, {
    imports: [NoopAnimationsModule],
    providers: [
      provideHttpClient(),
      provideTranslateService(),
      provideLandLookupMock(),
    ],
    inputs: {
      languageSig: language,
    },
  });
}

describe(SharedUiLandAutocompleteComponent.name, () => {
  it('should set the correct value when a land is searched and selected', async () => {
    const { getByTestId } = await setup();
    const user = userEvent.setup();

    const autocompleteInput = getByTestId('land-autocomplete-input');
    expect(autocompleteInput).toBeInTheDocument();

    await user.type(autocompleteInput, 'Sch');

    const listbox = screen.getByRole('listbox');
    expect(listbox).toBeInTheDocument();

    const options = screen.getAllByRole('option');
    expect(options.length).toBe(2); // Should show Switzerland and Germany

    expect(options[0]).toHaveTextContent('Schweiz');

    await user.click(options[0]);
    expect(autocompleteInput).toHaveValue('Schweiz');
  });

  it('should display the correct land value depending on language', async () => {
    const { getByTestId } = await setup('fr');
    const user = userEvent.setup();

    const autocompleteInput = getByTestId('land-autocomplete-input');

    await user.type(autocompleteInput, 'Sui');
    const options = screen.getAllByRole('option');
    expect(options[0]).toHaveTextContent('Suisse');

    await user.click(options[0]);
    expect(autocompleteInput).toHaveValue('Suisse');
  });
});
