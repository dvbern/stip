import { provideHttpClient } from '@angular/common/http';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { provideMockStore } from '@ngrx/store/testing';
import { fireEvent, render, screen, waitFor } from '@testing-library/angular';
import { userEvent } from '@testing-library/user-event';
import { TranslateTestingModule } from 'ngx-translate-testing';

import { GesuchFormular, PartnerUpdate } from '@dv/shared/model/gesuch';
import { provideMaterialDefaultOptions } from '@dv/shared/pattern/angular-material-config';
import { checkMatCheckbox } from '@dv/shared/util-fn/comp-test';

import { SharedFeatureGesuchFormPartnerComponent } from './shared-feature-gesuch-form-partner.component';

async function setup() {
  return await render(SharedFeatureGesuchFormPartnerComponent, {
    imports: [
      TranslateTestingModule.withTranslations({ de: {} }),
      NoopAnimationsModule,
    ],
    providers: [
      provideMockStore({
        initialState: {
          language: {
            language: 'de',
          },
          gesuch: null,
          gesuchFormular: {
            partner: {} as PartnerUpdate,
          } as GesuchFormular,
          gesuchs: {
            cache: {
              gesuchId: null,
              gesuchFormular: null,
            },
          },
          loading: false,
          error: undefined,
          stammdatens: {
            laender: [],
          },
        },
      }),
      provideMaterialDefaultOptions(),
      provideHttpClient(),
    ],
  });
}

describe(SharedFeatureGesuchFormPartnerComponent.name, () => {
  it('should not display jahreseinkommen, verpflegunskosten and fahrkosten if "ausbildungMitEinkommenOderErwerbstaetig" is not selected', async () => {
    await setup();

    expect(screen.queryByTestId('form-partner-fahrkosten')).toBeNull();
    expect(screen.queryByTestId('form-partner-jahreseinkommen')).toBeNull();
    expect(screen.queryByTestId('form-partner-verpflegungskosten')).toBeNull();
  });

  it('should display jahreseinkommen, verpflegunskosten and fahrkosten if "ausbildungMitEinkommenOderErwerbstaetig" is selected', async () => {
    const { detectChanges } = await setup();

    await checkMatCheckbox(
      'form-partner-ausbildungMitEinkommenOderErwerbstaetig',
    );

    detectChanges();

    expect(screen.queryByTestId('form-partner-fahrkosten')).toBeInTheDocument();
    expect(
      screen.queryByTestId('form-partner-jahreseinkommen'),
    ).toBeInTheDocument();
    expect(
      screen.queryByTestId('form-partner-verpflegungskosten'),
    ).toBeInTheDocument();
  });

  it('should allow to save the form when all required fields are filled in if "ausbildungMitEinkommenOderErwerbstaetig" is not selected', async () => {
    const { getByTestId, detectChanges } = await setup();
    const user = await fillBasicForm();

    await user.click(getByTestId('button-save-continue'));

    detectChanges();

    expect(getByTestId('form-partner-form').classList.contains('ng-valid'));
  });

  it('should allow to save the form when all required fields are filled in if "ausbildungMitEinkommenOderErwerbstaetig" is selected', async () => {
    const { getByTestId, container, detectChanges } = await setup();
    const user = await fillBasicForm();
    await checkMatCheckbox(
      'form-partner-ausbildungMitEinkommenOderErwerbstaetig',
    );

    detectChanges();

    expect(screen.queryByTestId('form-partner-fahrkosten')).toBeInTheDocument();

    await user.type(getByTestId('form-partner-fahrkosten'), '3000');
    await user.type(getByTestId('form-partner-jahreseinkommen'), '4000');
    await user.type(getByTestId('form-partner-verpflegungskosten'), '5000');

    detectChanges();

    // discuss order with team
    expect(getByTestId('form-partner-form').classList.contains('ng-valid'));

    await user.click(getByTestId('button-save-continue'));

    detectChanges();

    expect(container.querySelector('mat-error')).not.toBeInTheDocument();
  });

  it('should reset jahreseinkommen, verpflegunskosten and fahrkosten if "ausbildungMitEinkommenOderErwerbstaetig" is selected, unselected, and selected again', async () => {
    const { getByTestId, detectChanges } = await setup();
    const checkbox = await checkMatCheckbox(
      'form-partner-ausbildungMitEinkommenOderErwerbstaetig',
    );

    detectChanges();

    await userEvent.type(getByTestId('form-partner-fahrkosten'), '3000');
    await userEvent.type(getByTestId('form-partner-jahreseinkommen'), '4000');
    await userEvent.type(
      getByTestId('form-partner-verpflegungskosten'),
      '5000',
    );

    await userEvent.click(checkbox);

    detectChanges();

    expect(screen.queryByTestId('form-partner-fahrkosten')).toBeNull();
    expect(screen.queryByTestId('form-partner-jahreseinkommen')).toBeNull();
    expect(screen.queryByTestId('form-partner-verpflegungskosten')).toBeNull();

    await userEvent.click(checkbox);

    detectChanges();

    expect(getByTestId('form-partner-fahrkosten')).toHaveValue('');
    expect(getByTestId('form-partner-jahreseinkommen')).toHaveValue('');
    expect(getByTestId('form-partner-verpflegungskosten')).toHaveValue('');
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

  // put into utility function
  screen.getByTestId('form-address-land').click();
  await waitFor(() =>
    expect(screen.queryByRole('listbox')).toBeInTheDocument(),
  );
  screen.getByTestId('CH').click();

  fireEvent.input(screen.getByTestId('form-partner-geburtsdatum'), {
    target: { value: '01.01.1990' },
  });

  return user;
};
