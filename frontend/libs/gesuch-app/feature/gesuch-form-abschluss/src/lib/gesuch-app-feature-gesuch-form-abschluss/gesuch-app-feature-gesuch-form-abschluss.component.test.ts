import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { provideMockStore } from '@ngrx/store/testing';
import { render } from '@testing-library/angular';
import { TranslateTestingModule } from 'ngx-translate-testing';

import { Gesuchstatus } from '@dv/shared/model/gesuch';
import { provideMaterialDefaultOptions } from '@dv/shared/pattern/angular-material-config';

import { GesuchAppFeatureGesuchFormAbschlussComponent } from './gesuch-app-feature-gesuch-form-abschluss.component';

// const abschlussPhase: Record<AbschlussPhase, string> = {
//   NOT_READY: 'NOT_READY',
//   READY_TO_SEND: 'READY_TO_SEND',
//   SUBMITTED: 'SUBMITTED',
// };

async function setup() {
  return await render(GesuchAppFeatureGesuchFormAbschlussComponent, {
    imports: [
      TranslateTestingModule.withTranslations({}),
      NoopAnimationsModule,
    ],
    providers: [
      provideMockStore({
        initialState: {
          abschluss: {
            checkResult: {
              success: false,
              error: {
                type: 'validationError',
                validationErrors: [
                  {
                    messageTemplate: 'test',
                    message: 'test',
                    propertyPath: 'test',
                  },
                ],
                validationWarnings: [
                  {
                    messageTemplate: 'test',
                    message: 'test',
                    propertyPath: 'test',
                  },
                ],
              },
            },
          },
          gesuchs: {
            validations: {
              errors: [],
              warnings: [
                {
                  messageTemplate: 'test',
                  message: 'test',
                  propertyPath: 'test',
                },
              ],
            },
            gesuch: {
              id: '1',
            },
            gesuchStatus: Gesuchstatus.IN_BEARBEITUNG_GS,
          },
        },
      }),
      provideMaterialDefaultOptions(),
    ],
  });
}

describe(GesuchAppFeatureGesuchFormAbschlussComponent.name, () => {
  it('should render a success alert if no validaton Errors are present', async () => {
    const { getByTestId, detectChanges } = await setup();

    detectChanges();

    expect(getByTestId('alert-success')).toBeInTheDocument();
  });
});
