import { ActiveDescendantKeyManager } from '@angular/cdk/a11y';
import {
  Directive,
  HostListener,
  QueryList,
  effect,
  input,
} from '@angular/core';

import { SharedUiFocusableListItemDirective } from '../shared-ui-focusable-list-item/shared-ui-focusable-list-item.directive';

@Directive({
  selector: '[dvSharedUiFocusableList]',
  standalone: true,
})
export class SharedUiFocusableListDirective {
  dvSharedUiFocusableListSig = input.required<
    QueryList<SharedUiFocusableListItemDirective> | undefined
  >({ alias: 'dvSharedUiFocusableList' });
  private keyManager?: ActiveDescendantKeyManager<SharedUiFocusableListItemDirective>;

  constructor() {
    effect(() => {
      const items = this.dvSharedUiFocusableListSig();
      if (items) {
        if (this.keyManager) {
          this.keyManager.destroy();
        }
        this.keyManager = new ActiveDescendantKeyManager(items).withWrap();
      }
    });
  }

  @HostListener('keydown', ['$event'])
  onKeydown(event: KeyboardEvent) {
    if (event.key === 'Enter') {
      this.keyManager?.activeItem?.interact();
    } else {
      this.keyManager?.onKeydown(event);
    }
  }
}
