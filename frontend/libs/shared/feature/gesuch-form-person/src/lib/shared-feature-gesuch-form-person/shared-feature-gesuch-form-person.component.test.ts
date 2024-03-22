import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { provideMockStore } from '@ngrx/store/testing';
import { render } from '@testing-library/angular';
import { userEvent } from '@testing-library/user-event';
import { TranslateTestingModule } from 'ngx-translate-testing';

import { GesuchFormular, PersonInAusbildung } from '@dv/shared/model/gesuch';
import { provideMaterialDefaultOptions } from '@dv/shared/pattern/angular-material-config';
import { provideSharedAppSettings } from '@dv/shared/pattern/app-settings';

import { SharedFeatureGesuchFormPersonComponent } from './shared-feature-gesuch-form-person.component';

async function setup() {
  return await render(SharedFeatureGesuchFormPersonComponent, {
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
            personInAusbildung: {} as PersonInAusbildung,
          } as GesuchFormular,
          gesuchs: {
            cache: {
              gesuchFormular: null,
            },
          },
        },
      }),
      provideSharedAppSettings('gesuch-app'),
      provideMaterialDefaultOptions(),
    ],
  });
}

describe(SharedFeatureGesuchFormPersonComponent.name, () => {
  it('should not display vermoegenVorjahr if no PLZ has been filled', async () => {
    const { queryByTestId, detectChanges } = await setup();

    detectChanges();
    expect(queryByTestId('form-person-vermoegenVorjahr')).toBeNull();
  });

  it('should not display vermoegenVorjahr if PLZ != Bern has been filled', async () => {
    const { queryByTestId, getByTestId } = await setup();

    await userEvent.type(getByTestId('form-address-plz'), '2000');
    expect(queryByTestId('form-person-vermoegenVorjahr')).toBeInTheDocument();
  });

  it('should display vermoegenVorjahr if PLZ = Bern has been filled', async () => {
    const { queryByTestId, getByTestId } = await setup();

    await userEvent.type(getByTestId('form-address-plz'), '3000');
    expect(queryByTestId('form-person-vermoegenVorjahr')).toBeNull();
  });
});
