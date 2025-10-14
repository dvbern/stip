import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { provideMockStore } from '@ngrx/store/testing';
import { fireEvent, render, screen } from '@testing-library/angular';
import { userEvent } from '@testing-library/user-event';

import { RolesMap } from '@dv/shared/model/benutzer';
import {
  configureTestbedTranslateLanguage,
  getTranslocoModule,
  mockConfigsState,
  mockedGesuchAppWritableGesuchState,
  provideCompileTimeConfig,
  provideSharedOAuthServiceWithGesuchstellerJWT,
} from '@dv/shared/pattern/vitest-test-setup';
import { provideMaterialDefaultOptions } from '@dv/shared/util/form';
import {
  checkMatCheckbox,
  clickMatSelectOption,
  provideLandLookupMock,
} from '@dv/shared/util-fn/comp-test';

import { SharedFeatureGesuchFormPartnerComponent } from './shared-feature-gesuch-form-partner.component';

const language = 'de';
async function setup() {
  return await render(SharedFeatureGesuchFormPartnerComponent, {
    imports: [getTranslocoModule(), NoopAnimationsModule],
    providers: [
      provideMockStore({
        initialState: {
          benutzers: {
            rolesMap: {
              V0_Gesuchsteller: true,
            } satisfies RolesMap,
          },
          language: {
            language,
          },
          gesuchs: mockedGesuchAppWritableGesuchState({
            formular: {
              partner: {
                adresse: {},
                geburtsdatum: '1990-01-01',
              },
            },
          }),
          configs: mockConfigsState(),
        },
      }),
      provideSharedOAuthServiceWithGesuchstellerJWT(),
      provideMaterialDefaultOptions(),
      provideLandLookupMock(),
      provideHttpClient(),
      provideHttpClientTesting(),
      provideCompileTimeConfig(),
    ],
    configureTestBed: configureTestbedTranslateLanguage(language),
  });
}

describe(SharedFeatureGesuchFormPartnerComponent.name, () => {
  it('should not display ausbildungspensum field when inAusbildung is not checked', async () => {
    await setup();

    expect(screen.queryByTestId('form-partner-ausbildungspensum')).toBeNull();
  });

  it('should display ausbildungspensum field when inAusbildung is checked', async () => {
    const { detectChanges } = await setup();

    await checkMatCheckbox('form-partner-inAusbildung');

    detectChanges();

    expect(
      screen.queryByTestId('form-partner-ausbildungspensum'),
    ).toBeInTheDocument();
  });

  it('should hide ausbildungspensum field when inAusbildung is unchecked after being checked', async () => {
    const { detectChanges } = await setup();

    const checkbox = await checkMatCheckbox('form-partner-inAusbildung');

    detectChanges();

    expect(
      screen.queryByTestId('form-partner-ausbildungspensum'),
    ).toBeInTheDocument();

    await userEvent.click(checkbox);

    detectChanges();

    expect(screen.queryByTestId('form-partner-ausbildungspensum')).toBeNull();
  });

  it('should allow to save the form when all required fields are filled in without inAusbildung selected', async () => {
    const { getByTestId, detectChanges } = await setup();
    const user = await fillBasicForm();

    await user.click(getByTestId('button-save-continue'));

    detectChanges();

    expect(getByTestId('form-partner-form').classList.contains('ng-valid'));
  });

  it('should allow to save the form when inAusbildung is checked and ausbildungspensum is selected', async () => {
    const { getByTestId, detectChanges } = await setup();
    const user = await fillBasicForm();

    await checkMatCheckbox('form-partner-inAusbildung');

    detectChanges();

    expect(
      screen.queryByTestId('form-partner-ausbildungspensum'),
    ).toBeInTheDocument();

    await clickMatSelectOption('form-partner-ausbildungspensum', 'VOLLZEIT');

    detectChanges();

    await user.click(getByTestId('button-save-continue'));

    detectChanges();

    expect(getByTestId('form-partner-form').classList.contains('ng-valid'));
  });
});

const fillBasicForm = async () => {
  const user = userEvent.setup();

  fireEvent.input(
    screen.getByTestId('form-partner-sozialversicherungsnummer'),
    {
      target: { value: '756.9217.0769.85' },
    },
  );
  await user.type(screen.getByTestId('form-partner-nachname'), 'Test N');
  await user.type(screen.getByTestId('form-partner-vorname'), 'Test V');
  await user.type(screen.getByTestId('form-address-strasse'), 'Test street');
  await user.type(screen.getByTestId('form-address-plz'), '3000');
  await user.type(screen.getByTestId('form-address-ort'), 'Bern');

  await clickMatSelectOption('form-address-land', 'Schweiz');

  fireEvent.input(screen.getByTestId('form-partner-geburtsdatum'), {
    target: { value: '01.01.1990' },
  });

  return user;
};
