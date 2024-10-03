import { signal } from '@angular/core';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { provideMockStore } from '@ngrx/store/testing';
import { render } from '@testing-library/angular';
import { TranslateTestingModule } from 'ngx-translate-testing';

import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import { AbschlussPhase } from '@dv/shared/model/einreichen';
import { provideMaterialDefaultOptions } from '@dv/shared/util/form';

import { GesuchAppFeatureGesuchFormAbschlussComponent } from './gesuch-app-feature-gesuch-form-abschluss.component';

async function setup(abschlussPhase: AbschlussPhase) {
  return await render(GesuchAppFeatureGesuchFormAbschlussComponent, {
    imports: [
      TranslateTestingModule.withTranslations({}),
      NoopAnimationsModule,
    ],
    providers: [
      {
        provide: EinreichenStore,
        useValue: {
          einreichenViewSig: signal<
            ReturnType<EinreichenStore['einreichenViewSig']>
          >({
            loading: false,
            abschlussPhase,
            gesuch: {
              id: '1',
            } as any,
            canCheck: true,
            isEditingTranche: false,
            lastUpdate: null,
            specialValidationErrors: [],
            trancheId: '1',
          }),
        },
      },
      provideMockStore(),
      provideMaterialDefaultOptions(),
    ],
  });
}

describe(GesuchAppFeatureGesuchFormAbschlussComponent.name, () => {
  it('should render a warning alert if the gesuch is not ready', async () => {
    const { getByTestId, detectChanges } = await setup('NOT_READY');

    detectChanges();

    expect(getByTestId('alert-warning')).toBeInTheDocument();
  });

  it('should render a info alert if the gesuch is ready to send', async () => {
    const { getByTestId, detectChanges } = await setup('READY_TO_SEND');

    detectChanges();

    expect(getByTestId('alert-info')).toBeInTheDocument();
  });

  it('should render a success alert if the gesuch was submitted', async () => {
    const { getByTestId, detectChanges } = await setup('SUBMITTED');

    detectChanges();

    expect(getByTestId('alert-success')).toBeInTheDocument();
  });
});
