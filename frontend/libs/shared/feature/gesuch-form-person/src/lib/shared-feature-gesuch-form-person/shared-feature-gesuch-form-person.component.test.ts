import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MockStore, provideMockStore } from '@ngrx/store/testing';
import { RenderResult, render, within } from '@testing-library/angular';
import { TranslateTestingModule } from 'ngx-translate-testing';

import { GesuchFormular, PersonInAusbildung } from '@dv/shared/model/gesuch';
import { provideSharedAppSettings } from '@dv/shared/pattern/app-settings';
import {
  mockedGesuchAppWritableGesuchState,
  provideCompileTimeConfig,
} from '@dv/shared/pattern/jest-test-setup';
import { provideMaterialDefaultOptions } from '@dv/shared/util/form';

import { SharedFeatureGesuchFormPersonComponent } from './shared-feature-gesuch-form-person.component';
import { selectSharedFeatureGesuchFormPersonView } from './shared-feature-gesuch-form-person.selector';

const formularMock = {
  ausbildung: {
    ausbildungBegin: '01.' + new Date().getFullYear(),
  },
};

async function setup() {
  return await render(SharedFeatureGesuchFormPersonComponent, {
    imports: [
      TranslateTestingModule.withTranslations({ de: {} }),
      NoopAnimationsModule,
    ],
    providers: [
      provideHttpClient(),
      provideCompileTimeConfig(),
      provideMockStore({
        initialState: {
          language: {
            language: 'de',
          },
          gesuchs: mockedGesuchAppWritableGesuchState({
            formular: formularMock,
          }),
          configs: {},
        },
      }),
      provideSharedAppSettings('gesuch-app'),
      provideMaterialDefaultOptions(),
    ],
  });
}

describe(SharedFeatureGesuchFormPersonComponent.name, () => {
  let store: MockStore;
  let c: RenderResult<SharedFeatureGesuchFormPersonComponent>;

  beforeEach(async () => {
    c = await setup();

    store = TestBed.inject(MockStore);
  });

  describe('form field states', () => {
    it('should display sozialversicherungsnummer input', async () => {
      c.detectChanges();
      expect(
        c.queryByTestId('form-person-sozialversicherungsnummer'),
      ).toBeInTheDocument();
    });
  });

  describe('it should disable the form and all controls within it', () => {
    const textInputs = [
      'form-person-sozialversicherungsnummer',
      'form-person-nachname',
      'form-person-vorname',
      // 'form-person-identischer-zivilrechlicher-wohnsitz-plz',
      // 'form-person-identischer-zivilrechlicher-wohnsitz-ort',
      'form-person-email',
      'form-person-telefonnummer',
      'form-person-geburtsdatum',
      'form-person-heimatort',
      // 'form-person-einreisedatum',
      'form-address-plz',
      'form-address-ort',
      'form-address-strasse',
      'form-address-hausnummer',
      'form-address-coAdresse',
    ];

    const matSelects = [
      'form-person-anrede',
      'form-person-zivilstand',
      'form-person-wohnsitz',
      // 'form-person-niederlassungsstatus',
      // 'form-person-zustaendigerKanton',
    ];

    const matCheckbox = [
      // 'form-person-digitaleKommunikation',
      'form-person-identischerZivilrechtlicherWohnsitz',
      // 'form-person-vormundschaft', // is not visible if not checked
    ];

    const radioGroups = [
      'form-person-sozialhilfeBeitraege',
      'form-person-korrespondenzSprache',
    ];

    beforeAll(() => {
      store.overrideSelector(selectSharedFeatureGesuchFormPersonView, {
        loading: false,
        gesuchId: '1',
        trancheId: '1',
        allowTypes: '',
        gesuch: null,
        gesuchFormular: {
          ...formularMock,
          personInAusbildung: {
            adresse: {},
          } as PersonInAusbildung,
        } as GesuchFormular,
        permissions: {},
        formChanges: null,
        benutzerEinstellungen: {
          digitaleKommunikation: undefined,
        },
        laender: [],
        readonly: true,
      });

      store.refreshState();

      c.detectChanges();
    });

    afterAll(() => {
      store.resetSelectors();
    });

    it('should disable the form if readonly is true', async () => {
      expect(c.getByTestId('form-person-form')).toHaveClass('readonly');
    });

    it('should disable all text input fields', async () => {
      textInputs.forEach((field) => {
        expect(c.getByTestId(field)).toBeDisabled();
      });
    });

    it('should disable all mat-select fields', async () => {
      matSelects.forEach((field) => {
        expect(c.getByTestId(field)).toHaveClass('mat-mdc-select-disabled');
      });
    });

    // could also get the input by role and check if it is disabled
    it('should disable all mat-checkbox fields', async () => {
      matCheckbox.forEach((field) => {
        expect(c.getByTestId(field)).toHaveClass('mdc-checkbox--disabled');
      });
    });

    it('should disable all radio groups', async () => {
      radioGroups.forEach((field) => {
        const radioGroup = c.getByTestId(field);
        const radioInputs = within(radioGroup).getAllByRole('radio');

        radioInputs.forEach((radio) => {
          expect(radio).toBeDisabled();
        });
      });
    });
  });
});
