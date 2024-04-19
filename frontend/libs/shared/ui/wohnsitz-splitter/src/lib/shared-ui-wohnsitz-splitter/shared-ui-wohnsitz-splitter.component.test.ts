import { FormBuilder } from '@angular/forms';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { fireEvent, render } from '@testing-library/angular';
import { TranslateTestingModule } from 'ngx-translate-testing';

import { SharedUiWohnsitzSplitterComponent } from './shared-ui-wohnsitz-splitter.component';
import { addWohnsitzControls } from '../utils/form';

describe(SharedUiWohnsitzSplitterComponent.name, () => {
  const fb = new FormBuilder().nonNullable;

  const initializeControls = () => {
    const form = fb.group(addWohnsitzControls(fb));
    return form;
  };

  it('should show component with empty values', async () => {
    await render(SharedUiWohnsitzSplitterComponent, {
      imports: [
        TranslateTestingModule.withTranslations({}),
        NoopAnimationsModule,
      ],
      componentProperties: {
        controls: initializeControls().controls,
      },
    });
  });

  it('should show component with empty values', async () => {
    const { getByTestId } = await render(SharedUiWohnsitzSplitterComponent, {
      imports: [
        TranslateTestingModule.withTranslations({}),
        NoopAnimationsModule,
      ],
      componentProperties: {
        controls: initializeControls().controls,
      },
    });
    expect(getByTestId('component-percentage-splitter-a')).toHaveValue('');
    expect(getByTestId('component-percentage-splitter-b')).toHaveValue('');
  });

  [
    [10, '10%', '90%'],
    [0, '0%', '100%'],
    [100, '100%', '0%'],
    [-1, '1%', '99%'],
    // [1.99, '1.99%', '99%'],
    [300, '100%', '0%'],
  ].forEach(([value, expectedA, expectedB]) =>
    it(`should show component with valueA: [${value}] to be A('${expectedA}') B('${expectedB}')`, async () => {
      const { getByTestId } = await render(SharedUiWohnsitzSplitterComponent, {
        imports: [
          TranslateTestingModule.withTranslations({}),
          NoopAnimationsModule,
        ],
        componentProperties: {
          controls: initializeControls().controls,
        },
      });
      const controlA = getByTestId('component-percentage-splitter-a');
      const controlB = getByTestId('component-percentage-splitter-b');

      fireEvent.input(controlA, { target: { value } });

      expect(controlA).toHaveValue(expectedA);
      expect(controlB).toHaveValue(expectedB);
    }),
  );
});
