import { FormBuilder } from '@angular/forms';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { fireEvent, render, screen } from '@testing-library/angular';

// eslint-disable-next-line @nx/enforce-module-boundaries
import { getTranslocoModule } from '@dv/shared/pattern/vitest-test-setup';

import { SharedUiWohnsitzSplitterComponent } from './shared-ui-wohnsitz-splitter.component';
import { addWohnsitzControls } from '../utils/form';

describe(SharedUiWohnsitzSplitterComponent.name, () => {
  const fb = new FormBuilder().nonNullable;

  const initializeControls = () => {
    const form = fb.group(addWohnsitzControls(fb));
    return form;
  };
  const changes = {
    wohnsitzAnteilMutter: undefined,
    wohnsitzAnteilVater: undefined,
  };

  it('should show component with empty values', async () => {
    await render(SharedUiWohnsitzSplitterComponent, {
      imports: [getTranslocoModule(), NoopAnimationsModule],
      componentProperties: {
        controls: initializeControls().controls,
        changes,
      },
    });
  });

  it('should show component with empty values', async () => {
    await render(SharedUiWohnsitzSplitterComponent, {
      imports: [getTranslocoModule(), NoopAnimationsModule],
      componentProperties: {
        controls: initializeControls().controls,
        changes,
      },
    });
    expect(screen.getByTestId('component-percentage-splitter-a')).toHaveValue(
      '',
    );
    expect(screen.getByTestId('component-percentage-splitter-b')).toHaveValue(
      '',
    );
  });

  [
    [10, '10%', '90%'],
    [0, '0%', '100%'],
    [100, '100%', '0%'],
    [1, '1%', '99%'],
  ].forEach(([value, expectedA, expectedB]) =>
    it(`should show component with valueA: [${value}] to be A('${expectedA}') B('${expectedB}')`, async () => {
      await render(SharedUiWohnsitzSplitterComponent, {
        imports: [getTranslocoModule(), NoopAnimationsModule],
        componentProperties: {
          controls: initializeControls().controls,
          changes,
        },
      });
      const controlA = screen.getByTestId('component-percentage-splitter-a');
      const controlB = screen.getByTestId('component-percentage-splitter-b');

      fireEvent.input(controlA, { target: { value } });

      expect(controlA).toHaveValue(expectedA);
      expect(controlB).toHaveValue(expectedB);
    }),
  );
});
