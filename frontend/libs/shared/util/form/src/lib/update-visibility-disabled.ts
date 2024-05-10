import { WritableSignal } from '@angular/core';
import { FormControl } from '@angular/forms';

export function updateVisbilityAndDisbledState(options: {
  hiddenFieldsSetSig: WritableSignal<Set<FormControl>>;
  formControl: FormControl;
  visible: boolean;
  disabled: boolean;
  resetOnInvisible?: boolean;
}): void {
  options.hiddenFieldsSetSig.update((hiddenFieldsSet) => {
    if (options.visible) {
      hiddenFieldsSet.delete(options.formControl);
      if (!options.disabled) {
        options.formControl.enable();
      }
    } else {
      hiddenFieldsSet.add(options.formControl);
      options.formControl.disable();
      if (options?.resetOnInvisible) {
        options.formControl.reset();
      }
    }
    return hiddenFieldsSet;
  });
}
