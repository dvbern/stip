import { FormControl, FormGroup } from '@angular/forms';
import { Subject } from 'rxjs';

import {
  hasUnsavedChanges,
  observeUnsavedChanges,
} from './shared-util-unsaved-changes';

describe('SharedUtilUnsavedGuard', () => {
  it('should return false if form is pristine', () => {
    const form = new FormGroup({});
    expect(hasUnsavedChanges({ form })).toBe(false);
  });

  it('should return true if form is dirty', () => {
    const form = new FormGroup({});
    form.markAsDirty();
    expect(hasUnsavedChanges({ form })).toBe(true);
  });

  it('should notify about unsaved changes and also reset', () => {
    const form = new FormGroup({ test: new FormControl('') });
    const resetEvent = new Subject();
    const unsavedChanges$ = observeUnsavedChanges(form, resetEvent);
    const spy = vitest.fn();
    unsavedChanges$.subscribe(spy);
    form.markAsDirty();
    form.setValue({ test: 'test' });
    expect(spy).toHaveBeenLastCalledWith(true);
    resetEvent.next({});
    expect(spy).toHaveBeenLastCalledWith(false);
  });
});
