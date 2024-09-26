import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { provideMockStore } from '@ngrx/store/testing';
import { render } from '@testing-library/angular';
import { TranslateTestingModule } from 'ngx-translate-testing';

import { selectGesuchAppDataAccessAbschlussView } from '@dv/shared/data-access/abschluss';
import { AbschlussPhase } from '@dv/shared/model/gesuch-abschluss';
import { provideMaterialDefaultOptions } from '@dv/shared/util/form';

import { SharedFeatureGesuchFormAbschlussComponent } from './shared-feature-gesuch-form-abschluss.component';

const abschlussPhase: Record<AbschlussPhase, string> = {
  NOT_READY: 'NOT_READY',
  READY_TO_SEND: 'READY_TO_SEND',
  SUBMITTED: 'SUBMITTED',
  ABGELEHNT: 'ABGELEHNT',
  AKZETPIERT: 'AKZETPIERT',
};

async function setup(abschlussPhase: AbschlussPhase) {
  return await render(SharedFeatureGesuchFormAbschlussComponent, {
    imports: [
      TranslateTestingModule.withTranslations({}),
      NoopAnimationsModule,
    ],
    providers: [
      provideMockStore({
        selectors: [
          {
            selector: selectGesuchAppDataAccessAbschlussView,
            value: {
              abschlussPhase,
              gesuch: {
                id: '1',
              },
              validations: [],
              specialValidationErrors: [],
              canCheck: true,
            },
          },
        ],
      }),
      provideMaterialDefaultOptions(),
    ],
  });
}

describe(SharedFeatureGesuchFormAbschlussComponent.name, () => {
  it('should render a warning alert if the gesuch is not ready', async () => {
    const { getByTestId, detectChanges } = await setup(
      abschlussPhase.NOT_READY as AbschlussPhase,
    );

    detectChanges();

    expect(getByTestId('alert-warning')).toBeInTheDocument();
  });

  it('should render a info alert if the gesuch is ready to send', async () => {
    const { getByTestId, detectChanges } = await setup(
      abschlussPhase.READY_TO_SEND as AbschlussPhase,
    );

    detectChanges();

    expect(getByTestId('alert-info')).toBeInTheDocument();
  });

  it('should render a success alert if the gesuch was submitted', async () => {
    const { getByTestId, detectChanges } = await setup(
      abschlussPhase.SUBMITTED as AbschlussPhase,
    );

    detectChanges();

    expect(getByTestId('alert-success')).toBeInTheDocument();
  });
});
