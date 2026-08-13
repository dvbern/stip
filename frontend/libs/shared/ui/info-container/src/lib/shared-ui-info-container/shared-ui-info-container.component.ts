import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  HostBinding,
  Renderer2,
  effect,
  inject,
  input,
} from '@angular/core';

import { assertUnreachable } from '@dv/shared/model/type-util';

type AvailableTypes = 'info' | 'warning' | 'danger' | 'success' | 'light';

@Component({
  standalone: true,
  selector: 'dv-shared-ui-info-container',
  templateUrl: './shared-ui-info-container.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiInfoContainerComponent {
  typeSig = input<AvailableTypes>('info', {
    // eslint-disable-next-line @angular-eslint/no-input-rename
    alias: 'type',
  });
  @HostBinding('class') defaultClasses =
    'tw:flex tw:rounded-lg tw:p-4 tw:gap-4 tw:mb-4 tw:border-1';

  private renderer = inject(Renderer2);
  private elementRef = inject(ElementRef);

  constructor() {
    effect(() => {
      const type = this.typeSig();

      getColorForType(type).forEach((klass) =>
        this.renderer.addClass(this.elementRef.nativeElement, klass),
      );
    });
  }
}

const getColorForType = (type: AvailableTypes) => {
  switch (type) {
    case 'info':
      return [
        'tw:text-dv-blue-dark',
        'tw:border-dv-blue',
        'tw:bg-dv-blue-subtle',
      ];
    case 'warning':
      return [
        'tw:text-dv-yellow-dark',
        'tw:border-dv-yellow',
        'tw:bg-dv-yellow-subtle',
      ];
    case 'danger':
      return ['tw:text-dv-red-dark', 'tw:border-dv-red', 'tw:bg-dv-red-subtle'];
    case 'success':
      return [
        'tw:text-dv-green-dark',
        'tw:border-dv-green',
        'tw:bg-dv-green-subtle',
      ];
    case 'light':
      return [
        'tw:text-dv-gray-dark',
        'tw:border-dv-gray',
        'tw:bg-dv-gray-subtle',
      ];
    default: {
      assertUnreachable(type);
    }
  }
};
